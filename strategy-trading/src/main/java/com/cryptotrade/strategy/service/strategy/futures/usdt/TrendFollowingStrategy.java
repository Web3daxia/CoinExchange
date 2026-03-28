/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.futures.usdt;

import com.cryptotrade.strategy.service.StrategyService;
import com.cryptotrade.common.technical.TechnicalIndicatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 趋势跟随策略（USDT本位合约）
 * 
 * 策略逻辑：
 * - 上升趋势时做多
 * - 下降趋势时做空
 */
@Component("TREND_FOLLOWING_FUTURES_USDT")
public class TrendFollowingStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer shortMaPeriod = getIntParam(params, "shortMaPeriod", 5);
        Integer longMaPeriod = getIntParam(params, "longMaPeriod", 20);
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", longMaPeriod + 10);
        if (klines == null || klines.size() < longMaPeriod + 1) {
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

        if (closePrices.size() < longMaPeriod + 1) {
            return;
        }

        // 计算均线
        BigDecimal shortMA = TechnicalIndicatorUtil.calculateSMA(closePrices, shortMaPeriod);
        BigDecimal longMA = TechnicalIndicatorUtil.calculateSMA(closePrices, longMaPeriod);

        if (shortMA == null || longMA == null) {
            return;
        }

        // 获取前一个周期的均线值
        List<BigDecimal> prevClosePrices = closePrices.subList(0, closePrices.size() - 1);
        BigDecimal prevShortMA = TechnicalIndicatorUtil.calculateSMA(prevClosePrices, shortMaPeriod);
        BigDecimal prevLongMA = TechnicalIndicatorUtil.calculateSMA(prevClosePrices, longMaPeriod);

        if (prevShortMA == null || prevLongMA == null) {
            return;
        }

        // 判断趋势
        boolean uptrend = shortMA.compareTo(longMA) > 0 && prevShortMA.compareTo(prevLongMA) <= 0; // 金叉
        boolean downtrend = shortMA.compareTo(longMA) < 0 && prevShortMA.compareTo(prevLongMA) >= 0; // 死叉

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 执行交易
        if (orderService != null) {
            if (uptrend) {
                // 上升趋势，做多
                BigDecimal orderAmount = new BigDecimal("100"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createFuturesUsdtOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (downtrend) {
                // 下降趋势，做空
                BigDecimal orderAmount = new BigDecimal("100"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createFuturesUsdtOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "TREND_FOLLOWING";
    }

    @Override
    public String getStrategyName() {
        return "趋势跟随策略";
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
