/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.usdt.entity.GradientRule;

import java.math.BigDecimal;
import java.util.Optional;

public interface CoinFuturesGradientService {
    /**
     * 查询梯度规则
     * @param pairName 交易对名称
     * @param positionQuantity 仓位大小
     * @return 适用的梯度规则
     */
    Optional<GradientRule> getGradientRule(String pairName, BigDecimal positionQuantity);

    /**
     * 根据仓位和波动率计算杠杆
     * @param position 仓位
     * @param volatilityFactor 市场波动率因子
     * @return 计算后的杠杆倍数
     */
    Integer calculateLeverage(CoinFuturesPosition position, BigDecimal volatilityFactor);

    /**
     * 调整杠杆
     * @param userId 用户ID
     * @param positionId 仓位ID
     * @param newLeverage 新的杠杆倍数
     * @return 更新后的仓位
     */
    CoinFuturesPosition adjustLeverage(Long userId, Long positionId, Integer newLeverage);

    /**
     * 自动调整杠杆（定时任务）
     */
    void autoAdjustLeverage();
}















