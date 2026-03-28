/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.dto.request.CreateFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.usdt.entity.FuturesAdvancedOrder;

import java.util.List;

public interface FuturesAdvancedOrderService {
    /**
     * 创建高级订单
     */
    FuturesAdvancedOrder createAdvancedOrder(Long userId, CreateFuturesAdvancedOrderRequest request);

    /**
     * 取消高级订单
     */
    void cancelAdvancedOrder(Long userId, Long orderId);

    /**
     * 查询用户的高级订单列表
     */
    List<FuturesAdvancedOrder> getUserAdvancedOrders(Long userId);

    /**
     * 执行所有待执行和激活状态的高级订单（定时任务调用）
     */
    void executeAdvancedOrders();

    /**
     * 执行高级限价单（GTC/IOC/FOK）
     */
    void executeAdvancedLimitOrder(FuturesAdvancedOrder order);

    /**
     * 执行追踪委托
     */
    void executeTrailingOrder(FuturesAdvancedOrder order);

    /**
     * 执行追逐限价单
     */
    void executeTrailingLimitOrder(FuturesAdvancedOrder order);

    /**
     * 执行冰山策略
     */
    void executeIcebergOrder(FuturesAdvancedOrder order);

    /**
     * 执行分段委托
     */
    void executeSegmentedOrder(FuturesAdvancedOrder order);

    /**
     * 执行分时委托
     */
    void executeTimeWeightedOrder(FuturesAdvancedOrder order);
}

