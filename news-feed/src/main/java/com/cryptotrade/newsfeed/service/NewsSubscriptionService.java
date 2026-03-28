/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.entity.NewsSubscription;

import java.util.List;

/**
 * 新闻订阅Service接口
 */
public interface NewsSubscriptionService {
    
    /**
     * 订阅新闻
     */
    NewsSubscription subscribe(Long userId, Long categoryId, Long tagId, String pushFrequency, String pushMethod);
    
    /**
     * 取消订阅
     */
    void unsubscribe(Long subscriptionId);
    
    /**
     * 更新订阅设置
     */
    NewsSubscription updateSubscription(Long subscriptionId, String pushFrequency, Boolean pushEnabled, String pushMethod);
    
    /**
     * 获取用户的订阅列表
     */
    List<NewsSubscription> getUserSubscriptions(Long userId);
    
    /**
     * 获取需要推送的用户订阅
     */
    List<NewsSubscription> getSubscriptionsForPush(String pushFrequency);
}














