/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import com.cryptotrade.spot.dto.request.AdvancedOrderRequest;
import com.cryptotrade.spot.entity.AdvancedOrder;

import java.util.List;

public interface AdvancedOrderService {
    /**
     * 创建高级订单
     */
    AdvancedOrder createAdvancedOrder(Long userId, AdvancedOrderRequest request);

    /**
     * 取消高级订单
     */
    void cancelAdvancedOrder(Long userId, Long orderId);

    /**
     * 查询用户的高级订单列表
     */
    List<AdvancedOrder> getUserAdvancedOrders(Long userId);

    /**
     * 执行高级订单
     */
    void executeAdvancedOrders();

    /**
     * 执行高级限价单
     */
    void executeAdvancedLimitOrder(AdvancedOrder order);

    /**
     * 执行分时委托
     */
    void executeTimeWeightedOrder(AdvancedOrder order);

    /**
     * 执行循环委托
     */
    void executeRecurringOrder(AdvancedOrder order);

    /**
     * 执行追踪委托
     */
    void executeTrailingOrder(AdvancedOrder order);

    /**
     * 执行冰山策略
     */
    void executeIcebergOrder(AdvancedOrder order);
}















