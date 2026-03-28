/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.spot.dto.response.OrderResponse;
import com.cryptotrade.spot.entity.SpotOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface SpotOrderService {
    /**
     * 创建订单
     */
    SpotOrder createOrder(Long userId, CreateOrderRequest request);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 查询订单状态
     */
    OrderResponse getOrderStatus(Long userId, Long orderId);

    /**
     * 查询订单历史（分页）
     */
    Page<OrderResponse> getOrderHistory(Long userId, Pageable pageable);

    /**
     * 订单撮合
     */
    void matchOrders(SpotOrder order);

    /**
     * 订单结算
     */
    void settleOrder(SpotOrder order, BigDecimal filledQuantity, BigDecimal avgPrice);
}















