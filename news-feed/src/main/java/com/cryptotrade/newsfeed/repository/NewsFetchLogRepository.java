/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.repository;

import com.cryptotrade.newsfeed.entity.NewsFetchLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻采集日志Repository
 */
@Repository
public interface NewsFetchLogRepository extends JpaRepository<NewsFetchLog, Long> {
    
    List<NewsFetchLog> findBySourceId(Long sourceId);
    
    Page<NewsFetchLog> findBySourceId(Long sourceId, Pageable pageable);
    
    List<NewsFetchLog> findByFetchStatus(String fetchStatus);
    
    Page<NewsFetchLog> findByFetchStatus(String fetchStatus, Pageable pageable);
}

