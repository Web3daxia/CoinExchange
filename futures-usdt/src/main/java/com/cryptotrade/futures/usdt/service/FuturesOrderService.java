/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.dto.request.CreateFuturesOrderRequest;
import com.cryptotrade.futures.usdt.dto.response.FuturesOrderResponse;
import com.cryptotrade.futures.usdt.entity.FuturesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface FuturesOrderService {
    /**
     * 创建订单
     */
    FuturesOrder createOrder(Long userId, CreateFuturesOrderRequest request);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 查询订单状态
     */
    FuturesOrderResponse getOrderStatus(Long userId, Long orderId);

    /**
     * 查询订单历史（分页）
     */
    Page<FuturesOrderResponse> getOrderHistory(Long userId, Pageable pageable);

    /**
     * 订单撮合
     */
    void matchOrders(FuturesOrder order);

    /**
     * 订单结算
     */
    void settleOrder(FuturesOrder order, BigDecimal filledQuantity, BigDecimal avgPrice);
}


