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
import java.util.HashMap;
import java.util.Map;

/**
 * 币本位永续合约反向策略
 * 与趋势相反，价格下跌时买入，价格上涨时卖出
 */
@Component
public class CoinFuturesReverseStrategy {

    @Autowired
    private CoinFuturesMarketDataService coinFuturesMarketDataService;

    @Autowired
    private CoinFuturesOrderService coinFuturesOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行反向策略
     */
    public void execute(CoinFuturesTradingRobot robot) throws Exception {
        // 读取策略参数
        Map<String, Object> params = parseStrategyParams(robot.getStrategyParams());
        
        BigDecimal priceChangeThreshold = params.containsKey("priceChangeThreshold") ? 
                new BigDecimal(params.get("priceChangeThreshold").toString()) : new BigDecimal("0.02"); // 默认2%

        // 获取当前价格和历史价格
        CoinFuturesMarketDataResponse marketData = coinFuturesMarketDataService.getMarketData(robot.getPairName());
        BigDecimal currentPrice = marketData.getCurrentPrice();
        
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 获取K线数据计算价格变化
        // 简化处理：使用24小时涨跌幅
        BigDecimal priceChange24h = marketData.getPriceChange24h();
        if (priceChange24h == null) {
            priceChange24h = BigDecimal.ZERO;
        }

        BigDecimal orderQuantity = robot.getOrderQuantity() != null ? 
                robot.getOrderQuantity() : new BigDecimal("0.001");

        // 如果价格下跌超过阈值，买入（反向操作）
        if (priceChange24h.compareTo(priceChangeThreshold.negate()) < 0) {
            CreateCoinFuturesOrderRequest request = new CreateCoinFuturesOrderRequest();
            request.setPairName(robot.getPairName());
            request.setSide("BUY");
            request.setPositionSide("LONG");
            request.setMarginMode(robot.getMarginMode());
            request.setLeverage(robot.getLeverage());
            request.setQuantity(orderQuantity);
            request.setOrderType("MARKET");
            coinFuturesOrderService.createOrder(robot.getUserId(), request);
        } 
        // 如果价格上涨超过阈值，卖出（反向操作）
        else if (priceChange24h.compareTo(priceChangeThreshold) > 0) {
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















