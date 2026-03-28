/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.entity.GridRobotDefaultStrategy;

import java.util.List;

/**
 * 网格机器人默认策略服务接口
 */
public interface GridRobotDefaultStrategyService {
    /**
     * 获取默认策略列表
     */
    List<GridRobotDefaultStrategy> getDefaultStrategies(String marketType);

    /**
     * 获取默认策略详情
     */
    GridRobotDefaultStrategy getDefaultStrategy(Long strategyId);

    /**
     * 创建默认策略（管理员）
     */
    GridRobotDefaultStrategy createDefaultStrategy(GridRobotDefaultStrategy strategy);

    /**
     * 更新默认策略（管理员）
     */
    GridRobotDefaultStrategy updateDefaultStrategy(Long strategyId, GridRobotDefaultStrategy strategy);

    /**
     * 删除默认策略（管理员）
     */
    void deleteDefaultStrategy(Long strategyId);
}













