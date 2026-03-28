/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.repository;

import com.cryptotrade.strategy.entity.StrategyTradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 策略交易记录Repository
 */
@Repository
public interface StrategyTradeRecordRepository extends JpaRepository<StrategyTradeRecord, Long> {
    /**
     * 根据策略ID查询交易记录
     */
    List<StrategyTradeRecord> findByStrategyId(Long strategyId);

    /**
     * 根据用户ID查询交易记录
     */
    List<StrategyTradeRecord> findByUserId(Long userId);

    /**
     * 根据策略ID和方向查询交易记录
     */
    List<StrategyTradeRecord> findByStrategyIdAndSide(Long strategyId, String side);

    /**
     * 根据策略ID和操作查询交易记录
     */
    List<StrategyTradeRecord> findByStrategyIdAndAction(Long strategyId, String action);

    /**
     * 根据创建时间范围查询交易记录
     */
    @Query("SELECT r FROM StrategyTradeRecord r WHERE r.createdAt BETWEEN :startTime AND :endTime")
    List<StrategyTradeRecord> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}













