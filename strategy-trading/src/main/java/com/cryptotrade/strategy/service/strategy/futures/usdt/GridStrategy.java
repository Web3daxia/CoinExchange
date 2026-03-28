/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.strategy.futures.usdt;

import com.cryptotrade.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网格策略（USDT本位合约）
 * 
 * 策略逻辑：
 * - 在价格区间内设置多个网格
 * - 价格跌到网格底部时做多
 * - 价格涨到网格顶部时做空
 */
@Component("GRID_FUTURES_USDT")
public class GridStrategy implements StrategyService {

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotMarketDataService marketDataService;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.RobotOrderService orderService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> params) throws Exception {
        BigDecimal upperPrice = getBigDecimalParam(params, "upperPrice", null);
        BigDecimal lowerPrice = getBigDecimalParam(params, "lowerPrice", null);
        Integer gridCount = getIntParam(params, "gridCount", 10);
        Integer leverage = getIntParam(params, "leverage", 10);
        String marginMode = getStringParam(params, "marginMode", "ISOLATED");
        BigDecimal investmentRatio = getBigDecimalParam(params, "investmentRatio", new BigDecimal("0.5"));

        // 获取当前价格
        if (marketDataService == null) {
            throw new RuntimeException("市场数据服务未配置");
        }

        BigDecimal currentPrice = marketDataService.getCurrentPrice(pairName, marketType);
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // 如果未指定价格区间，使用默认区间（当前价格的±10%）
        if (upperPrice == null || lowerPrice == null) {
            upperPrice = currentPrice.multiply(new BigDecimal("1.1"));
            lowerPrice = currentPrice.multiply(new BigDecimal("0.9"));
        }

        // 计算网格价格
        BigDecimal priceRange = upperPrice.subtract(lowerPrice);
        BigDecimal gridStep = priceRange.divide(new BigDecimal(gridCount), 8, BigDecimal.ROUND_HALF_UP);

        // 确定当前价格所在的网格
        BigDecimal priceOffset = currentPrice.subtract(lowerPrice);
        int currentGrid = priceOffset.divide(gridStep, 0, BigDecimal.ROUND_DOWN).intValue();

        // 计算每个网格的投资金额
        BigDecimal totalInvestment = new BigDecimal("1000"); // TODO: 从账户余额和investmentRatio计算
        BigDecimal investmentPerGrid = totalInvestment.divide(new BigDecimal(gridCount), 8, BigDecimal.ROUND_DOWN);

        // 执行交易（简化处理：在网格底部做多，在网格顶部做空）
        if (orderService != null) {
            // 如果价格接近网格底部，做多
            if (currentGrid <= 1) {
                BigDecimal quantity = investmentPerGrid.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
                try {
                    orderService.createFuturesUsdtOrder(userId, pairName, "BUY", "MARKET", quantity, currentPrice, leverage, marginMode);
                } catch (Exception e) {
                    // 记录日志
                }
            }
            // 如果价格接近网格顶部，做空
            else if (currentGrid >= gridCount - 1) {
                BigDecimal quantity = investmentPerGrid.divide(currentPrice, 8, BigDecimal.ROUND_DOWN);
                
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
        return "GRID";
    }

    @Override
    public String getStrategyName() {
        return "网格策略";
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
