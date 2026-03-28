/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.dto.NewsDataDTO;
import com.cryptotrade.newsfeed.entity.NewsSource;

import java.util.List;

/**
 * 新闻采集处理器接口（用于适配不同新闻源的API）
 * 实现此接口以支持不同的新闻源API
 */
public interface NewsFetchProcessor {
    
    /**
     * 从新闻源采集新闻
     * @param source 新闻源配置
     * @return 新闻数据列表
     * @throws Exception 采集异常
     */
    List<NewsDataDTO> fetchFromSource(NewsSource source) throws Exception;
}














