/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户新闻订阅Repository
 */
@Repository
public interface NewsSubscriptionRepository extends JpaRepository<NewsSubscription, Long> {
    
    List<NewsSubscription> findByUserId(Long userId);
    
    List<NewsSubscription> findByUserIdAndPushEnabledTrue(Long userId);
    
    List<NewsSubscription> findByCategoryId(Long categoryId);
    
    List<NewsSubscription> findByTagId(Long tagId);
    
    List<NewsSubscription> findByPushFrequencyAndPushEnabledTrue(String pushFrequency);
}

