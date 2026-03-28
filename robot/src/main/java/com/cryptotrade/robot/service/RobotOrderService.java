/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import java.math.BigDecimal;

/**
 * 机器人订单服务接口
 * 用于机器人创建和管理订单（与订单服务集成）
 */
public interface RobotOrderService {
    /**
     * 创建现货订单
     */
    Long createSpotOrder(Long userId, String pairName, String side, String orderType, 
                        BigDecimal quantity, BigDecimal price);

    /**
     * 创建USDT本位合约订单
     */
    Long createFuturesUsdtOrder(Long userId, String pairName, String side, String orderType,
                                BigDecimal quantity, BigDecimal price, Integer leverage, String marginMode);

    /**
     * 创建币本位合约订单
     */
    Long createFuturesCoinOrder(Long userId, String pairName, String side, String orderType,
                                BigDecimal quantity, BigDecimal price, Integer leverage, String marginMode);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId, String marketType);

    /**
     * 查询订单状态
     */
    String getOrderStatus(Long userId, Long orderId, String marketType);
}













