/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 新闻分类Repository
 */
@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
    
    Optional<NewsCategory> findByCategoryCode(String categoryCode);
    
    List<NewsCategory> findByStatus(String status);
    
    List<NewsCategory> findByParentId(Long parentId);
    
    List<NewsCategory> findAllByOrderBySortOrderAsc();
}














