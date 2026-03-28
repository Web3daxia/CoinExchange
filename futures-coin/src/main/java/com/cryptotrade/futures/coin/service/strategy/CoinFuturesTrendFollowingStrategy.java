/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.strategy;

import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesMarketDataResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot;
import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataService;
import com.cryptotrade.futures.coin.service.CoinFuturesOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币本位永续合约趋势跟踪策略
 * 根据价格趋势买入或卖出，上涨趋势买入，下跌趋势卖出
 */
@Component
public class CoinFuturesTrendFollowingStrategy {

    @Autowired
    private CoinFuturesMarketDataService coinFuturesMarketDataService;

    @Autowired
    private CoinFuturesOrderService coinFuturesOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行趋势跟踪策略
     */
    public void execute(CoinFuturesTradingRobot robot) throws Exception {
        // 读取策略参数
        Map<String, Object> params = parseStrategyParams(robot.getStrategyParams());
        
        Integer maPeriod = params.containsKey("maPeriod") ? 
                Integer.parseInt(params.get("maPeriod").toString()) : 20; // 默认20周期移动平均线

        // 获取K线数据
        List<Map<String, Object>> klines = coinFuturesMarketDataService.getKlineData(robot.getPairName(), "1h", maPeriod + 10);
        
        if (klines == null || klines.size() < maPeriod) {
            throw new RuntimeException("K线数据不足，无法计算趋势");
        }

        // 计算移动平均线（MA）
        BigDecimal ma = calculateMovingAverage(klines, maPeriod);
        
        // 获取当前价格
        CoinFuturesMarketDataResponse marketData = coinFuturesMarketDataService.getMarketData(robot.getPairName());
        BigDecimal currentPrice = marketData.getCurrentPrice();
        
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 判断趋势
        // 如果当前价格 > MA，认为是上涨趋势，买入开多
        // 如果当前价格 < MA，认为是下跌趋势，卖出开空
        BigDecimal orderQuantity = robot.getOrderQuantity() != null ? 
                robot.getOrderQuantity() : new BigDecimal("0.001");

        if (currentPrice.compareTo(ma) > 0) {
            // 上涨趋势：买入开多
            CreateCoinFuturesOrderRequest request = new CreateCoinFuturesOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide("BUY");
            request.setPositionSide("LONG");
            request.setMarginMode(robot.getMarginMode());
            request.setLeverage(robot.getLeverage());
            request.setQuantity(orderQuantity);
            request.setOrderType("MARKET");
            coinFuturesOrderService.createOrder(robot.getUserId(), request);
        } else if (currentPrice.compareTo(ma) < 0) {
            // 下跌趋势：卖出开空
            CreateCoinFuturesOrderRequest request = new CreateCoinFuturesOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide("SELL");
            request.setPositionSide("SHORT");
            request.setMarginMode(robot.getMarginMode());
            request.setLeverage(robot.getLeverage());
            request.setQuantity(orderQuantity);
            request.setOrderType("MARKET");
            coinFuturesOrderService.createOrder(robot.getUserId(), request);
        }
    }

    /**
     * 计算移动平均线
     */
    private BigDecimal calculateMovingAverage(List<Map<String, Object>> klines, int period) {
        if (klines.size() < period) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = klines.size() - period; i < klines.size(); i++) {
            Map<String, Object> kline = klines.get(i);
            BigDecimal close = (BigDecimal) kline.get("close");
            sum = sum.add(close);
        }

        return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
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















