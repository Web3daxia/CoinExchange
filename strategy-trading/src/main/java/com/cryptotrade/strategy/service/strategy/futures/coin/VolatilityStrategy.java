/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.futures.coin;

import com.cryptotrade.strategy.service.StrategyService;
import com.cryptotrade.common.technical.TechnicalIndicatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 波动率策略（币本位合约）
 * 
 * 策略逻辑：
 * - 根据市场波动率进行交易
 * - 适应高波动市场环境
 */
@Component("VOLATILITY_FUTURES_COIN")
public class VolatilityStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer period = getIntParam(params, "period", 20);
        BigDecimal highVolatilityThreshold = getBigDecimalParam(params, "highVolatilityThreshold", new BigDecimal("5")); // 5%
        BigDecimal lowVolatilityThreshold = getBigDecimalParam(params, "lowVolatilityThreshold", new BigDecimal("1")); // 1%
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", period + 10);
        if (klines == null || klines.size() < period + 1) {
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

        if (closePrices.size() < period + 1) {
            return;
        }

        // 计算波动率
        BigDecimal volatility = TechnicalIndicatorUtil.calculateVolatility(closePrices, period);
        if (volatility == null) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 计算移动平均线判断趋势
        BigDecimal ma = TechnicalIndicatorUtil.calculateSMA(closePrices, period);
        if (ma == null) {
            return;
        }

        boolean uptrend = currentPrice.compareTo(ma) > 0;

        // 执行交易
        if (orderService != null) {
            BigDecimal baseQuantity = new BigDecimal("0.01"); // 基础数量

            // 高波动率时增加仓位，低波动率时减少仓位
            BigDecimal quantityMultiplier = BigDecimal.ONE;
            if (volatility.compareTo(highVolatilityThreshold) > 0) {
                quantityMultiplier = new BigDecimal("1.5"); // 高波动率时增加50%仓位
            } else if (volatility.compareTo(lowVolatilityThreshold) < 0) {
                quantityMultiplier = new BigDecimal("0.5"); // 低波动率时减少50%仓位
            }

            BigDecimal quantity = baseQuantity.multiply(quantityMultiplier);

            if (uptrend) {
                // 上升趋势，做多
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            } else {
                // 下降趋势，做空
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "SELL", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "VOLATILITY";
    }

    @Override
    public String getStrategyName() {
        return "波动率策略";
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
