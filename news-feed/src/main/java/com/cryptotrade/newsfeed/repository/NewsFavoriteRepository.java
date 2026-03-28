/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户新闻收藏Repository
 */
@Repository
public interface NewsFavoriteRepository extends JpaRepository<NewsFavorite, Long> {
    
    Optional<NewsFavorite> findByUserIdAndNewsId(Long userId, Long newsId);
    
    List<NewsFavorite> findByUserId(Long userId);
    
    Page<NewsFavorite> findByUserId(Long userId, Pageable pageable);
    
    boolean existsByUserIdAndNewsId(Long userId, Long newsId);
}














