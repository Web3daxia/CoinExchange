/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.service.RobotMarketDataService;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.futures.usdt.dto.response.FuturesMarketDataResponse;
import com.cryptotrade.futures.usdt.service.FuturesMarketDataService;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesMarketDataResponse;
import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataService;
import com.cryptotrade.spot.dto.response.KlineDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机器人市场数据服务实现类
 */
@Service
public class RobotMarketDataServiceImpl implements RobotMarketDataService {

    @Autowired(required = false)
    private MarketDataService spotMarketDataService;

    @Autowired(required = false)
    private FuturesMarketDataService futuresUsdtMarketDataService;

    @Autowired(required = false)
    private CoinFuturesMarketDataService futuresCoinMarketDataService;

    @Override
    public BigDecimal getCurrentPrice(String pairName, String marketType) {
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotMarketDataService != null) {
                        MarketDataResponse marketData = spotMarketDataService.getMarketData(pairName);
                        return marketData.getLastPrice();
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtMarketDataService != null) {
                        FuturesMarketDataResponse marketData = futuresUsdtMarketDataService.getMarketData(pairName);
                        return marketData.getCurrentPrice();
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinMarketDataService != null) {
                        CoinFuturesMarketDataResponse marketData = futuresCoinMarketDataService.getMarketData(pairName);
                        return marketData.getCurrentPrice();
                    }
                    break;
            }
        } catch (Exception e) {
            // 记录日志
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Map<String, Object>> getKlineData(String pairName, String marketType, String interval, Integer limit) {
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotMarketDataService != null) {
                        KlineDataResponse klineData = spotMarketDataService.getKlineData(pairName, interval, limit);
                        // 转换K线数据格式
                        // KlineDataResponse需要转换为List<Map<String, Object>>
                        // TODO: 根据实际KlineDataResponse结构转换
                        return new ArrayList<>();
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtMarketDataService != null) {
                        return futuresUsdtMarketDataService.getKlineData(pairName, interval, limit);
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinMarketDataService != null) {
                        return futuresCoinMarketDataService.getKlineData(pairName, interval, limit);
                    }
                    break;
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getMarketDepth(String pairName, String marketType, Integer limit) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotMarketDataService != null) {
                        return spotMarketDataService.getMarketDepth(pairName, limit);
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtMarketDataService != null) {
                        return futuresUsdtMarketDataService.getMarketDepth(pairName, limit);
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinMarketDataService != null) {
                        return futuresCoinMarketDataService.getMarketDepth(pairName, limit);
                    }
                    break;
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return result;
    }

    @Override
    public Map<String, Object> get24hrTicker(String pairName, String marketType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            switch (marketType) {
                case "SPOT":
                    if (spotMarketDataService != null) {
                        MarketDataResponse marketData = spotMarketDataService.getMarketData(pairName);
                        result.put("lastPrice", marketData.getLastPrice());
                        result.put("volume", marketData.getVolume24h());
                        result.put("high24h", marketData.getHigh24h());
                        result.put("low24h", marketData.getLow24h());
                        result.put("priceChange24h", marketData.getPriceChange24h());
                        // 计算价格变化百分比
                        if (marketData.getPriceChange24h() != null && marketData.getLastPrice() != null && marketData.getLastPrice().compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal previousPrice = marketData.getLastPrice().subtract(marketData.getPriceChange24h());
                            if (previousPrice.compareTo(BigDecimal.ZERO) > 0) {
                                BigDecimal changePercent = marketData.getPriceChange24h()
                                        .divide(previousPrice, 4, java.math.RoundingMode.HALF_UP)
                                        .multiply(java.math.BigDecimal.valueOf(100));
                                result.put("priceChangePercent24h", changePercent);
                            }
                        }
                        return result;
                    }
                    break;
                case "FUTURES_USDT":
                    if (futuresUsdtMarketDataService != null) {
                        FuturesMarketDataResponse marketData = futuresUsdtMarketDataService.getMarketData(pairName);
                        result.put("lastPrice", marketData.getCurrentPrice());
                        result.put("volume", marketData.getVolume24h());
                        result.put("high24h", marketData.getHigh24h());
                        result.put("low24h", marketData.getLow24h());
                        if (marketData.getPriceChange24h() != null) {
                            result.put("priceChange24h", marketData.getPriceChange24h());
                        }
                        return result;
                    }
                    break;
                case "FUTURES_COIN":
                    if (futuresCoinMarketDataService != null) {
                        CoinFuturesMarketDataResponse marketData = futuresCoinMarketDataService.getMarketData(pairName);
                        result.put("lastPrice", marketData.getCurrentPrice());
                        result.put("volume", marketData.getVolume24h());
                        result.put("high24h", marketData.getHigh24h());
                        result.put("low24h", marketData.getLow24h());
                        if (marketData.getPriceChange24h() != null) {
                            result.put("priceChange24h", marketData.getPriceChange24h());
                        }
                        return result;
                    }
                    break;
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return result;
    }
}

