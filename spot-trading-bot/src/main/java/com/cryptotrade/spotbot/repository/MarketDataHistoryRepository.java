/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.repository;

import com.cryptotrade.spotbot.document.MarketDataHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MarketDataHistoryRepository extends MongoRepository<MarketDataHistory, String> {
    List<MarketDataHistory> findByPairNameAndMarketType(String pairName, String marketType);
    
    @Query("{ 'pairName': ?0, 'marketType': ?1, 'timestamp': { $gte: ?2, $lte: ?3 } }")
    List<MarketDataHistory> findByPairNameAndMarketTypeAndTimeRange(
            String pairName, String marketType, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'pairName': ?0, 'marketType': ?1, 'interval': ?2, 'timestamp': { $gte: ?3, $lte: ?4 } }")
    List<MarketDataHistory> findByPairNameAndMarketTypeAndIntervalAndTimeRange(
            String pairName, String marketType, String interval, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'pairName': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    Page<MarketDataHistory> findByPairNameAndTimeRange(
            String pairName, LocalDateTime start, LocalDateTime end, Pageable pageable);
}














