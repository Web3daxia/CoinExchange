/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import com.cryptotrade.options.entity.OptionStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 期权策略服务接口
 */
public interface OptionStrategyService {
    /**
     * 创建跨式期权策略（Straddle）
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param strikePrice 执行价格
     * @param expiryDate 到期日期
     * @param quantity 数量
     * @return 策略
     */
    OptionStrategy createStraddleStrategy(Long userId, String pairName, BigDecimal strikePrice,
                                        java.time.LocalDateTime expiryDate, BigDecimal quantity);

    /**
     * 创建蝶式期权策略（Butterfly Spread）
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param lowerStrike 较低执行价格
     * @param middleStrike 中间执行价格
     * @param upperStrike 较高执行价格
     * @param expiryDate 到期日期
     * @param quantity 数量
     * @param optionType CALL或PUT
     * @return 策略
     */
    OptionStrategy createButterflyStrategy(Long userId, String pairName, BigDecimal lowerStrike,
                                          BigDecimal middleStrike, BigDecimal upperStrike,
                                          java.time.LocalDateTime expiryDate, BigDecimal quantity, String optionType);

    /**
     * 创建牛市/熊市价差策略（Vertical Spread）
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param buyStrike 买入执行价格
     * @param sellStrike 卖出执行价格
     * @param expiryDate 到期日期
     * @param quantity 数量
     * @param optionType CALL或PUT
     * @param direction BULLISH（牛市）或BEARISH（熊市）
     * @return 策略
     */
    OptionStrategy createVerticalSpreadStrategy(Long userId, String pairName, BigDecimal buyStrike,
                                                BigDecimal sellStrike, java.time.LocalDateTime expiryDate,
                                                BigDecimal quantity, String optionType, String direction);

    /**
     * 创建日历价差策略（Calendar Spread）
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param strikePrice 执行价格
     * @param nearExpiryDate 近期到期日期
     * @param farExpiryDate 远期到期日期
     * @param quantity 数量
     * @param optionType CALL或PUT
     * @return 策略
     */
    OptionStrategy createCalendarSpreadStrategy(Long userId, String pairName, BigDecimal strikePrice,
                                                java.time.LocalDateTime nearExpiryDate,
                                                java.time.LocalDateTime farExpiryDate,
                                                BigDecimal quantity, String optionType);

    /**
     * 查询用户策略列表
     * @param userId 用户ID
     * @return 策略列表
     */
    List<OptionStrategy> getUserStrategies(Long userId);

    /**
     * 查询策略详情
     * @param userId 用户ID
     * @param strategyId 策略ID
     * @return 策略
     */
    OptionStrategy getStrategy(Long userId, Long strategyId);

    /**
     * 关闭策略
     * @param userId 用户ID
     * @param strategyId 策略ID
     */
    void closeStrategy(Long userId, Long strategyId);

    /**
     * 更新策略价值
     * @param strategyId 策略ID
     */
    void updateStrategyValue(Long strategyId);
}















