/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.spot;

import com.cryptotrade.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 震荡区间突破策略（现货）
 * 
 * 策略逻辑：
 * - 在震荡区间内高抛低吸
 * - 当价格突破震荡区间时，跟随趋势
 */
@Component("OSCILLATION_RANGE_BREAKOUT_SPOT")
public class OscillationRangeBreakoutStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        BigDecimal upperBound = getBigDecimalParam(params, "upperBound", null);
        BigDecimal lowerBound = getBigDecimalParam(params, "lowerBound", null);
        Integer period = getIntParam(params, "period", 20); // 用于计算震荡区间的周期
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));
        BigDecimal breakoutThreshold = getBigDecimalParam(params, "breakoutThreshold", new BigDecimal("0.02")); // 突破阈值2%

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
        List<BigDecimal> closePrices = new ArrayList<>();

        for (Map<String, Object> kline : klines) {
            Object highObj = kline.get("high");
            Object lowObj = kline.get("low");
            Object closeObj = kline.get("close");

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

            if (closeObj instanceof BigDecimal) {
                closePrices.add((BigDecimal) closeObj);
            } else if (closeObj instanceof Number) {
                closePrices.add(new BigDecimal(closeObj.toString()));
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
            // 突破上边界，跟随趋势买入
            if (currentPrice.compareTo(upperBound.multiply(BigDecimal.ONE.add(breakoutThreshold))) > 0) {
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            }
            // 跌破下边界，跟随趋势卖出
            else if (currentPrice.compareTo(lowerBound.multiply(BigDecimal.ONE.subtract(breakoutThreshold))) < 0) {
                // TODO: 获取当前持仓数量
                BigDecimal quantity = BigDecimal.ZERO; // TODO: 从持仓获取
                
                if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        orderService.createSpotOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice);
                    } catch (Exception e) {
                        // 记录日志
                    }
                }
            }
            // 在区间内，高抛低吸（价格在上半区卖出，在下半区买入）
            else {
                BigDecimal quantity = BigDecimal.ZERO; // TODO: 从持仓获取
                
                if (pricePosition.compareTo(new BigDecimal("0.7")) > 0 && quantity.compareTo(BigDecimal.ZERO) > 0) {
                    // 价格在上半区，卖出
                    try {
                        orderService.createSpotOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice);
                    } catch (Exception e) {
                        // 记录日志
                    }
                } else if (pricePosition.compareTo(new BigDecimal("0.3")) < 0) {
                    // 价格在下半区，买入
                    BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                    quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                    
                    try {
                        orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                    } catch (Exception e) {
                        // 记录日志
                    }
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "OSCILLATION_RANGE_BREAKOUT";
    }

    @Override
    public String getStrategyName() {
        return "震荡区间突破策略";
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
