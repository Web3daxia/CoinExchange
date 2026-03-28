/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.repository;

import com.cryptotrade.strategy.entity.TradingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易策略Repository
 */
@Repository
public interface TradingStrategyRepository extends JpaRepository<TradingStrategy, Long> {
    /**
     * 根据用户ID查询策略列表
     */
    List<TradingStrategy> findByUserId(Long userId);

    /**
     * 根据用户ID和状态查询策略列表
     */
    List<TradingStrategy> findByUserIdAndStatus(Long userId, String status);

    /**
     * 根据市场类型查询策略列表
     */
    List<TradingStrategy> findByMarketType(String marketType);

    /**
     * 根据策略类型查询策略列表
     */
    List<TradingStrategy> findByStrategyType(String strategyType);

    /**
     * 根据状态查询策略列表
     */
    List<TradingStrategy> findByStatus(String status);

    /**
     * 查询运行中的策略列表
     */
    @Query("SELECT s FROM TradingStrategy s WHERE s.status = 'RUNNING'")
    List<TradingStrategy> findRunningStrategies();
}













