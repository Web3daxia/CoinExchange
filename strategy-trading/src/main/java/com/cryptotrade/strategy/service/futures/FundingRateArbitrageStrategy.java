/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.futures;

import com.cryptotrade.futures.usdt.service.FuturesMarketDataService;
import com.cryptotrade.futures.usdt.dto.response.FuturesMarketDataResponse;
import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataService;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesMarketDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 资金费率套利策略（合约）
 * 通过做多和做空合约同时进行，从资金费率中获益
 */
@Component
public class FundingRateArbitrageStrategy extends AbstractFuturesStrategy {

    @Autowired(required = false)
    private FuturesMarketDataService futuresUsdtMarketDataService;

    @Autowired(required = false)
    private CoinFuturesMarketDataService futuresCoinMarketDataService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!marketType.startsWith("FUTURES")) {
            throw new IllegalArgumentException("资金费率套利策略仅支持合约市场");
        }

        // 读取策略参数
        BigDecimal minFundingRate = getBigDecimalParam(strategyParams, "minFundingRate", new BigDecimal("0.0001")); // 最小资金费率0.01%
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));
        Integer leverage = getIntegerParam(strategyParams, "leverage", 10);

        // 获取当前资金费率
        BigDecimal fundingRate = getFundingRate(pairName, marketType);
        if (fundingRate == null) {
            throw new RuntimeException("无法获取资金费率");
        }

        // 如果资金费率足够高，进行套利
        if (fundingRate.abs().compareTo(minFundingRate) > 0) {
            BigDecimal currentPrice = getCurrentFuturesPrice(pairName, marketType);
            BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

            if (fundingRate.compareTo(BigDecimal.ZERO) > 0) {
                // 资金费率为正，做多方支付做空方，我们做多收取费用
                // 实际上需要同时持有多空仓位，做多方收取资金费用
                // TODO: 创建多单
                // TODO: 创建空单（对冲风险，只赚取资金费用）
                createFuturesOrder(userId, pairName, marketType, "BUY", "OPEN", "LONG", currentPrice, quantity, leverage);
                createFuturesOrder(userId, pairName, marketType, "SELL", "OPEN", "SHORT", currentPrice, quantity, leverage);
            } else {
                // 资金费率为负，做空方支付做多方，我们做空收取费用
                // TODO: 创建空单
                // TODO: 创建多单（对冲风险，只赚取资金费用）
                createFuturesOrder(userId, pairName, marketType, "SELL", "OPEN", "SHORT", currentPrice, quantity, leverage);
                createFuturesOrder(userId, pairName, marketType, "BUY", "OPEN", "LONG", currentPrice, quantity, leverage);
            }
        }
    }

    /**
     * 获取当前资金费率
     * 
     * @param pairName 交易对名称
     * @param marketType 市场类型（FUTURES_USDT 或 FUTURES_COIN）
     * @return 资金费率，如果获取失败返回 null
     */
    private BigDecimal getFundingRate(String pairName, String marketType) {
        try {
            // 根据市场类型获取市场数据
            if ("FUTURES_USDT".equals(marketType) && futuresUsdtMarketDataService != null) {
                FuturesMarketDataResponse marketData = futuresUsdtMarketDataService.getMarketData(pairName);
                if (marketData != null && marketData.getFundingRate() != null) {
                    return marketData.getFundingRate();
                }
            } else if ("FUTURES_COIN".equals(marketType) && futuresCoinMarketDataService != null) {
                CoinFuturesMarketDataResponse marketData = futuresCoinMarketDataService.getMarketData(pairName);
                if (marketData != null && marketData.getFundingRate() != null) {
                    return marketData.getFundingRate();
                }
            }
        } catch (Exception e) {
            // 如果获取失败，返回 null，让调用方处理
            return null;
        }
        return null;
    }

    /**
     * 获取当前合约价格
     * 
     * @param pairName 交易对名称
     * @param marketType 市场类型（FUTURES_USDT 或 FUTURES_COIN）
     * @return 当前价格，如果获取失败返回默认价格
     */
    private BigDecimal getCurrentFuturesPrice(String pairName, String marketType) {
        try {
            if ("FUTURES_USDT".equals(marketType) && futuresUsdtMarketDataService != null) {
                FuturesMarketDataResponse marketData = futuresUsdtMarketDataService.getMarketData(pairName);
                if (marketData != null && marketData.getCurrentPrice() != null) {
                    return marketData.getCurrentPrice();
                }
            } else if ("FUTURES_COIN".equals(marketType) && futuresCoinMarketDataService != null) {
                CoinFuturesMarketDataResponse marketData = futuresCoinMarketDataService.getMarketData(pairName);
                if (marketData != null && marketData.getCurrentPrice() != null) {
                    return marketData.getCurrentPrice();
                }
            }
        } catch (Exception e) {
            // 如果获取失败，返回默认值
        }
        return new BigDecimal("50000"); // 默认价格
    }

    private void createFuturesOrder(Long userId, String pairName, String marketType, String side,
                                   String action, String positionSide, BigDecimal price,
                                   BigDecimal quantity, Integer leverage) {
        // TODO: 实现合约订单创建
    }

    @Override
    public String getStrategyType() {
        return "FUNDING_RATE_ARBITRAGE";
    }

    @Override
    public String getStrategyName() {
        return "资金费率套利策略";
    }
}














