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
 * 趋势跟踪策略
 * 根据价格趋势买入或卖出，上涨趋势买入，下跌趋势卖出
 */
@Component
public class TrendFollowingStrategy {

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private SpotOrderService spotOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行趋势跟踪策略
     */
    public void execute(TradingRobot robot) throws Exception {
        // 读取策略参数
        Map<String, Object> params = parseStrategyParams(robot.getStrategyParams());
        
        Integer maPeriod = params.containsKey("maPeriod") ? 
                Integer.parseInt(params.get("maPeriod").toString()) : 20; // 默认20周期移动平均线

        // 获取K线数据
        KlineDataResponse klineData = marketDataService.getKlineData(robot.getPairName(), "1h", maPeriod + 10);
        List<KlineDataResponse.KlineItem> klines = klineData.getKlines();
        
        if (klines == null || klines.size() < maPeriod) {
            throw new RuntimeException("K线数据不足，无法计算趋势");
        }

        // 计算移动平均线（MA）
        BigDecimal ma = calculateMovingAverage(klines, maPeriod);
        
        // 获取当前价格
        MarketDataResponse marketData = marketDataService.getMarketData(robot.getPairName());
        BigDecimal currentPrice = marketData.getCurrentPrice();
        
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 判断趋势
        // 如果当前价格 > MA，认为是上涨趋势，买入
        // 如果当前价格 < MA，认为是下跌趋势，卖出
        BigDecimal orderQuantity = robot.getOrderQuantity() != null ? 
                robot.getOrderQuantity() : calculateOrderQuantity(robot, currentPrice);

        if (currentPrice.compareTo(ma) > 0) {
            // 上涨趋势，买入
            createTrendOrder(robot, "BUY", currentPrice, orderQuantity);
        } else if (currentPrice.compareTo(ma) < 0) {
            // 下跌趋势，卖出
            createTrendOrder(robot, "SELL", currentPrice, orderQuantity);
        }
        // 如果价格接近MA，不操作
    }

    /**
     * 计算移动平均线
     */
    private BigDecimal calculateMovingAverage(List<KlineDataResponse.KlineItem> klines, int period) {
        if (klines.size() < period) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = klines.size() - period; i < klines.size(); i++) {
            sum = sum.add(klines.get(i).getClose());
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
     * 创建趋势订单
     */
    private void createTrendOrder(TradingRobot robot, String side, BigDecimal price, BigDecimal quantity) {
        try {
            CreateOrderRequest request = new CreateOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide(side);
            request.setPrice(price);
            request.setQuantity(quantity);
            request.setOrderType("LIMIT");

            spotOrderService.createOrder(robot.getUserId(), request);
        } catch (Exception e) {
            System.err.println("创建趋势订单失败: " + e.getMessage());
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




