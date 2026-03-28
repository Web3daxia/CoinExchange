/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.request.CoinFuturesStrategyRequest;
import com.cryptotrade.futures.coin.entity.CoinFuturesStrategy;

import java.util.List;

public interface CoinFuturesStrategyService {
    /**
     * 配置合约策略
     */
    CoinFuturesStrategy configureStrategy(Long userId, CoinFuturesStrategyRequest request);

    /**
     * 执行合约策略
     */
    void executeStrategy(Long userId, Long strategyId);

    /**
     * 停止策略
     */
    void stopStrategy(Long userId, Long strategyId);

    /**
     * 查询用户的所有策略
     */
    List<CoinFuturesStrategy> getUserStrategies(Long userId);

    /**
     * 查询策略状态
     */
    CoinFuturesStrategy getStrategyStatus(Long userId, Long strategyId);

    /**
     * 执行所有运行中的策略（定时任务调用）
     */
    void executeAllStrategies();

    /**
     * 执行套利策略
     */
    void executeArbitrageStrategy(CoinFuturesStrategy strategy);

    /**
     * 执行对冲策略
     */
    void executeHedgeStrategy(CoinFuturesStrategy strategy);

    /**
     * 执行跨期套利策略
     */
    void executeInterTemporalArbitrageStrategy(CoinFuturesStrategy strategy);
}















