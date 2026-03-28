/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsTagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻标签关联Repository
 */
@Repository
public interface NewsTagRelationRepository extends JpaRepository<NewsTagRelation, Long> {
    
    List<NewsTagRelation> findByNewsId(Long newsId);
    
    List<NewsTagRelation> findByTagId(Long tagId);
    
    void deleteByNewsId(Long newsId);
    
    void deleteByNewsIdAndTagId(Long newsId, Long tagId);
}














