/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import java.math.BigDecimal;

public interface TradingFeeService {
    /**
     * 计算交易手续费
     */
    BigDecimal calculateFee(Long userId, String pairName, BigDecimal quantity, 
                           BigDecimal price, String side);

    /**
     * 获取费率
     */
    BigDecimal getFeeRate(Long userId);

    /**
     * 计算使用平台代币支付的手续费（享受折扣）
     */
    BigDecimal calculateFeeWithDiscount(Long userId, BigDecimal fee, boolean usePlatformToken);
}















