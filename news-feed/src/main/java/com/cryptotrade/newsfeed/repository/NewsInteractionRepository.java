/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户新闻互动Repository
 */
@Repository
public interface NewsInteractionRepository extends JpaRepository<NewsInteraction, Long> {
    
    Optional<NewsInteraction> findByUserIdAndNewsIdAndInteractionType(Long userId, Long newsId, String interactionType);
    
    List<NewsInteraction> findByUserId(Long userId);
    
    List<NewsInteraction> findByNewsId(Long newsId);
    
    List<NewsInteraction> findByNewsIdAndInteractionType(Long newsId, String interactionType);
    
    boolean existsByUserIdAndNewsIdAndInteractionType(Long userId, Long newsId, String interactionType);
    
    Long countByNewsIdAndInteractionType(Long newsId, String interactionType);
}














