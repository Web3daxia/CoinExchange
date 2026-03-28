/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import com.cryptotrade.options.entity.OptionOrder;
import com.cryptotrade.options.entity.OptionPosition;

import java.util.List;

/**
 * 期权订单服务接口
 */
public interface OptionOrderService {
    /**
     * 创建期权订单（开仓或平仓）
     * @param userId 用户ID
     * @param contractId 期权合约ID
     * @param orderType 订单类型（OPEN开仓、CLOSE平仓）
     * @param side 方向（BUY买入、SELL卖出）
     * @param quantity 数量
     * @param price 价格（期权费）
     * @return 订单
     */
    OptionOrder createOrder(Long userId, Long contractId, String orderType, String side, 
                           java.math.BigDecimal quantity, java.math.BigDecimal price);

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
    List<OptionOrder> getUserOrders(Long userId);

    /**
     * 查询订单详情
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单
     */
    OptionOrder getOrder(Long userId, Long orderId);

    /**
     * 执行订单（成交）
     * @param orderId 订单ID
     * @return 持仓（如果是开仓订单）
     */
    OptionPosition executeOrder(Long orderId);

    /**
     * 查询订单历史
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<OptionOrder> getOrderHistory(Long userId, java.time.LocalDateTime startTime, 
                                     java.time.LocalDateTime endTime);
}















