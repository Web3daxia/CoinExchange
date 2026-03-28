/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedStrategy;

import java.util.List;

/**
 * 杠杆交易策略服务接口
 */
public interface LeveragedStrategyService {
    /**
     * 配置杠杆交易策略
     * @param userId 用户ID
     * @param accountId 账户ID
     * @param strategyName 策略名称
     * @param strategyType 策略类型
     * @param pairName 交易对名称
     * @param strategyParams 策略参数（JSON格式）
     * @param leverage 杠杆倍数
     * @return 策略
     */
    LeveragedStrategy configureStrategy(Long userId, Long accountId, String strategyName,
                                       String strategyType, String pairName, String strategyParams, Integer leverage);

    /**
     * 执行杠杆交易策略
     * @param strategyId 策略ID
     */
    void executeStrategy(Long strategyId);

    /**
     * 停止策略
     * @param userId 用户ID
     * @param strategyId 策略ID
     */
    void stopStrategy(Long userId, Long strategyId);

    /**
     * 查询用户策略列表
     * @param userId 用户ID
     * @return 策略列表
     */
    List<LeveragedStrategy> getUserStrategies(Long userId);

    /**
     * 执行所有活跃策略
     */
    void executeAllStrategies();
}















