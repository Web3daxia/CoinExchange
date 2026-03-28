/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.service;

import com.cryptotrade.quickbuy.entity.QuickBuyOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 快捷买币服务接口
 */
public interface QuickBuyService {
    /**
     * 查询可用于快捷购买的加密货币及支付方式
     * @return 支持的加密货币和支付方式列表
     */
    Map<String, Object> getAvailableOptions();

    /**
     * 执行快捷买币交易
     * @param userId 用户ID
     * @param cryptoCurrency 加密货币类型
     * @param fiatCurrency 法币类型
     * @param paymentMethod 支付方式
     * @param cryptoAmount 购买的加密货币数量
     * @return 订单
     */
    QuickBuyOrder executeQuickBuy(Long userId, String cryptoCurrency, String fiatCurrency,
                                  String paymentMethod, BigDecimal cryptoAmount);

    /**
     * 查询用户订单历史
     * @param userId 用户ID
     * @return 订单列表
     */
    List<QuickBuyOrder> getUserOrders(Long userId);

    /**
     * 确认支付
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentProof 支付凭证
     */
    void confirmPayment(Long userId, Long orderId, String paymentProof);
}















