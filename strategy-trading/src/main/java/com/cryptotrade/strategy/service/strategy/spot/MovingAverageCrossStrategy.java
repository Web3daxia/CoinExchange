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
 * 均线交叉策略（现货）
 * 
 * 策略逻辑：
 * - 当短期均线上穿长期均线时，买入
 * - 当短期均线下穿长期均线时，卖出
 */
@Component("MA_CROSS_SPOT")
public class MovingAverageCrossStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        // 获取参数
        Integer shortPeriod = getIntParam(params, "shortPeriod", 5);
        Integer longPeriod = getIntParam(params, "longPeriod", 50);
        String maType = getStringParam(params, "maType", "SMA");
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));
        BigDecimal stopLossPercentage = getBigDecimalParam(params, "stopLossPercentage", null);
        BigDecimal takeProfitPercentage = getBigDecimalParam(params, "takeProfitPercentage", null);

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", longPeriod + 10);
        if (klines == null || klines.size() < longPeriod + 1) {
            return; // 数据不足，无法计算
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

        if (closePrices.size() < longPeriod + 1) {
            return;
        }

        // 计算均线
        BigDecimal shortMA = "EMA".equals(maType) 
            ? TechnicalIndicatorUtil.calculateEMA(closePrices, shortPeriod)
            : TechnicalIndicatorUtil.calculateSMA(closePrices, shortPeriod);
        
        BigDecimal longMA = "EMA".equals(maType)
            ? TechnicalIndicatorUtil.calculateEMA(closePrices, longPeriod)
            : TechnicalIndicatorUtil.calculateSMA(closePrices, longPeriod);

        if (shortMA == null || longMA == null) {
            return;
        }

        // 获取前一个周期的均线值（用于判断交叉）
        List<BigDecimal> prevClosePrices = closePrices.subList(0, closePrices.size() - 1);
        BigDecimal prevShortMA = "EMA".equals(maType)
            ? TechnicalIndicatorUtil.calculateEMA(prevClosePrices, shortPeriod)
            : TechnicalIndicatorUtil.calculateSMA(prevClosePrices, shortPeriod);
        
        BigDecimal prevLongMA = "EMA".equals(maType)
            ? TechnicalIndicatorUtil.calculateEMA(prevClosePrices, longPeriod)
            : TechnicalIndicatorUtil.calculateSMA(prevClosePrices, longPeriod);

        if (prevShortMA == null || prevLongMA == null) {
            return;
        }

        // 检测交叉信号
        boolean goldenCross = prevShortMA.compareTo(prevLongMA) <= 0 && shortMA.compareTo(longMA) > 0; // 金叉
        boolean deathCross = prevShortMA.compareTo(prevLongMA) >= 0 && shortMA.compareTo(longMA) < 0; // 死叉

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);

        // 执行交易
        if (orderService != null) {
            if (goldenCross) {
                // 买入信号
                // 这里需要获取账户余额来计算买入数量
                // 简化处理：使用固定金额
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (deathCross) {
                // 卖出信号
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

        // TODO: 检查止损止盈
        // checkStopLossTakeProfit(userId, pairName, stopLossPercentage, takeProfitPercentage);
    }

    @Override
    public String getStrategyType() {
        return "MA_CROSS";
    }

    @Override
    public String getStrategyName() {
        return "均线交叉策略";
    }

    // 辅助方法
    private Integer getIntParam(Map<String, Object> params, String key, Integer defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    private String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    private BigDecimal getBigDecimalParam(Map<String, Object> params, String key, BigDecimal defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        return defaultValue;
    }
}
