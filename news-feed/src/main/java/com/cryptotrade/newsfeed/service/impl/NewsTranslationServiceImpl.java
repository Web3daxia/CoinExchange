/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsTranslation;
import com.cryptotrade.newsfeed.repository.NewsRepository;
import com.cryptotrade.newsfeed.repository.NewsTranslationRepository;
import com.cryptotrade.newsfeed.service.NewsTranslationService;
import com.cryptotrade.newsfeed.service.TranslationProviderFactory;
import com.cryptotrade.newsfeed.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻翻译Service实现
 */
@Service
public class NewsTranslationServiceImpl implements NewsTranslationService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsTranslationRepository translationRepository;

    @Autowired(required = false)
    private TranslationProviderFactory translationProviderFactory;

    @Override
    @Transactional
    public NewsTranslation translateNews(Long newsId, String targetLanguage) {
        if (!LanguageUtil.isLanguageSupported(targetLanguage)) {
            throw new RuntimeException("不支持的语言: " + targetLanguage);
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));

        // 检查是否已存在翻译
        NewsTranslation existing = translationRepository.findByNewsIdAndLanguageCode(newsId, targetLanguage)
                .orElse(null);

        if (existing != null && "COMPLETED".equals(existing.getTranslationStatus())) {
            return existing; // 已翻译完成，直接返回
        }

        // 创建或更新翻译记录
        NewsTranslation translation = existing != null ? existing : new NewsTranslation();
        translation.setNewsId(newsId);
        translation.setLanguageCode(targetLanguage);
        translation.setTranslationStatus("TRANSLATING");
        translation = translationRepository.save(translation);

        try {
            // 调用翻译服务
            TranslationResult result = translateText(
                    news.getTitle(),
                    news.getSummary(),
                    news.getContent(),
                    targetLanguage
            );

            translation.setTitle(result.getTitle());
            translation.setSummary(result.getSummary());
            translation.setContent(result.getContent());
            translation.setTranslationStatus("COMPLETED");
            translation.setTranslationTime(LocalDateTime.now());
            translation = translationRepository.save(translation);

        } catch (Exception e) {
            translation.setTranslationStatus("FAILED");
            translationRepository.save(translation);
            throw new RuntimeException("翻译失败: " + e.getMessage(), e);
        }

        return translation;
    }

    @Override
    @Transactional
    public void translateNewsToAllLanguages(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));

        // 获取新闻的源语言（假设原始新闻是英文，默认语言为en-US）
        String sourceLanguage = LanguageUtil.getDefaultLanguage(); // en-US

        // 遍历所有支持的语言
        for (String targetLanguage : LanguageUtil.SUPPORTED_LANGUAGES) {
            // 跳过源语言（如果是英文，则英文不需要翻译）
            if (targetLanguage.equals(sourceLanguage)) {
                continue;
            }

            // 检查是否已存在翻译
            boolean exists = translationRepository.findByNewsIdAndLanguageCode(newsId, targetLanguage).isPresent();
            if (!exists) {
                try {
                    translateNews(newsId, targetLanguage);
                } catch (Exception e) {
                    // 记录错误但继续处理其他语言
                    System.err.println("翻译到 " + targetLanguage + " 失败: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public NewsTranslation getNewsTranslation(Long newsId, String languageCode) {
        return translationRepository.findByNewsIdAndLanguageCode(newsId, languageCode)
                .orElse(null);
    }

    @Override
    public List<NewsTranslation> getNewsTranslations(Long newsId) {
        return translationRepository.findByNewsId(newsId);
    }

    @Override
    public News getNewsByLanguage(Long newsId, String languageCode) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("新闻不存在: " + newsId));

        // 如果是默认语言，直接返回原文
        if (LanguageUtil.getDefaultLanguage().equals(languageCode)) {
            return news;
        }

        // 获取翻译版本
        NewsTranslation translation = getNewsTranslation(newsId, languageCode);
        if (translation != null && "COMPLETED".equals(translation.getTranslationStatus())) {
            // 创建新的News对象，使用翻译内容
            News translatedNews = new News();
            translatedNews.setId(news.getId());
            translatedNews.setSourceId(news.getSourceId());
            translatedNews.setTitle(translation.getTitle());
            translatedNews.setSummary(translation.getSummary());
            translatedNews.setContent(translation.getContent());
            translatedNews.setAuthor(news.getAuthor());
            translatedNews.setCoverImageUrl(news.getCoverImageUrl());
            translatedNews.setOriginalUrl(news.getOriginalUrl());
            translatedNews.setPublishTime(news.getPublishTime());
            translatedNews.setCategoryId(news.getCategoryId());
            translatedNews.setStatus(news.getStatus());
            translatedNews.setViewCount(news.getViewCount());
            translatedNews.setLikeCount(news.getLikeCount());
            translatedNews.setShareCount(news.getShareCount());
            translatedNews.setCommentCount(news.getCommentCount());
            return translatedNews;
        }

        // 如果没有翻译，返回原文
        return news;
    }

    @Override
    @Transactional
    public NewsTranslation retranslateNews(Long newsId, String languageCode) {
        // 删除旧翻译
        translationRepository.findByNewsIdAndLanguageCode(newsId, languageCode)
                .ifPresent(translationRepository::delete);

        // 重新翻译
        return translateNews(newsId, languageCode);
    }

    @Override
    public List<News> getNewsPendingTranslation(String languageCode) {
        List<News> allNews = newsRepository.findAll();
        List<News> pendingNews = new ArrayList<>();

        for (News news : allNews) {
            boolean hasTranslation = translationRepository
                    .findByNewsIdAndLanguageCode(news.getId(), languageCode)
                    .isPresent();
            if (!hasTranslation) {
                pendingNews.add(news);
            }
        }

        return pendingNews;
    }

    /**
     * 调用翻译服务翻译文本
     */
    private TranslationResult translateText(String title, String summary, String content, String targetLanguage) {
        if (translationProviderFactory != null) {
            NewsTranslationServiceImpl.TranslationProvider provider = translationProviderFactory.getActiveProvider();
            if (provider != null) {
                try {
                    return provider.translate(title, summary, content, targetLanguage);
                } catch (Exception e) {
                    throw new RuntimeException("翻译失败: " + e.getMessage(), e);
                }
            }
        }
        // 如果没有配置翻译服务，抛出异常
        throw new RuntimeException("翻译服务未配置或未启用，请在系统管理中配置翻译服务");
    }

    /**
     * 翻译结果DTO
     */
    public static class TranslationResult {
        private String title;
        private String summary;
        private String content;

        public TranslationResult(String title, String summary, String content) {
            this.title = title;
            this.summary = summary;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getSummary() {
            return summary;
        }

        public String getContent() {
            return content;
        }
    }

    /**
     * 翻译服务提供者接口（需要根据实际使用的翻译API实现）
     */
    public interface TranslationProvider {
        TranslationResult translate(String title, String summary, String content, String targetLanguage) throws Exception;
    }
}

