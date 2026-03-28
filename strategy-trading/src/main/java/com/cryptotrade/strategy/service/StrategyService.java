/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import java.util.Map;

/**
 * 策略服务接口
 * 所有策略实现类都需要实现此接口
 */
public interface StrategyService {
    /**
     * 执行策略
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param marketType 市场类型：SPOT、FUTURES_USDT、FUTURES_COIN、LEVERAGED
     * @param strategyParams 策略参数（Map格式）
     * @throws Exception 策略执行异常
     */
    void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception;

    /**
     * 获取策略类型
     * @return 策略类型
     */
    String getStrategyType();

    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
}















