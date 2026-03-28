/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.service.RobotOrderService;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.spot.entity.SpotOrder;
import com.cryptotrade.spot.service.SpotOrderService;
import com.cryptotrade.futures.usdt.entity.FuturesOrder;
import com.cryptotrade.futures.usdt.service.FuturesOrderService;
import com.cryptotrade.futures.usdt.dto.request.CreateFuturesOrderRequest;
import com.cryptotrade.futures.coin.entity.CoinFuturesOrder;
import com.cryptotrade.futures.coin.service.CoinFuturesOrderService;
import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.spot.dto.response.OrderResponse;
import com.cryptotrade.futures.usdt.dto.response.FuturesOrderResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 机器人订单服务实现类
 */
@Service
public class RobotOrderServiceImpl implements RobotOrderService {

    @Autowired(required = false)
    private SpotOrderService spotOrderService;

    @Autowired(required = false)
    private FuturesOrderService futuresUsdtOrderService;

    @Autowired(required = false)
    private CoinFuturesOrderService futuresCoinOrderService;

    @Override
    public Long createSpotOrder(Long userId, String pairName, String side, String orderType,
                               BigDecimal quantity, BigDecimal price) {
        if (spotOrderService == null) {
            throw new RuntimeException("现货订单服务未配置");
        }

        try {
            CreateOrderRequest request = new CreateOrderRequest();
            request.setPairName(pairName);
            request.setSide(side);
            request.setOrderType(orderType);
            request.setQuantity(quantity);
            if ("LIMIT".equals(orderType)) {
                request.setPrice(price);
            }

            SpotOrder order = spotOrderService.createOrder(userId, request);
            return order.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建现货订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Long createFuturesUsdtOrder(Long userId, String pairName, String side, String orderType,
                                       BigDecimal quantity, BigDecimal price, Integer leverage, String marginMode) {
        if (futuresUsdtOrderService == null) {
            throw new RuntimeException("USDT本位合约订单服务未配置");
        }

        try {
            CreateFuturesOrderRequest request = new CreateFuturesOrderRequest();
            request.setPairName(pairName);
            request.setSide(side);
            request.setOrderType(orderType);
            request.setQuantity(quantity);
            request.setLeverage(leverage);
            request.setMarginMode(marginMode);
            request.setPositionSide("LONG"); // 默认做多，如果需要做空，需要额外参数
            if ("LIMIT".equals(orderType)) {
                request.setPrice(price);
            }

            FuturesOrder order = futuresUsdtOrderService.createOrder(userId, request);
            return order.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建USDT本位合约订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Long createFuturesCoinOrder(Long userId, String pairName, String side, String orderType,
                                      BigDecimal quantity, BigDecimal price, Integer leverage, String marginMode) {
        if (futuresCoinOrderService == null) {
            throw new RuntimeException("币本位合约订单服务未配置");
        }

        try {
            CreateCoinFuturesOrderRequest request = new CreateCoinFuturesOrderRequest();
            request.setPairName(pairName);
            request.setSide(side);
            request.setOrderType(orderType);
            request.setQuantity(quantity);
            request.setLeverage(leverage);
            request.setMarginMode(marginMode);
            request.setPositionSide("LONG"); // 默认做多，如果需要做空，需要额外参数
            if ("LIMIT".equals(orderType)) {
                request.setPrice(price);
            }

            CoinFuturesOrder order = futuresCoinOrderService.createOrder(userId, request);
            return order.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建币本位合约订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelOrder(Long userId, Long orderId, String marketType) {
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotOrderService != null) {
                        spotOrderService.cancelOrder(userId, orderId);
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtOrderService != null) {
                        futuresUsdtOrderService.cancelOrder(userId, orderId);
                    } else {
                        throw new RuntimeException("USDT本位合约订单服务未配置");
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinOrderService != null) {
                        futuresCoinOrderService.cancelOrder(userId, orderId);
                    } else {
                        throw new RuntimeException("币本位合约订单服务未配置");
                    }
                    break;
                default:
                    throw new RuntimeException("不支持的市场类型: " + marketType);
            }
        } catch (Exception e) {
            throw new RuntimeException("取消订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getOrderStatus(Long userId, Long orderId, String marketType) {
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotOrderService != null) {
                        OrderResponse orderResponse = spotOrderService.getOrderStatus(userId, orderId);
                        return orderResponse.getStatus();
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtOrderService != null) {
                        FuturesOrderResponse orderResponse = futuresUsdtOrderService.getOrderStatus(userId, orderId);
                        return orderResponse.getStatus();
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinOrderService != null) {
                        CoinFuturesOrderResponse orderResponse = futuresCoinOrderService.getOrderStatus(userId, orderId);
                        return orderResponse.getStatus();
                    }
                    break;
            }
        } catch (Exception e) {
            // 记录日志
        }
        return "UNKNOWN";
    }
}

