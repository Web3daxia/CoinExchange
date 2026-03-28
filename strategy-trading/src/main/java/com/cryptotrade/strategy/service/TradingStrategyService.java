/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import com.cryptotrade.strategy.entity.TradingStrategy;

import java.util.List;

/**
 * 交易策略服务接口
 */
public interface TradingStrategyService {
    /**
     * 创建策略
     */
    TradingStrategy createStrategy(Long userId, TradingStrategy strategy);

    /**
     * 查询策略列表
     */
    List<TradingStrategy> getStrategies(Long userId, String marketType, String strategyType, 
                                        String pairName, String status);

    /**
     * 查询策略详情
     */
    TradingStrategy getStrategy(Long userId, Long strategyId);

    /**
     * 启动策略
     */
    TradingStrategy startStrategy(Long userId, Long strategyId);

    /**
     * 停止策略
     */
    TradingStrategy stopStrategy(Long userId, Long strategyId, Boolean closePositions, Boolean cancelOrders);

    /**
     * 暂停策略
     */
    TradingStrategy pauseStrategy(Long userId, Long strategyId);

    /**
     * 恢复策略
     */
    TradingStrategy resumeStrategy(Long userId, Long strategyId);

    /**
     * 更新策略参数
     */
    TradingStrategy updateStrategy(Long userId, Long strategyId, TradingStrategy strategy);

    /**
     * 删除策略
     */
    void deleteStrategy(Long userId, Long strategyId);
}













