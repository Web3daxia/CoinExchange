/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsTranslation;

import java.util.List;

/**
 * 新闻翻译Service接口
 */
public interface NewsTranslationService {
    
    /**
     * 翻译新闻到指定语言
     */
    NewsTranslation translateNews(Long newsId, String targetLanguage);
    
    /**
     * 批量翻译新闻到所有支持的语言
     */
    void translateNewsToAllLanguages(Long newsId);
    
    /**
     * 获取新闻的翻译版本
     */
    NewsTranslation getNewsTranslation(Long newsId, String languageCode);
    
    /**
     * 获取新闻的所有翻译版本
     */
    List<NewsTranslation> getNewsTranslations(Long newsId);
    
    /**
     * 根据用户语言获取新闻（如果翻译存在则返回翻译，否则返回原文）
     */
    News getNewsByLanguage(Long newsId, String languageCode);
    
    /**
     * 重新翻译新闻
     */
    NewsTranslation retranslateNews(Long newsId, String languageCode);
    
    /**
     * 获取待翻译的新闻列表
     */
    List<News> getNewsPendingTranslation(String languageCode);
}














