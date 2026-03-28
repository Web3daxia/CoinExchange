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
 * 资金管理策略（币本位合约）
 * 
 * 策略逻辑：
 * - 根据市场资金流向调整仓位
 * - 结合技术指标和成交量进行交易
 */
@Component("FUND_MANAGEMENT_COIN_FUTURES_COIN")
public class FundManagementCoinStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer maPeriod = getIntParam(params, "maPeriod", 20);
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal basePositionRatio = getBigDecimalParam(params, "basePositionRatio", new BigDecimal("0.3"));
        BigDecimal maxPositionRatio = getBigDecimalParam(params, "maxPositionRatio", new BigDecimal("0.8"));

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", maPeriod + 10);
        if (klines == null || klines.size() < maPeriod) {
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

            if (volumeObj != null) {
                if (volumeObj instanceof BigDecimal) {
                    volumes.add((BigDecimal) volumeObj);
                } else if (volumeObj instanceof Number) {
                    volumes.add(new BigDecimal(volumeObj.toString()));
                }
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

        // 计算成交量变化
        BigDecimal avgVolume = BigDecimal.ZERO;
        if (!volumes.isEmpty() && volumes.size() >= maPeriod) {
            BigDecimal sum = volumes.stream()
                    .skip(Math.max(0, volumes.size() - maPeriod))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgVolume = sum.divide(new BigDecimal(maPeriod), 8, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal recentVolume = volumes.isEmpty() ? BigDecimal.ZERO : volumes.get(volumes.size() - 1);
        boolean volumeIncrease = avgVolume.compareTo(BigDecimal.ZERO) > 0 && 
                                recentVolume.compareTo(avgVolume.multiply(new BigDecimal("1.2"))) > 0;

        // 判断信号
        boolean bullishSignal = currentPrice.compareTo(ma) > 0 && volumeIncrease;
        boolean bearishSignal = currentPrice.compareTo(ma) < 0 && volumeIncrease;

        // 执行交易
        if (orderService != null) {
            BigDecimal baseAmount = new BigDecimal("100"); // TODO: 从账户余额计算
            BigDecimal orderAmount = baseAmount.multiply(basePositionRatio);
            BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);

            if (bullishSignal) {
                // 资金流入，做多
                try {
                    orderService.createFuturesCoinOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (bearishSignal) {
                // 资金流出，做空
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
        return "FUND_MANAGEMENT_COIN";
    }

    @Override
    public String getStrategyName() {
        return "资金管理策略";
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
