/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.service;

import com.cryptotrade.flashexchange.entity.FlashExchangeOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 闪兑交易服务接口
 */
public interface FlashExchangeService {
    /**
     * 查询支持的闪兑币种及实时汇率
     * @param fromCurrency 源币种（可选）
     * @param toCurrency 目标币种（可选）
     * @return 支持的币种和汇率信息
     */
    Map<String, Object> getSupportedCurrencies(String fromCurrency, String toCurrency);

    /**
     * 计算兑换数量
     * @param fromCurrency 源币种
     * @param toCurrency 目标币种
     * @param fromAmount 源币种数量
     * @return 兑换信息（目标数量、汇率、手续费等）
     */
    Map<String, Object> calculateExchange(String fromCurrency, String toCurrency, BigDecimal fromAmount);

    /**
     * 执行闪兑交易
     * @param userId 用户ID
     * @param fromCurrency 源币种
     * @param toCurrency 目标币种
     * @param fromAmount 源币种数量
     * @param maxSlippage 最大允许滑点
     * @return 订单
     */
    FlashExchangeOrder executeExchange(Long userId, String fromCurrency, String toCurrency,
                                      BigDecimal fromAmount, BigDecimal maxSlippage);

    /**
     * 查询用户兑换历史
     * @param userId 用户ID
     * @return 订单列表
     */
    List<FlashExchangeOrder> getUserExchangeHistory(Long userId);

    /**
     * 查询订单详情
     * @param orderId 订单ID
     * @return 订单
     */
    FlashExchangeOrder getOrder(Long orderId);
}















