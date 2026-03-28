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
import java.math.RoundingMode;
import java.util.Map;

/**
 * 反向交易策略（合约）
 * 在市场极度看涨或看跌时，采取与市场主流相反的仓位
 */
@Component
public class ContrarianStrategy extends AbstractFuturesStrategy {

    @Autowired(required = false)
    private FuturesMarketDataService futuresUsdtMarketDataService;

    @Autowired(required = false)
    private CoinFuturesMarketDataService futuresCoinMarketDataService;

    @Override
    public void execute(Long userId, String pairName, String marketType, Map<String, Object> strategyParams) throws Exception {
        if (!marketType.startsWith("FUTURES")) {
            throw new IllegalArgumentException("反向交易策略仅支持合约市场");
        }

        // 读取策略参数
        BigDecimal extremeThreshold = getBigDecimalParam(strategyParams, "extremeThreshold", new BigDecimal("0.05")); // 极端阈值5%
        BigDecimal orderAmount = getBigDecimalParam(strategyParams, "orderAmount", new BigDecimal("100"));
        Integer leverage = getIntegerParam(strategyParams, "leverage", 10);
        Integer lookbackPeriod = getIntegerParam(strategyParams, "lookbackPeriod", 20);

        // 获取市场情绪指标（如持仓量、资金费率等）
        BigDecimal marketSentiment = getMarketSentiment(pairName, marketType, lookbackPeriod);
        
        // 获取当前价格
        BigDecimal currentPrice = getCurrentFuturesPrice(pairName, marketType);
        BigDecimal quantity = calculateQuantity(orderAmount, currentPrice);

        // 如果市场极度看涨（情绪指标很高），反向做空
        if (marketSentiment.compareTo(extremeThreshold) > 0) {
            createFuturesOrder(userId, pairName, marketType, "SELL", "OPEN", "SHORT", currentPrice, quantity, leverage);
        }
        // 如果市场极度看跌（情绪指标很低），反向做多
        else if (marketSentiment.compareTo(extremeThreshold.negate()) < 0) {
            createFuturesOrder(userId, pairName, marketType, "BUY", "OPEN", "LONG", currentPrice, quantity, leverage);
        }
    }

    /**
     * 计算市场情绪指标（基于资金费率、价格变化等）
     * 市场情绪指标范围：-1 到 1
     * - 正值表示看涨情绪，值越大越看涨
     * - 负值表示看跌情绪，值越小越看跌
     * 
     * @param pairName 交易对名称
     * @param marketType 市场类型（FUTURES_USDT 或 FUTURES_COIN）
     * @param period 回看周期（暂未使用，可用于计算历史平均）
     * @return 市场情绪指标
     */
    private BigDecimal getMarketSentiment(String pairName, String marketType, Integer period) {
        try {
            BigDecimal fundingRate = BigDecimal.ZERO;
            BigDecimal priceChange24h = BigDecimal.ZERO;
            BigDecimal currentPrice = BigDecimal.ZERO;

            // 根据市场类型获取市场数据
            if ("FUTURES_USDT".equals(marketType) && futuresUsdtMarketDataService != null) {
                FuturesMarketDataResponse marketData = futuresUsdtMarketDataService.getMarketData(pairName);
                if (marketData != null) {
                    fundingRate = marketData.getFundingRate() != null ? marketData.getFundingRate() : BigDecimal.ZERO;
                    priceChange24h = marketData.getPriceChange24h() != null ? marketData.getPriceChange24h() : BigDecimal.ZERO;
                    currentPrice = marketData.getCurrentPrice() != null ? marketData.getCurrentPrice() : BigDecimal.ZERO;
                }
            } else if ("FUTURES_COIN".equals(marketType) && futuresCoinMarketDataService != null) {
                CoinFuturesMarketDataResponse marketData = futuresCoinMarketDataService.getMarketData(pairName);
                if (marketData != null) {
                    fundingRate = marketData.getFundingRate() != null ? marketData.getFundingRate() : BigDecimal.ZERO;
                    priceChange24h = marketData.getPriceChange24h() != null ? marketData.getPriceChange24h() : BigDecimal.ZERO;
                    currentPrice = marketData.getCurrentPrice() != null ? marketData.getCurrentPrice() : BigDecimal.ZERO;
                }
            } else {
                // 如果服务不可用，返回中性情绪
                return BigDecimal.ZERO;
            }

            // 计算市场情绪指标
            // 1. 资金费率指标（权重 60%）
            // 资金费率越高，看涨情绪越强；资金费率越低（负值），看跌情绪越强
            // 将资金费率标准化到 -1 到 1 范围（假设资金费率范围是 -0.01 到 0.01）
            BigDecimal fundingRateNormalized = fundingRate.multiply(new BigDecimal("100"))
                    .setScale(4, RoundingMode.HALF_UP);
            // 限制在 -1 到 1 之间
            if (fundingRateNormalized.compareTo(BigDecimal.ONE) > 0) {
                fundingRateNormalized = BigDecimal.ONE;
            } else if (fundingRateNormalized.compareTo(BigDecimal.ONE.negate()) < 0) {
                fundingRateNormalized = BigDecimal.ONE.negate();
            }
            BigDecimal fundingRateIndicator = fundingRateNormalized.multiply(new BigDecimal("0.6"));

            // 2. 价格变化指标（权重 40%）
            // 24小时涨跌幅，转换为情绪指标
            BigDecimal priceChangeIndicator = BigDecimal.ZERO;
            if (currentPrice.compareTo(BigDecimal.ZERO) > 0 && priceChange24h != null) {
                // priceChange24h 是24小时涨跌金额，计算涨跌幅百分比
                // 24小时前价格 = 当前价格 - priceChange24h
                BigDecimal price24hAgo = currentPrice.subtract(priceChange24h);
                if (price24hAgo.compareTo(BigDecimal.ZERO) > 0) {
                    // 涨跌幅百分比 = (当前价格 - 24小时前价格) / 24小时前价格 * 100
                    BigDecimal priceChangePercent = priceChange24h.divide(price24hAgo, 6, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    // 限制在 -1 到 1 之间（假设涨跌幅范围是 -100% 到 100%，但实际可能更大）
                    // 使用 tanh 函数的思想：将百分比除以 100 并限制在合理范围
                    if (priceChangePercent.compareTo(new BigDecimal("100")) > 0) {
                        priceChangePercent = new BigDecimal("100");
                    } else if (priceChangePercent.compareTo(new BigDecimal("-100")) < 0) {
                        priceChangePercent = new BigDecimal("-100");
                    }
                    // 标准化到 -1 到 1
                    BigDecimal normalizedPercent = priceChangePercent.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
                    priceChangeIndicator = normalizedPercent.multiply(new BigDecimal("0.4"));
                }
            }

            // 综合市场情绪指标
            BigDecimal marketSentiment = fundingRateIndicator.add(priceChangeIndicator);
            
            // 限制在 -1 到 1 之间
            if (marketSentiment.compareTo(BigDecimal.ONE) > 0) {
                marketSentiment = BigDecimal.ONE;
            } else if (marketSentiment.compareTo(BigDecimal.ONE.negate()) < 0) {
                marketSentiment = BigDecimal.ONE.negate();
            }

            return marketSentiment.setScale(4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            // 如果获取市场数据失败，返回中性情绪
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取当前合约价格
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
        return "CONTRARIAN";
    }

    @Override
    public String getStrategyName() {
        return "反向交易策略";
    }
}














