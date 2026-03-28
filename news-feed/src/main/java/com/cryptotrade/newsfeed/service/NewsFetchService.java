/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.NewsSource;

import java.util.List;

/**
 * 新闻采集Service接口
 */
public interface NewsFetchService {
    
    /**
     * 从指定新闻源采集新闻
     */
    int fetchNewsFromSource(Long sourceId);
    
    /**
     * 从所有启用的新闻源采集新闻
     */
    void fetchNewsFromAllSources();
    
    /**
     * 获取需要采集的新闻源列表
     */
    List<NewsSource> getSourcesToFetch();
    
    /**
     * 处理采集到的新闻（去重、分类、标签、敏感词过滤等）
     * @return 成功保存的新闻数量
     */
    int processFetchedNews(List<com.cryptotrade.newsfeed.dto.NewsDataDTO> newsList, Long sourceId);
}

