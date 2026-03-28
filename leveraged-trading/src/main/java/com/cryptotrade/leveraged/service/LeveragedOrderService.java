/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedOrder;
import com.cryptotrade.leveraged.entity.LeveragedPosition;

import java.math.BigDecimal;
import java.util.List;

/**
 * 杠杆订单服务接口
 */
public interface LeveragedOrderService {
    /**
     * 创建市价单
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param side 方向：BUY（买入）、SELL（卖出）
     * @param action 操作：OPEN（开仓）、CLOSE（平仓）
     * @param quantity 数量
     * @param leverage 杠杆倍数
     * @return 订单
     */
    LeveragedOrder createMarketOrder(Long userId, String pairName, String side, String action,
                                    BigDecimal quantity, Integer leverage);

    /**
     * 创建限价单
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param side 方向
     * @param action 操作
     * @param quantity 数量
     * @param price 价格
     * @param leverage 杠杆倍数
     * @return 订单
     */
    LeveragedOrder createLimitOrder(Long userId, String pairName, String side, String action,
                                   BigDecimal quantity, BigDecimal price, Integer leverage);

    /**
     * 创建止损单
     * @param userId 用户ID
     * @param positionId 仓位ID
     * @param stopPrice 止损价格
     * @return 订单
     */
    LeveragedOrder createStopLossOrder(Long userId, Long positionId, BigDecimal stopPrice);

    /**
     * 创建止盈单
     * @param userId 用户ID
     * @param positionId 仓位ID
     * @param takeProfitPrice 止盈价格
     * @return 订单
     */
    LeveragedOrder createTakeProfitOrder(Long userId, Long positionId, BigDecimal takeProfitPrice);

    /**
     * 创建止损限价单
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param side 方向
     * @param action 操作
     * @param quantity 数量
     * @param stopPrice 止损价格
     * @param limitPrice 限价
     * @param leverage 杠杆倍数
     * @return 订单
     */
    LeveragedOrder createStopLimitOrder(Long userId, String pairName, String side, String action,
                                       BigDecimal quantity, BigDecimal stopPrice, BigDecimal limitPrice, Integer leverage);

    /**
     * 创建条件单
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param side 方向
     * @param action 操作
     * @param quantity 数量
     * @param triggerPrice 触发价格
     * @param conditionType 条件类型
     * @param price 价格（可选）
     * @param leverage 杠杆倍数
     * @return 订单
     */
    LeveragedOrder createConditionalOrder(Long userId, String pairName, String side, String action,
                                         BigDecimal quantity, BigDecimal triggerPrice, String conditionType,
                                         BigDecimal price, Integer leverage);

    /**
     * 取消订单
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 查询用户订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<LeveragedOrder> getUserOrders(Long userId);

    /**
     * 查询订单历史
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<LeveragedOrder> getOrderHistory(Long userId, java.time.LocalDateTime startTime,
                                         java.time.LocalDateTime endTime);

    /**
     * 执行订单（成交）
     * @param orderId 订单ID
     * @return 持仓（如果是开仓订单）
     */
    LeveragedPosition executeOrder(Long orderId);

    /**
     * 检查并触发条件单
     */
    void checkAndTriggerConditionalOrders();
}















