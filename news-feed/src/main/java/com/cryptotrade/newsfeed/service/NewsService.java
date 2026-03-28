/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.News;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 新闻Service接口
 */
public interface NewsService {
    
    /**
     * 获取新闻列表（分页）
     */
    Page<News> getNewsList(String status, Integer page, Integer size);
    
    /**
     * 获取热门新闻
     */
    Page<News> getHotNews(Integer page, Integer size);
    
    /**
     * 获取推荐新闻
     */
    Page<News> getRecommendedNews(Integer page, Integer size);
    
    /**
     * 根据分类获取新闻
     */
    Page<News> getNewsByCategory(Long categoryId, Integer page, Integer size);
    
    /**
     * 根据标签获取新闻
     */
    Page<News> getNewsByTag(Long tagId, Integer page, Integer size);
    
    /**
     * 搜索新闻
     */
    Page<News> searchNews(String keyword, Integer page, Integer size);
    
    /**
     * 根据ID获取新闻详情
     */
    News getNewsById(Long newsId);
    
    /**
     * 根据ID和语言获取新闻详情（自动返回翻译版本）
     */
    News getNewsById(Long newsId, String languageCode);
    
    /**
     * 增加阅读量
     */
    void incrementViewCount(Long newsId);
    
    /**
     * 审核新闻
     */
    News reviewNews(Long newsId, Long reviewerId, String status, String remark);
    
    /**
     * 设置热门新闻
     */
    void setHotNews(Long newsId, boolean isHot);
    
    /**
     * 设置推荐新闻
     */
    void setRecommendedNews(Long newsId, boolean isRecommended);
}

