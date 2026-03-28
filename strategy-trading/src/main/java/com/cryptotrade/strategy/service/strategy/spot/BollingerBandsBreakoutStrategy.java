/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.spot;

import com.cryptotrade.strategy.service.StrategyService;
import com.cryptotrade.common.technical.TechnicalIndicatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 布林带突破策略（现货）
 * 
 * 策略逻辑：
 * - 当价格突破布林带上轨时，卖出
 * - 当价格跌破布林带下轨时，买入
 */
@Component("BOLLINGER_BANDS_BREAKOUT_SPOT")
public class BollingerBandsBreakoutStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer period = getIntParam(params, "period", 20);
        BigDecimal stdDev = getBigDecimalParam(params, "stdDev", new BigDecimal("2"));
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", period + 10);
        if (klines == null || klines.size() < period) {
            return;
        }

        // 提取收盘价
        List<BigDecimal> closePrices = new ArrayList<>();
        for (Map<String, Object> kline : klines) {
            Object closeObj = kline.get("close");
            if (closeObj instanceof BigDecimal) {
                closePrices.add((BigDecimal) closeObj);
            } else if (closeObj instanceof Number) {
                closePrices.add(new BigDecimal(closeObj.toString()));
            }
        }

        if (closePrices.size() < period) {
            return;
        }

        // 计算布林带
        TechnicalIndicatorUtil.BollingerBandsResult bb = TechnicalIndicatorUtil.calculateBollingerBands(closePrices, period, stdDev);
        if (bb == null) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);

        // 判断突破信号
        if (orderService != null) {
            if (currentPrice.compareTo(bb.getUpperBand()) > 0) {
                // 突破上轨，卖出信号
                // TODO: 获取当前持仓数量
                BigDecimal quantity = BigDecimal.ZERO; // TODO: 从持仓获取
                
                if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        orderService.createSpotOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice);
                    } catch (Exception e) {
                        // 记录日志
                    }
                }
            } else if (currentPrice.compareTo(bb.getLowerBand()) < 0) {
                // 跌破下轨，买入信号
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "BOLLINGER_BANDS_BREAKOUT";
    }

    @Override
    public String getStrategyName() {
        return "布林带突破策略";
    }

    private Integer getIntParam(Map<String, Object> params, String key, Integer defaultValue) {
        Object value = params.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return defaultValue;
    }

    private BigDecimal getBigDecimalParam(Map<String, Object> params, String key, BigDecimal defaultValue) {
        Object value = params.get(key);
        if (value == null) return defaultValue;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return new BigDecimal(value.toString());
        if (value instanceof String) return new BigDecimal((String) value);
        return defaultValue;
    }
}
