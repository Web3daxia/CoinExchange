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
 * RSI超买超卖策略（现货）
 * 
 * 策略逻辑：
 * - 当RSI < 30时（超卖），买入
 * - 当RSI > 70时（超买），卖出
 */
@Component("RSI_OVERBOUGHT_OVERSOLD_SPOT")
public class RSIOversoldOverboughtStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer rsiPeriod = getIntParam(params, "rsiPeriod", 14);
        BigDecimal oversoldThreshold = getBigDecimalParam(params, "oversoldThreshold", new BigDecimal("30"));
        BigDecimal overboughtThreshold = getBigDecimalParam(params, "overboughtThreshold", new BigDecimal("70"));
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", rsiPeriod + 10);
        if (klines == null || klines.size() < rsiPeriod + 1) {
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

        if (closePrices.size() < rsiPeriod + 1) {
            return;
        }

        // 计算RSI
        BigDecimal rsi = TechnicalIndicatorUtil.calculateRSI(closePrices, rsiPeriod);
        if (rsi == null) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);

        // 判断信号
        if (orderService != null) {
            if (rsi.compareTo(oversoldThreshold) < 0) {
                // 超卖，买入信号
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (rsi.compareTo(overboughtThreshold) > 0) {
                // 超买，卖出信号
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
        return "RSI_OVERBOUGHT_OVERSOLD";
    }

    @Override
    public String getStrategyName() {
        return "RSI超买超卖策略";
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
