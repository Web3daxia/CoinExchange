/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.repository;

import com.cryptotrade.strategy.entity.StrategyBacktest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 策略回测Repository
 */
@Repository
public interface StrategyBacktestRepository extends JpaRepository<StrategyBacktest, Long> {
    /**
     * 根据回测ID查询
     */
    Optional<StrategyBacktest> findByBacktestId(String backtestId);

    /**
     * 根据策略ID查询回测记录
     */
    List<StrategyBacktest> findByStrategyId(Long strategyId);

    /**
     * 根据用户ID查询回测记录
     */
    List<StrategyBacktest> findByUserId(Long userId);

    /**
     * 根据状态查询回测记录
     */
    List<StrategyBacktest> findByStatus(String status);

    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT b FROM StrategyBacktest b WHERE b.createdAt BETWEEN :startTime AND :endTime")
    List<StrategyBacktest> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}













