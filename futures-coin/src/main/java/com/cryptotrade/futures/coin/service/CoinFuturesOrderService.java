/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesOrderResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CoinFuturesOrderService {
    /**
     * 创建订单
     */
    CoinFuturesOrder createOrder(Long userId, CreateCoinFuturesOrderRequest request);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 查询订单状态
     */
    CoinFuturesOrderResponse getOrderStatus(Long userId, Long orderId);

    /**
     * 查询订单历史（分页）
     */
    Page<CoinFuturesOrderResponse> getOrderHistory(Long userId, Pageable pageable);

    /**
     * 订单撮合
     */
    void matchOrders(CoinFuturesOrder order);

    /**
     * 订单结算
     */
    void settleOrder(CoinFuturesOrder order, BigDecimal filledQuantity, BigDecimal avgPrice);
}















