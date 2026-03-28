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
 * 动量交易策略（现货）
 * 基于市场的动量信号进行交易，价格上涨时买入，价格下跌时卖出
 */
@Component
public class MomentumTradingStrategy extends AbstractSpotStrategy {

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!"SPOT".equals(marketType)) {
            throw new IllegalArgumentException("动量交易策略仅支持现货市场");
        }

        // 读取策略参数
        Integer period = getIntegerParam(strategyParams, "period", 14); // 默认14周期
        BigDecimal momentumThreshold = getBigDecimalParam(strategyParams, "momentumThreshold", new BigDecimal("0.01")); // 默认1%
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));

        // 获取K线数据
        KlineDataResponse klineData = marketDataService.getKlineData(pairName, "1h", period + 10);
        if (klineData == null || klineData.getKlines() == null || klineData.getKlines().size() < period + 1) {
            throw new RuntimeException("K线数据不足，无法计算动量");
        }

        List<KlineDataResponse.KlineItem> klines = klineData.getKlines();
        
        // 获取当前价格和N周期前的价格
        BigDecimal currentPrice = klines.get(klines.size() - 1).getClose();
        BigDecimal previousPrice = klines.get(klines.size() - 1 - period).getClose();

        // 计算动量（价格变化率）
        BigDecimal momentum = currentPrice.subtract(previousPrice).divide(previousPrice, 4, RoundingMode.HALF_UP);

        // 计算订单数量
        BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

        // 如果动量大于阈值，买入
        if (momentum.compareTo(momentumThreshold) > 0) {
            MarketDataResponse marketData = marketDataService.getMarketData(pairName);
            BigDecimal marketPrice = marketData.getCurrentPrice();
            createOrder(userId, pairName, "BUY", marketPrice, quantity);
        }
        // 如果动量小于负阈值，卖出
        else if (momentum.compareTo(momentumThreshold.negate()) < 0) {
            MarketDataResponse marketData = marketDataService.getMarketData(pairName);
            BigDecimal marketPrice = marketData.getCurrentPrice();
            createOrder(userId, pairName, "SELL", marketPrice, quantity);
        }
    }

    @Override
    public String getStrategyType() {
        return "MOMENTUM_TRADING";
    }

    @Override
    public String getStrategyName() {
        return "动量交易策略";
    }
}















