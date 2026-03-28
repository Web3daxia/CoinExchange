/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.NewsInteraction;

/**
 * 新闻互动Service接口
 */
public interface NewsInteractionService {
    
    /**
     * 点赞/取消点赞
     */
    NewsInteraction likeNews(Long userId, Long newsId);
    
    /**
     * 分享新闻
     */
    NewsInteraction shareNews(Long userId, Long newsId, String platform);
    
    /**
     * 记录浏览
     */
    void viewNews(Long userId, Long newsId);
    
    /**
     * 检查是否已点赞
     */
    boolean isLiked(Long userId, Long newsId);
    
    /**
     * 获取新闻的点赞数
     */
    Long getLikeCount(Long newsId);
    
    /**
     * 获取新闻的分享数
     */
    Long getShareCount(Long newsId);
}














