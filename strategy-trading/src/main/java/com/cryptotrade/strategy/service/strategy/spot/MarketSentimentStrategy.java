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
 * 市场情绪策略（现货）
 * 
 * 策略逻辑：
 * - 基于市场情绪指标进行交易
 * - 使用多个技术指标综合判断市场情绪（RSI、MACD、成交量等）
 */
@Component("MARKET_SENTIMENT_SPOT")
public class MarketSentimentStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer rsiPeriod = getIntParam(params, "rsiPeriod", 14);
        Integer maPeriod = getIntParam(params, "maPeriod", 20);
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));
        BigDecimal bullishThreshold = getBigDecimalParam(params, "bullishThreshold", new BigDecimal("60")); // 看涨阈值
        BigDecimal bearishThreshold = getBigDecimalParam(params, "bearishThreshold", new BigDecimal("40")); // 看跌阈值

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", maPeriod + 20);
        if (klines == null || klines.size() < maPeriod + 10) {
            return;
        }

        // 提取收盘价和成交量
        List<BigDecimal> closePrices = new ArrayList<>();
        List<BigDecimal> volumes = new ArrayList<>();

        for (Map<String, Object> kline : klines) {
            Object closeObj = kline.get("close");
            Object volumeObj = kline.get("volume");

            if (closeObj instanceof BigDecimal) {
                closePrices.add((BigDecimal) closeObj);
            } else if (closeObj instanceof Number) {
                closePrices.add(new BigDecimal(closeObj.toString()));
            }

            if (volumeObj instanceof BigDecimal) {
                volumes.add((BigDecimal) volumeObj);
            } else if (volumeObj instanceof Number) {
                volumes.add(new BigDecimal(volumeObj.toString()));
            }
        }

        if (closePrices.size() < maPeriod + 10) {
            return;
        }

        // 计算技术指标
        BigDecimal rsi = TechnicalIndicatorUtil.calculateRSI(closePrices, rsiPeriod);
        BigDecimal ma = TechnicalIndicatorUtil.calculateSMA(closePrices, maPeriod);

        if (rsi == null || ma == null) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 计算成交量变化（简化处理：使用最近成交量与平均成交量比较）
        BigDecimal avgVolume = BigDecimal.ZERO;
        if (!volumes.isEmpty()) {
            BigDecimal sum = volumes.stream()
                    .skip(Math.max(0, volumes.size() - maPeriod))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgVolume = sum.divide(new BigDecimal(Math.min(volumes.size(), maPeriod)), 8, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal recentVolume = volumes.isEmpty() ? BigDecimal.ZERO : volumes.get(volumes.size() - 1);
        BigDecimal volumeRatio = avgVolume.compareTo(BigDecimal.ZERO) > 0 ?
                recentVolume.divide(avgVolume, 8, BigDecimal.ROUND_HALF_UP) : BigDecimal.ONE;

        // 综合判断市场情绪
        // 看涨信号：RSI > 50，价格 > MA，成交量增加
        // 看跌信号：RSI < 50，价格 < MA，成交量增加
        boolean bullish = rsi.compareTo(bullishThreshold) > 0 && 
                          currentPrice.compareTo(ma) > 0 && 
                          volumeRatio.compareTo(BigDecimal.ONE) > 0;

        boolean bearish = rsi.compareTo(bearishThreshold) < 0 && 
                          currentPrice.compareTo(ma) < 0 && 
                          volumeRatio.compareTo(BigDecimal.ONE) > 0;

        // 执行交易
        if (orderService != null) {
            if (bullish) {
                // 看涨信号，买入
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (bearish) {
                // 看跌信号，卖出
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
        }
    }

    @Override
    public String getStrategyType() {
        return "MARKET_SENTIMENT";
    }

    @Override
    public String getStrategyName() {
        return "市场情绪策略";
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
