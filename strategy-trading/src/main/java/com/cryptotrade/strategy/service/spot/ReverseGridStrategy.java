/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.spot;

import com.cryptotrade.spot.dto.response.MarketDataResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 反向网格交易策略（现货）
 * 与传统网格交易相反，价格突破网格范围时，做出相反的交易决策
 */
@Component
public class ReverseGridStrategy extends AbstractSpotStrategy {

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!"SPOT".equals(marketType)) {
            throw new IllegalArgumentException("反向网格交易策略仅支持现货市场");
        }

        // 读取策略参数
        BigDecimal gridLowerPrice = getBigDecimalParam(strategyParams, "gridLowerPrice", null);
        BigDecimal gridUpperPrice = getBigDecimalParam(strategyParams, "gridUpperPrice", null);
        Integer gridCount = getIntegerParam(strategyParams, "gridCount", 10);
        BigDecimal gridSpacing = getBigDecimalParam(strategyParams, "gridSpacing", null);
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));

        // 获取当前价格
        MarketDataResponse marketData = marketDataService.getMarketData(pairName);
        BigDecimal currentPrice = marketData.getCurrentPrice();
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 如果未设置价格区间，根据当前价格自动计算
        if (gridLowerPrice == null || gridUpperPrice == null) {
            BigDecimal priceRange = currentPrice.multiply(new BigDecimal("0.2")); // 上下20%
            gridLowerPrice = currentPrice.subtract(priceRange);
            gridUpperPrice = currentPrice.add(priceRange);
        }

        // 如果未设置网格间距，根据网格数量计算
        if (gridSpacing == null) {
            gridSpacing = gridUpperPrice.subtract(gridLowerPrice)
                    .divide(new BigDecimal(gridCount), 8, RoundingMode.HALF_UP);
        }

        // 计算订单数量
        BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

        // 反向网格逻辑：如果价格突破上轨，卖出（预期反转）；如果价格突破下轨，买入（预期反转）
        if (currentPrice.compareTo(gridUpperPrice) > 0) {
            // 价格突破上轨，反向操作：卖出（预期价格下跌）
            createOrder(userId, pairName, "SELL", currentPrice, quantity);
        } else if (currentPrice.compareTo(gridLowerPrice) < 0) {
            // 价格突破下轨，反向操作：买入（预期价格上涨）
            createOrder(userId, pairName, "BUY", currentPrice, quantity);
        }
        // 价格在网格范围内，不操作
    }

    @Override
    public String getStrategyType() {
        return "REVERSE_GRID";
    }

    @Override
    public String getStrategyName() {
        return "反向网格交易策略";
    }
}















