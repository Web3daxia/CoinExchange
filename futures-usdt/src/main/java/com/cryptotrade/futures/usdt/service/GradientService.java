/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.entity.FuturesPosition;
import com.cryptotrade.futures.usdt.entity.GradientRule;

import java.math.BigDecimal;
import java.util.Optional;

public interface GradientService {
    /**
     * 查询梯度规则
     * @param pairName 交易对名称
     * @param positionSize 仓位大小
     * @return 适用的梯度规则
     */
    Optional<GradientRule> getGradientRule(String pairName, BigDecimal positionSize);

    /**
     * 根据仓位和波动率计算杠杆
     * @param position 仓位
     * @param volatility 市场波动率（0-1之间的小数）
     * @return 计算后的杠杆倍数
     */
    Integer calculateLeverage(FuturesPosition position, BigDecimal volatility);

    /**
     * 调整杠杆
     * @param positionId 仓位ID
     * @param newLeverage 新杠杆倍数
     */
    void adjustLeverage(Long positionId, Integer newLeverage);

    /**
     * 自动调整杠杆（定时任务调用）
     */
    void autoAdjustLeverage();
}















