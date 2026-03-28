/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.futures.usdt;

import com.cryptotrade.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 震荡区间策略（USDT本位合约）
 * 
 * 策略逻辑：
 * - 在震荡市场中，价格达到区间上限时做空
 * - 价格达到区间下限时做多
 */
@Component("OSCILLATION_RANGE_FUTURES_USDT")
public class OscillationRangeStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        BigDecimal upperBound = getBigDecimalParam(params, "upperBound", null);
        BigDecimal lowerBound = getBigDecimalParam(params, "lowerBound", null);
        Integer period = getIntParam(params, "period", 20);
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));

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

        // 判断价格位置
        BigDecimal range = upperBound.subtract(lowerBound);
        if (range.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal pricePosition = currentPrice.subtract(lowerBound).divide(range, 8, BigDecimal.ROUND_HALF_UP);

        // 执行交易
        if (orderService != null) {
            BigDecimal orderAmount = new BigDecimal("100"); // TODO: 从账户余额和investmentRatio计算

            // 价格接近上限，做空
            if (pricePosition.compareTo(new BigDecimal("0.8")) > 0) {
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createFuturesUsdtOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
            // 价格接近下限，做多
            else if (pricePosition.compareTo(new BigDecimal("0.2")) < 0) {
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createFuturesUsdtOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "OSCILLATION_RANGE";
    }

    @Override
    public String getStrategyName() {
        return "震荡区间策略";
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
