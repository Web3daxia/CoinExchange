/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 新闻源Repository
 */
@Repository
public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
    
    Optional<NewsSource> findBySourceCode(String sourceCode);
    
    List<NewsSource> findByStatus(String status);
    
    List<NewsSource> findAllByOrderByPriorityDesc();
}














