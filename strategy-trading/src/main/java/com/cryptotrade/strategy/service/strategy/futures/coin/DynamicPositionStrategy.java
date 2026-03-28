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
 * 动态仓位策略（币本位合约）
 * 
 * 策略逻辑：
 * - 根据市场波动动态调整仓位比例
 * - 波动率低时加仓，波动率高时减仓
 */
@Component("DYNAMIC_POSITION_FUTURES_COIN")
public class DynamicPositionStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer volatilityPeriod = getIntParam(params, "volatilityPeriod", 24);
        BigDecimal basePositionRatio = getBigDecimalParam(params, "basePositionRatio", new BigDecimal("0.3"));
        BigDecimal maxPositionRatio = getBigDecimalParam(params, "maxPositionRatio", new BigDecimal("0.8"));
        BigDecimal minPositionRatio = getBigDecimalParam(params, "minPositionRatio", new BigDecimal("0.1"));
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", volatilityPeriod + 10);
        if (klines == null || klines.size() < volatilityPeriod + 1) {
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

        if (closePrices.size() < volatilityPeriod + 1) {
            return;
        }

        // 计算波动率
        BigDecimal volatility = TechnicalIndicatorUtil.calculateVolatility(closePrices, volatilityPeriod);
        if (volatility == null) {
            return;
        }

        // 根据波动率调整仓位比例（波动率越高，仓位越低）
        // 简化处理：假设正常波动率为5%，波动率每增加1%，仓位减少5%
        BigDecimal normalVolatility = new BigDecimal("5"); // 5%
        BigDecimal volatilityDiff = volatility.subtract(normalVolatility);
        BigDecimal positionAdjustment = volatilityDiff.multiply(new BigDecimal("0.05")); // 每1%波动率调整5%仓位
        BigDecimal positionRatio = basePositionRatio.subtract(positionAdjustment);

        // 限制仓位比例在最小和最大值之间
        if (positionRatio.compareTo(maxPositionRatio) > 0) {
            positionRatio = maxPositionRatio;
        }
        if (positionRatio.compareTo(minPositionRatio) < 0) {
            positionRatio = minPositionRatio;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 计算移动平均线判断趋势
        BigDecimal ma = TechnicalIndicatorUtil.calculateSMA(closePrices, 20);
        if (ma == null) {
            return;
        }

        boolean uptrend = currentPrice.compareTo(ma) > 0;

        // 执行交易
        if (orderService != null) {
            BigDecimal baseAmount = new BigDecimal("100"); // TODO: 从账户余额计算
            BigDecimal orderAmount = baseAmount.multiply(positionRatio);
            BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);

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
        return "DYNAMIC_POSITION";
    }

    @Override
    public String getStrategyName() {
        return "动态仓位策略";
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
