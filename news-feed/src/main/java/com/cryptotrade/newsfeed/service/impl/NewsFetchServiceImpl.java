/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.dto.NewsDataDTO;
import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsFetchLog;
import com.cryptotrade.newsfeed.entity.NewsSource;
import com.cryptotrade.newsfeed.repository.NewsFetchLogRepository;
import com.cryptotrade.newsfeed.repository.NewsRepository;
import com.cryptotrade.newsfeed.repository.NewsSourceRepository;
import com.cryptotrade.newsfeed.service.NewsFetchService;
import com.cryptotrade.newsfeed.service.NewsFetchProcessor;
import com.cryptotrade.newsfeed.service.NewsTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻采集Service实现
 */
@Service
public class NewsFetchServiceImpl implements NewsFetchService {

    @Autowired
    private NewsSourceRepository sourceRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsFetchLogRepository fetchLogRepository;

    @Autowired(required = false)
    private NewsFetchProcessor newsFetchProcessor;

    @Autowired(required = false)
    private NewsTranslationService translationService;

    @Override
    @Transactional
    public int fetchNewsFromSource(Long sourceId) {
        NewsSource source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("新闻源不存在: " + sourceId));

        if (!"ACTIVE".equals(source.getStatus())) {
            throw new RuntimeException("新闻源未启用: " + sourceId);
        }

        long startTime = System.currentTimeMillis();
        int fetchedCount = 0;
        String errorMessage = null;
        String fetchStatus = "SUCCESS";

        try {
            // 调用API采集新闻
            List<NewsDataDTO> newsList = new ArrayList<>();
            if (newsFetchProcessor != null) {
                newsList = newsFetchProcessor.fetchFromSource(source);
            } else {
                // 如果没有配置采集处理器，返回空列表
                newsList = new ArrayList<>();
            }
            
            // 处理采集到的新闻（去重、分类、标签、敏感词过滤等）
            fetchedCount = processFetchedNews(newsList, sourceId);
            
            // 更新新闻源的最后采集时间
            source.setLastFetchTime(LocalDateTime.now());
            source.setStatus("ACTIVE");
            sourceRepository.save(source);

        } catch (Exception e) {
            fetchStatus = "FAILED";
            errorMessage = e.getMessage();
            // 标记新闻源为错误状态
            source.setStatus("ERROR");
            sourceRepository.save(source);
        }

        // 记录采集日志
        long executionTime = System.currentTimeMillis() - startTime;
        NewsFetchLog log = new NewsFetchLog();
        log.setSourceId(sourceId);
        log.setFetchTime(LocalDateTime.now());
        log.setFetchStatus(fetchStatus);
        log.setFetchedCount(fetchedCount);
        log.setErrorMessage(errorMessage);
        log.setExecutionTime(executionTime);
        fetchLogRepository.save(log);

        return fetchedCount;
    }

    @Override
    @Transactional
    public void fetchNewsFromAllSources() {
        List<NewsSource> sources = sourceRepository.findByStatus("ACTIVE");
        for (NewsSource source : sources) {
            try {
                // 检查是否需要采集（根据采集间隔）
                if (shouldFetch(source)) {
                    fetchNewsFromSource(source.getId());
                }
            } catch (Exception e) {
                // 记录错误但继续处理其他新闻源
                System.err.println("采集新闻源失败: " + source.getId() + ", 错误: " + e.getMessage());
            }
        }
    }

    @Override
    public List<NewsSource> getSourcesToFetch() {
        List<NewsSource> allSources = sourceRepository.findByStatus("ACTIVE");
        List<NewsSource> sourcesToFetch = new ArrayList<>();
        
        for (NewsSource source : allSources) {
            if (shouldFetch(source)) {
                sourcesToFetch.add(source);
            }
        }
        
        return sourcesToFetch;
    }

    @Override
    @Transactional
    public int processFetchedNews(List<NewsDataDTO> newsList, Long sourceId) {
        if (newsList == null || newsList.isEmpty()) {
            return 0;
        }

        int savedCount = 0;
        for (NewsDataDTO newsData : newsList) {
            try {
                // 检查是否已存在（通过sourceId和sourceArticleId去重）
                if (newsRepository.findBySourceIdAndSourceArticleId(sourceId, newsData.getSourceArticleId()).isPresent()) {
                    continue; // 已存在，跳过
                }

                // 创建新闻实体
                News news = new News();
                news.setSourceId(sourceId);
                news.setSourceArticleId(newsData.getSourceArticleId());
                news.setTitle(newsData.getTitle());
                news.setSummary(newsData.getSummary());
                news.setContent(newsData.getContent());
                news.setAuthor(newsData.getAuthor());
                news.setCoverImageUrl(newsData.getCoverImageUrl());
                news.setOriginalUrl(newsData.getOriginalUrl());
                news.setPublishTime(newsData.getPublishTime() != null ? newsData.getPublishTime() : LocalDateTime.now());
                news.setStatus("PENDING"); // 待审核
                
                // TODO: 自动分类和标签（需要实现分类和标签的自动识别逻辑）
                // news.setCategoryId(...);
                
                // TODO: 敏感词检测（需要实现敏感词检测逻辑）
                // List<String> sensitiveWords = sensitiveWordFilter.detect(...);
                // news.setSensitiveWords(...);

                News savedNews = newsRepository.save(news);
                savedCount++;

                // 自动翻译到所有支持的语言（异步执行，避免阻塞）
                if (translationService != null) {
                    try {
                        // 可以在这里异步调用翻译服务
                        // translationService.translateNewsToAllLanguages(savedNews.getId());
                        // 或者使用异步任务队列
                    } catch (Exception e) {
                        // 翻译失败不影响新闻保存
                        System.err.println("自动翻译失败: " + e.getMessage());
                    }
                }
                
            } catch (Exception e) {
                // 记录错误但继续处理其他新闻
                System.err.println("处理新闻失败: " + newsData.getTitle() + ", 错误: " + e.getMessage());
            }
        }

        return savedCount;
    }

    /**
     * 判断是否需要采集（根据采集间隔）
     */
    private boolean shouldFetch(NewsSource source) {
        if (source.getLastFetchTime() == null) {
            return true; // 从未采集过
        }

        LocalDateTime nextFetchTime = source.getLastFetchTime()
                .plusSeconds(source.getFetchInterval());
        
        return LocalDateTime.now().isAfter(nextFetchTime);
    }

}

