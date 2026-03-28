/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service;

import com.cryptotrade.selftrading.entity.SelfTradingOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自选交易订单服务接口
 */
public interface SelfTradingOrderService {
    /**
     * 创建订单
     * @param userId 用户ID
     * @param adId 广告ID
     * @param cryptoAmount 加密货币数量
     * @return 订单
     */
    SelfTradingOrder createOrder(Long userId, Long adId, BigDecimal cryptoAmount);

    /**
     * 确认支付
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentProof 支付凭证
     */
    void confirmPayment(Long userId, Long orderId, String paymentProof);

    /**
     * 放币
     * @param merchantId 商家ID
     * @param orderId 订单ID
     */
    void releaseCrypto(Long merchantId, Long orderId);

    /**
     * 取消订单
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param reason 取消原因
     */
    void cancelOrder(Long userId, Long orderId, String reason);

    /**
     * 查询用户订单
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    List<SelfTradingOrder> getUserOrders(Long userId, String status);

    /**
     * 查询商家订单
     * @param merchantId 商家ID
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    List<SelfTradingOrder> getMerchantOrders(Long merchantId, String status);

    /**
     * 查询订单详情
     * @param orderId 订单ID
     * @return 订单
     */
    SelfTradingOrder getOrder(Long orderId);
}















