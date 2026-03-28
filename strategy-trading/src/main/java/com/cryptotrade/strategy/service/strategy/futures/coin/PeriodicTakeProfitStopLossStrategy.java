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
 * 定期止盈止损策略（币本位合约）
 * 
 * 策略逻辑：
 * - 定期设置固定的止盈止损
 * - 根据技术指标判断趋势方向
 */
@Component("PERIODIC_TAKE_PROFIT_STOP_LOSS_FUTURES_COIN")
public class PeriodicTakeProfitStopLossStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer maPeriod = getIntParam(params, "maPeriod", 20);
        BigDecimal takeProfitPercentage = getBigDecimalParam(params, "takeProfitPercentage", new BigDecimal("0.03"));
        BigDecimal stopLossPercentage = getBigDecimalParam(params, "stopLossPercentage", new BigDecimal("0.02"));
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal baseQuantity = getBigDecimalParam(params, "baseQuantity", new BigDecimal("0.01"));

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", maPeriod + 10);
        if (klines == null || klines.size() < maPeriod) {
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

        if (closePrices.size() < maPeriod) {
            return;
        }

        // 计算移动平均线
        BigDecimal ma = TechnicalIndicatorUtil.calculateSMA(closePrices, maPeriod);
        if (ma == null) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 判断趋势
        boolean uptrend = currentPrice.compareTo(ma) > 0;

        // 执行交易
        if (orderService != null) {
            if (uptrend) {
                // 上升趋势，做多（设置止盈止损）
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "BUY", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                    // TODO: 设置止盈止损价格
                } catch (Exception e) {
                    // 记录日志
                }
            } else {
                // 下降趋势，做空（设置止盈止损）
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "SELL", "MARKET", baseQuantity, currentPrice, leverage, marginMode);
                    // TODO: 设置止盈止损价格
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    @Override
    public String getStrategyType() {
        return "PERIODIC_TAKE_PROFIT_STOP_LOSS";
    }

    @Override
    public String getStrategyName() {
        return "定期止盈止损策略";
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
