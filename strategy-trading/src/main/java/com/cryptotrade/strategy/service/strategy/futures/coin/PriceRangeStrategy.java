/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.futures.coin;

import com.cryptotrade.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 价格区间策略（币本位合约）
 * 
 * 策略逻辑：
 * - 当价格处于特定区间内时，设置买入卖出
 * - 价格突破区间时平仓
 */
@Component("PRICE_RANGE_FUTURES_COIN")
public class PriceRangeStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        BigDecimal upperBound = getBigDecimalParam(params, "upperBound", null);
        BigDecimal lowerBound = getBigDecimalParam(params, "lowerBound", null);
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));
        Integer period = getIntParam(params, "period", 20); // 用于计算区间的周期

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", period + 10);
        if (klines == null || klines.size() < period) {
            return;
        }

        // 提取价格数据
        List<BigDecimal> highPrices = new ArrayList<>();
        List<BigDecimal> lowPrices = new ArrayList<>();

        for (Map<String, Object> kline : klines) {
            Object highObj = kline.get("high");
            Object lowObj = kline.get("low");

            if (highObj instanceof BigDecimal) {
                highPrices.add((BigDecimal) highObj);
            } else if (highObj instanceof Number) {
                highPrices.add(new BigDecimal(highObj.toString()));
            }

            if (lowObj instanceof BigDecimal) {
                lowPrices.add((BigDecimal) lowObj);
            } else if (lowObj instanceof Number) {
                lowPrices.add(new BigDecimal(lowObj.toString()));
            }
        }

        if (highPrices.size() < period || lowPrices.size() < period) {
            return;
        }

        // 如果未指定区间，则计算最近period周期的最高价和最低价
        if (upperBound == null || lowerBound == null) {
            BigDecimal maxHigh = highPrices.stream()
                    .skip(Math.max(0, highPrices.size() - period))
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal minLow = lowPrices.stream()
                    .skip(Math.max(0, lowPrices.size() - period))
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            if (maxHigh.compareTo(BigDecimal.ZERO) == 0 || minLow.compareTo(BigDecimal.ZERO) == 0) {
                return;
            }

            upperBound = maxHigh;
            lowerBound = minLow;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 执行交易
        if (orderService != null) {
            BigDecimal baseQuantity = new BigDecimal("0.01"); // 基础数量

            // 价格在区间内
            if (currentPrice.compareTo(lowerBound) > 0 && currentPrice.compareTo(upperBound) < 0) {
                BigDecimal range = upperBound.subtract(lowerBound);
                BigDecimal pricePosition = currentPrice.subtract(lowerBound).divide(range, 8, BigDecimal.ROUND_HALF_UP);

                // 价格在上半区，做空
                if (pricePosition.compareTo(new BigDecimal("0.6")) > 0) {
                    try {
                        orderService.createFuturesCoinOrder(userId, pairName, "SELL", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                    } catch (Exception e) {
                        // 记录日志
                    }
                }
                // 价格在下半区，做多
                else if (pricePosition.compareTo(new BigDecimal("0.4")) < 0) {
                    try {
                        orderService.createFuturesCoinOrder(userId, pairName, "BUY", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                    } catch (Exception e) {
                        // 记录日志
                    }
                }
            }
            // 价格突破上边界，做空
            else if (currentPrice.compareTo(upperBound) > 0) {
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "SELL", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
            // 价格跌破下边界，做多
            else if (currentPrice.compareTo(lowerBound) < 0) {
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "BUY", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "PRICE_RANGE";
    }

    @Override
    public String getStrategyName() {
        return "价格区间策略";
    }

    private Integer getIntParam(Map<String, Object> params, String key, Integer defaultValue) {
        Object value = params.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return defaultValue;
    }

    private String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        Object value = params.get(key);
        if (value == null) return defaultValue;
        return value.toString();
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
