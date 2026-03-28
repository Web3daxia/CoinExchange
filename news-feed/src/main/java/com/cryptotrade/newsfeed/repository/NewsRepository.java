/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 新闻Repository
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    
    Optional<News> findBySourceIdAndSourceArticleId(Long sourceId, String sourceArticleId);
    
    List<News> findByStatus(String status);
    
    List<News> findByCategoryId(Long categoryId);
    
    List<News> findBySourceId(Long sourceId);
    
    Page<News> findByStatus(String status, Pageable pageable);
    
    Page<News> findByStatusOrderByPublishTimeDesc(String status, Pageable pageable);
    
    Page<News> findByIsHotTrueAndStatus(String status, Pageable pageable);
    
    Page<News> findByIsRecommendedTrueAndStatus(String status, Pageable pageable);
    
    @Query("SELECT n FROM News n WHERE n.status = :status AND n.publishTime BETWEEN :startTime AND :endTime ORDER BY n.publishTime DESC")
    Page<News> findByStatusAndPublishTimeBetween(String status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}














