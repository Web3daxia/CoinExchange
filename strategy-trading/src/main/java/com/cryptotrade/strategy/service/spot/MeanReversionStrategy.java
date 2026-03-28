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
 * 均值回归策略（现货）
 * 当价格偏离均衡价时买入或卖出，等待价格回归
 */
@Component
public class MeanReversionStrategy extends AbstractSpotStrategy {

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!"SPOT".equals(marketType)) {
            throw new IllegalArgumentException("均值回归策略仅支持现货市场");
        }

        // 读取策略参数
        Integer lookbackPeriod = getIntegerParam(strategyParams, "lookbackPeriod", 20); // 默认20周期
        BigDecimal deviationThreshold = getBigDecimalParam(strategyParams, "deviationThreshold", new BigDecimal("0.02")); // 默认2%
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));

        // 获取K线数据
        KlineDataResponse klineData = marketDataService.getKlineData(pairName, "1h", lookbackPeriod + 10);
        if (klineData == null || klineData.getKlines() == null || klineData.getKlines().size() < lookbackPeriod) {
            throw new RuntimeException("K线数据不足，无法计算均值");
        }

        // 计算均值（移动平均）
        BigDecimal meanPrice = calculateMean(klineData.getKlines(), lookbackPeriod);

        // 获取当前价格
        MarketDataResponse marketData = marketDataService.getMarketData(pairName);
        BigDecimal currentPrice = marketData.getCurrentPrice();
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格");
        }

        // 计算价格偏离度
        BigDecimal deviation = currentPrice.subtract(meanPrice).divide(meanPrice, 4, RoundingMode.HALF_UP);

        // 计算订单数量
        BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

        // 如果价格低于均值，买入（价格回归）
        if (deviation.compareTo(deviationThreshold.negate()) < 0) {
            createOrder(userId, pairName, "BUY", currentPrice, quantity);
        }
        // 如果价格高于均值，卖出（价格回归）
        else if (deviation.compareTo(deviationThreshold) > 0) {
            createOrder(userId, pairName, "SELL", currentPrice, quantity);
        }
    }

    /**
     * 计算均值
     */
    private BigDecimal calculateMean(List<KlineDataResponse.KlineItem> klines, int period) {
        BigDecimal sum = BigDecimal.ZERO;
        int startIndex = Math.max(0, klines.size() - period);
        
        for (int i = startIndex; i < klines.size(); i++) {
            sum = sum.add(klines.get(i).getClose());
        }

        return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
    }

    @Override
    public String getStrategyType() {
        return "MEAN_REVERSION";
    }

    @Override
    public String getStrategyName() {
        return "均值回归策略";
    }
}
