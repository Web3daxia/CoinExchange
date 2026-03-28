/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service.impl;

import com.cryptotrade.newsfeed.entity.NewsSubscription;
import com.cryptotrade.newsfeed.repository.NewsSubscriptionRepository;
import com.cryptotrade.newsfeed.service.NewsSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 新闻订阅Service实现
 */
@Service
public class NewsSubscriptionServiceImpl implements NewsSubscriptionService {

    @Autowired
    private NewsSubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public NewsSubscription subscribe(Long userId, Long categoryId, Long tagId, String pushFrequency, String pushMethod) {
        NewsSubscription subscription = new NewsSubscription();
        subscription.setUserId(userId);
        subscription.setCategoryId(categoryId);
        subscription.setTagId(tagId);
        subscription.setPushFrequency(pushFrequency != null ? pushFrequency : "DAILY");
        subscription.setPushEnabled(true);
        subscription.setPushMethod(pushMethod);
        subscription.setNotificationPriority("NORMAL");
        return subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void unsubscribe(Long subscriptionId) {
        if (!subscriptionRepository.existsById(subscriptionId)) {
            throw new RuntimeException("订阅记录不存在: " + subscriptionId);
        }
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    @Transactional
    public NewsSubscription updateSubscription(Long subscriptionId, String pushFrequency, Boolean pushEnabled, String pushMethod) {
        NewsSubscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("订阅记录不存在: " + subscriptionId));

        if (pushFrequency != null) {
            subscription.setPushFrequency(pushFrequency);
        }
        if (pushEnabled != null) {
            subscription.setPushEnabled(pushEnabled);
        }
        if (pushMethod != null) {
            subscription.setPushMethod(pushMethod);
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<NewsSubscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<NewsSubscription> getSubscriptionsForPush(String pushFrequency) {
        return subscriptionRepository.findByPushFrequencyAndPushEnabledTrue(pushFrequency);
    }
}

