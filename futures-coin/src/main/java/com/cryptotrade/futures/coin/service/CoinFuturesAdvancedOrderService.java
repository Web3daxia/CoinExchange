/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.coin.entity.CoinFuturesAdvancedOrder;

import java.util.List;

public interface CoinFuturesAdvancedOrderService {
    /**
     * 创建高级订单
     */
    CoinFuturesAdvancedOrder createAdvancedOrder(Long userId, CreateCoinFuturesAdvancedOrderRequest request);

    /**
     * 取消高级订单
     */
    void cancelAdvancedOrder(Long userId, Long orderId);

    /**
     * 查询用户的高级订单列表
     */
    List<CoinFuturesAdvancedOrder> getUserAdvancedOrders(Long userId);

    /**
     * 执行所有待执行和激活状态的高级订单（定时任务调用）
     */
    void executeAdvancedOrders();

    /**
     * 执行高级限价单
     */
    void executeAdvancedLimitOrder(CoinFuturesAdvancedOrder order);

    /**
     * 执行追踪委托
     */
    void executeTrailingOrder(CoinFuturesAdvancedOrder order);

    /**
     * 执行追逐限价单
     */
    void executeTrailingLimitOrder(CoinFuturesAdvancedOrder order);

    /**
     * 执行冰山策略
     */
    void executeIcebergOrder(CoinFuturesAdvancedOrder order);

    /**
     * 执行分段委托
     */
    void executeSegmentedOrder(CoinFuturesAdvancedOrder order);

    /**
     * 执行分时委托
     */
    void executeTimeWeightedOrder(CoinFuturesAdvancedOrder order);
}















