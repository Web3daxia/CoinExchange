/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 新闻标签Repository
 */
@Repository
public interface NewsTagRepository extends JpaRepository<NewsTag, Long> {
    
    Optional<NewsTag> findByTagCode(String tagCode);
    
    List<NewsTag> findByStatus(String status);
    
    List<NewsTag> findAllByOrderBySortOrderAsc();
}














