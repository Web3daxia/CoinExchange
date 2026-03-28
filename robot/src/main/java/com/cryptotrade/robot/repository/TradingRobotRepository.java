/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.TradingRobot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交易机器人Repository
 */
@Repository
public interface TradingRobotRepository extends JpaRepository<TradingRobot, Long> {
    /**
     * 根据用户ID查询机器人列表
     */
    List<TradingRobot> findByUserId(Long userId);

    /**
     * 根据用户ID查询机器人列表（按创建时间倒序）
     */
    List<TradingRobot> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据用户ID和状态查询机器人列表
     */
    List<TradingRobot> findByUserIdAndStatus(Long userId, String status);

    /**
     * 根据市场类型查询机器人列表
     */
    List<TradingRobot> findByMarketType(String marketType);

    /**
     * 根据策略类型查询机器人列表
     */
    List<TradingRobot> findByStrategyType(String strategyType);

    /**
     * 根据状态查询机器人列表
     */
    List<TradingRobot> findByStatus(String status);
}
