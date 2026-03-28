/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.strategy;

import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.spot.service.SpotOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 网格交易策略
 * 在价格区间内设置多个买入和卖出订单，价格下跌时买入，价格上涨时卖出
 */
@Component
public class GridStrategy {

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private SpotOrderService spotOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行网格交易策略
     */
    public void execute(TradingRobot robot) throws Exception {
        // 读取策略参数
        Map<String, Object> params = parseStrategyParams(robot.getStrategyParams());
        
        BigDecimal gridLowerPrice = params.containsKey("gridLowerPrice") ? 
                new BigDecimal(params.get("gridLowerPrice").toString()) : null;
        BigDecimal gridUpperPrice = params.containsKey("gridUpperPrice") ? 
                new BigDecimal(params.get("gridUpperPrice").toString()) : null;
        Integer gridCount = params.containsKey("gridCount") ? 
                Integer.parseInt(params.get("gridCount").toString()) : 10;
        BigDecimal gridSpacing = params.containsKey("gridSpacing") ? 
                new BigDecimal(params.get("gridSpacing").toString()) : null;

        // 获取当前市场价格
        MarketDataResponse marketData = marketDataService.getMarketData(robot.getPairName());
        BigDecimal currentPrice = marketData.getCurrentPrice();
        
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 如果未设置价格区间，根据当前价格自动计算
        if (gridLowerPrice == null || gridUpperPrice == null) {
            BigDecimal priceRange = currentPrice.multiply(new BigDecimal("0.2")); // 上下20%
            gridLowerPrice = currentPrice.subtract(priceRange);
            gridUpperPrice = currentPrice.add(priceRange);
        }

        // 如果未设置网格间距，根据网格数量计算
        if (gridSpacing == null) {
            gridSpacing = gridUpperPrice.subtract(gridLowerPrice)
                    .divide(new BigDecimal(gridCount), 8, RoundingMode.HALF_UP);
        }

        // 计算每个网格的价格
        BigDecimal orderAmount = robot.getOrderAmount() != null ? 
                robot.getOrderAmount() : new BigDecimal("100"); // 默认每个订单100 USDT
        BigDecimal orderQuantity = orderAmount.divide(currentPrice, 8, RoundingMode.DOWN);

        // 创建买入网格订单（在价格下方）
        BigDecimal buyPrice = currentPrice.subtract(gridSpacing);
        while (buyPrice.compareTo(gridLowerPrice) >= 0) {
            createGridOrder(robot, "BUY", buyPrice, orderQuantity);
            buyPrice = buyPrice.subtract(gridSpacing);
        }

        // 创建卖出网格订单（在价格上方）
        BigDecimal sellPrice = currentPrice.add(gridSpacing);
        while (sellPrice.compareTo(gridUpperPrice) <= 0) {
            createGridOrder(robot, "SELL", sellPrice, orderQuantity);
            sellPrice = sellPrice.add(gridSpacing);
        }
    }

    /**
     * 创建网格订单
     */
    private void createGridOrder(TradingRobot robot, String side, BigDecimal price, BigDecimal quantity) {
        try {
            CreateOrderRequest request = new CreateOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide(side);
            request.setPrice(price);
            request.setQuantity(quantity);
            request.setOrderType("LIMIT");

            spotOrderService.createOrder(robot.getUserId(), request);
        } catch (Exception e) {
            // 记录错误，继续创建其他订单
            System.err.println("创建网格订单失败: " + e.getMessage());
        }
    }

    /**
     * 解析策略参数
     */
    private Map<String, Object> parseStrategyParams(String strategyParams) {
        try {
            if (strategyParams == null || strategyParams.isEmpty()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(strategyParams, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}




