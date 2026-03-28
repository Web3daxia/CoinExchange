/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.GridRobotDefaultStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网格机器人默认策略Repository
 */
@Repository
public interface GridRobotDefaultStrategyRepository extends JpaRepository<GridRobotDefaultStrategy, Long> {
    /**
     * 根据市场类型查询默认策略
     */
    List<GridRobotDefaultStrategy> findByMarketType(String marketType);

    /**
     * 根据状态查询默认策略
     */
    List<GridRobotDefaultStrategy> findByStatus(String status);

    /**
     * 根据风险等级查询默认策略
     */
    List<GridRobotDefaultStrategy> findByRiskLevel(String riskLevel);

    /**
     * 根据市场类型和状态查询默认策略
     */
    List<GridRobotDefaultStrategy> findByMarketTypeAndStatus(String marketType, String status);
}

