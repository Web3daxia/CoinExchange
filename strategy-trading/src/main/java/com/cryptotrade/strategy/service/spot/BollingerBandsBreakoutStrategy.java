/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.spot;

import com.cryptotrade.spot.dto.response.KlineDataResponse;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 布林带突破策略（现货）
 * 当价格突破布林带的上轨或下轨时，执行相应的买入或卖出操作
 */
@Component
public class BollingerBandsBreakoutStrategy extends AbstractSpotStrategy {

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!"SPOT".equals(marketType)) {
            throw new IllegalArgumentException("布林带突破策略仅支持现货市场");
        }

        // 读取策略参数
        Integer period = getIntegerParam(strategyParams, "period", 20); // 默认20周期
        BigDecimal stdDev = getBigDecimalParam(strategyParams, "stdDev", new BigDecimal("2.0")); // 默认2倍标准差
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));

        // 获取K线数据
        KlineDataResponse klineData = marketDataService.getKlineData(pairName, "1h", period + 10);
        if (klineData == null || klineData.getKlines() == null || klineData.getKlines().size() < period) {
            throw new RuntimeException("K线数据不足，无法计算布林带");
        }

        List<KlineDataResponse.KlineItem> klines = klineData.getKlines();

        // 计算布林带
        BollingerBands bands = calculateBollingerBands(klines, period, stdDev);

        // 获取当前价格
        MarketDataResponse marketData = marketDataService.getMarketData(pairName);
        BigDecimal currentPrice = marketData.getCurrentPrice();
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 计算订单数量
        BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

        // 如果价格突破上轨，买入（预计价格继续上涨）
        if (currentPrice.compareTo(bands.getUpperBand()) > 0) {
            createOrder(userId, pairName, "BUY", currentPrice, quantity);
        }
        // 如果价格突破下轨，卖出（预计价格继续下跌）
        else if (currentPrice.compareTo(bands.getLowerBand()) < 0) {
            createOrder(userId, pairName, "SELL", currentPrice, quantity);
        }
    }

    /**
     * 计算布林带
     */
    private BollingerBands calculateBollingerBands(List<KlineDataResponse.KlineItem> klines, int period, BigDecimal stdDevMultiplier) {
        int startIndex = Math.max(0, klines.size() - period);
        
        // 计算移动平均（中轨）
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = startIndex; i < klines.size(); i++) {
            sum = sum.add(klines.get(i).getClose());
        }
        BigDecimal middleBand = sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);

        // 计算标准差
        BigDecimal variance = BigDecimal.ZERO;
        for (int i = startIndex; i < klines.size(); i++) {
            BigDecimal diff = klines.get(i).getClose().subtract(middleBand);
            variance = variance.add(diff.multiply(diff));
        }
        BigDecimal avgVariance = variance.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
        
        // 计算标准差（简化版本，实际应该使用平方根）
        BigDecimal standardDeviation = sqrt(avgVariance);

        // 计算上下轨
        BigDecimal upperBand = middleBand.add(standardDeviation.multiply(stdDevMultiplier));
        BigDecimal lowerBand = middleBand.subtract(standardDeviation.multiply(stdDevMultiplier));

        return new BollingerBands(upperBand, middleBand, lowerBand);
    }

    /**
     * 简化的平方根计算（使用牛顿法）
     */
    private BigDecimal sqrt(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal x = value;
        for (int i = 0; i < 10; i++) {
            x = x.add(value.divide(x, 8, RoundingMode.HALF_UP))
                    .divide(new BigDecimal("2"), 8, RoundingMode.HALF_UP);
        }
        return x;
    }

    /**
     * 布林带数据结构
     */
    private static class BollingerBands {
        private final BigDecimal upperBand;
        private final BigDecimal middleBand;
        private final BigDecimal lowerBand;

        public BollingerBands(BigDecimal upperBand, BigDecimal middleBand, BigDecimal lowerBand) {
            this.upperBand = upperBand;
            this.middleBand = middleBand;
            this.lowerBand = lowerBand;
        }

        public BigDecimal getUpperBand() {
            return upperBand;
        }

        public BigDecimal getMiddleBand() {
            return middleBand;
        }

        public BigDecimal getLowerBand() {
            return lowerBand;
        }
    }

    @Override
    public String getStrategyType() {
        return "BOLLINGER_BANDS_BREAKOUT";
    }

    @Override
    public String getStrategyName() {
        return "布林带突破策略";
    }
}















