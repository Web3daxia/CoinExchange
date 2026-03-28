/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.strategy;

import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.spot.dto.response.KlineDataResponse;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.spot.service.SpotOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反向策略
 * 价格下跌时买入，价格上涨时卖出，与趋势跟踪相反，适合震荡市场
 */
@Component
public class ReverseStrategy {

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private SpotOrderService spotOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行反向策略
     */
    public void execute(TradingRobot robot) throws Exception {
        // 读取策略参数
        Map<String, Object> params = parseStrategyParams(robot.getStrategyParams());
        
        Integer lookbackPeriod = params.containsKey("lookbackPeriod") ? 
                Integer.parseInt(params.get("lookbackPeriod").toString()) : 10; // 默认回看10个周期
        BigDecimal threshold = params.containsKey("threshold") ? 
                new BigDecimal(params.get("threshold").toString()) : new BigDecimal("0.02"); // 默认2%波动触发

        // 获取K线数据
        KlineDataResponse klineData = marketDataService.getKlineData(robot.getPairName(), "1h", lookbackPeriod + 5);
        List<KlineDataResponse.KlineItem> klines = klineData.getKlines();
        
        if (klines == null || klines.size() < lookbackPeriod) {
            throw new RuntimeException("K线数据不足，无法执行反向策略");
        }

        // 获取当前价格和之前的价格
        MarketDataResponse marketData = marketDataService.getMarketData(robot.getPairName());
        BigDecimal currentPrice = marketData.getCurrentPrice();
        
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 计算之前周期的平均价格
        BigDecimal avgPrice = calculateAveragePrice(klines, lookbackPeriod);
        
        // 计算价格变化百分比
        BigDecimal priceChange = currentPrice.subtract(avgPrice)
                .divide(avgPrice, 4, java.math.RoundingMode.HALF_UP);

        BigDecimal orderQuantity = robot.getOrderQuantity() != null ? 
                robot.getOrderQuantity() : calculateOrderQuantity(robot, currentPrice);

        // 反向策略：价格下跌时买入，价格上涨时卖出
        if (priceChange.compareTo(threshold.negate()) < 0) {
            // 价格下跌超过阈值，买入
            createReverseOrder(robot, "BUY", currentPrice, orderQuantity);
        } else if (priceChange.compareTo(threshold) > 0) {
            // 价格上涨超过阈值，卖出
            createReverseOrder(robot, "SELL", currentPrice, orderQuantity);
        }
        // 如果价格波动在阈值内，不操作
    }

    /**
     * 计算平均价格
     */
    private BigDecimal calculateAveragePrice(List<KlineDataResponse.KlineItem> klines, int period) {
        if (klines.size() < period) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = klines.size() - period - 1; i < klines.size() - 1; i++) {
            if (i >= 0) {
                sum = sum.add(klines.get(i).getClose());
            }
        }

        return sum.divide(new BigDecimal(period), 8, java.math.RoundingMode.HALF_UP);
    }

    /**
     * 计算订单数量
     */
    private BigDecimal calculateOrderQuantity(TradingRobot robot, BigDecimal price) {
        if (robot.getOrderAmount() != null) {
            return robot.getOrderAmount().divide(price, 8, java.math.RoundingMode.DOWN);
        }
        return robot.getOrderQuantity() != null ? robot.getOrderQuantity() : new BigDecimal("0.001");
    }

    /**
     * 创建反向订单
     */
    private void createReverseOrder(TradingRobot robot, String side, BigDecimal price, BigDecimal quantity) {
        try {
            CreateOrderRequest request = new CreateOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide(side);
            request.setPrice(price);
            request.setQuantity(quantity);
            request.setOrderType("LIMIT");

            spotOrderService.createOrder(robot.getUserId(), request);
        } catch (Exception e) {
            System.err.println("创建反向订单失败: " + e.getMessage());
            throw e;
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




