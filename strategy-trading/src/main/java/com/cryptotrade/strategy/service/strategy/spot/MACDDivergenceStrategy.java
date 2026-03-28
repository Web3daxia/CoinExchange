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
 * MACD背离策略（现货）
 * 
 * 策略逻辑：
 * - 检测价格与MACD的背离信号
 * - 顶背离（价格创新高，MACD未创新高）：卖出信号
 * - 底背离（价格创新低，MACD未创新低）：买入信号
 */
@Component("MACD_DIVERGENCE_SPOT")
public class MACDDivergenceStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        Integer fastPeriod = getIntParam(params, "fastPeriod", 12);
        Integer slowPeriod = getIntParam(params, "slowPeriod", 26);
        Integer signalPeriod = getIntParam(params, "signalPeriod", 9);
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));
        Integer lookbackPeriod = getIntParam(params, "lookbackPeriod", 20); // 用于检测背离的周期

        // 获取历史K线数据
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        List<Map<String, Object>> klines = marketDataService.getKlineData(pairName, marketType, "1h", slowPeriod + lookbackPeriod + 10);
        if (klines == null || klines.size() < slowPeriod + lookbackPeriod) {
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

        if (closePrices.size() < slowPeriod + lookbackPeriod) {
            return;
        }

        // 计算MACD
        TechnicalIndicatorUtil.MACDResult currentMACD = TechnicalIndicatorUtil.calculateMACD(closePrices, fastPeriod, slowPeriod, signalPeriod);
        if (currentMACD == null) {
            return;
        }

        // 获取历史MACD值用于背离检测（简化处理，实际需要计算多个周期的MACD）
        List<BigDecimal> prevClosePrices = closePrices.subList(0, closePrices.size() - 1);
        TechnicalIndicatorUtil.MACDResult prevMACD = TechnicalIndicatorUtil.calculateMACD(prevClosePrices, fastPeriod, slowPeriod, signalPeriod);

        if (prevMACD == null) {
            return;
        }

        // 获取当前价格和历史价格
        BigDecimal currentPrice = closePrices.get(closePrices.size() - 1);
        BigDecimal prevPrice = closePrices.get(closePrices.size() - 2);

        // 检测顶背离（价格上升，MACD下降）
        boolean topDivergence = currentPrice.compareTo(prevPrice) > 0 && 
                                currentMACD.getDif().compareTo(prevMACD.getDif()) < 0;

        // 检测底背离（价格下降，MACD上升）
        boolean bottomDivergence = currentPrice.compareTo(prevPrice) < 0 && 
                                   currentMACD.getDif().compareTo(prevMACD.getDif()) > 0;

        // 执行交易
        if (orderService != null) {
            if (bottomDivergence) {
                // 底背离，买入信号
                BigDecimal orderAmount = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
                BigDecimal quantity = orderAmount.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createSpotOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice);
                } catch (Exception e) {
                    // 记录日志
                }
            } else if (topDivergence) {
                // 顶背离，卖出信号
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
        return "MACD_DIVERGENCE";
    }

    @Override
    public String getStrategyName() {
        return "MACD背离策略";
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
