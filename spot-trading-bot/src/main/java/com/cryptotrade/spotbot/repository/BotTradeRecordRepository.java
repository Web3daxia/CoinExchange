/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.repository;

import com.cryptotrade.spotbot.document.BotTradeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BotTradeRecordRepository extends MongoRepository<BotTradeRecord, String> {
    List<BotTradeRecord> findByPairName(String pairName);
    
    List<BotTradeRecord> findByPairNameAndSide(String pairName, String side);
    
    List<BotTradeRecord> findByPairNameAndTradeTimeBetween(String pairName, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'pairName': ?0, 'tradeTime': { $gte: ?1, $lte: ?2 } }")
    List<BotTradeRecord> findByPairNameAndTimeRange(String pairName, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'pairName': ?0, 'isMatchedWithUser': ?1 }")
    Page<BotTradeRecord> findByPairNameAndMatchedWithUser(String pairName, Boolean isMatchedWithUser, Pageable pageable);
    
    @Query("{ 'userId': ?0 }")
    List<BotTradeRecord> findByUserId(Long userId);
}














