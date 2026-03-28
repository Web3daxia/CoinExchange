/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.common.dto.MarketDepthRequest;
import com.cryptotrade.common.dto.MarketDepthResponse;
import com.cryptotrade.common.util.PricePrecisionUtil;
import com.cryptotrade.spot.dto.response.KlineDataResponse;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import com.cryptotrade.spot.entity.MarketDataConfig;
import com.cryptotrade.spot.entity.TradingPair;
import com.cryptotrade.spot.repository.MarketDataConfigRepository;
import com.cryptotrade.spot.repository.TradingPairRepository;
import com.cryptotrade.spot.service.MarketDataAdapter;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.spot.service.impl.BinanceMarketDataAdapter;
import com.cryptotrade.spot.service.impl.OKXMarketDataAdapter;
import com.cryptotrade.spot.service.impl.GateMarketDataAdapter;
import com.cryptotrade.spot.service.impl.HuobiMarketDataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    @Autowired
    private TradingPairRepository tradingPairRepository;

    @Autowired
    private MarketDataConfigRepository marketDataConfigRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BinanceMarketDataAdapter binanceAdapter;

    @Autowired
    private OKXMarketDataAdapter okxAdapter;

    @Autowired
    private GateMarketDataAdapter gateAdapter;

    @Autowired
    private HuobiMarketDataAdapter huobiAdapter;

    private static final String MARKET_DATA_PREFIX = "market:data:";
    private static final String CURRENT_DATA_SOURCE_KEY = "market:data:current_source";

    @Override
    public MarketDataResponse getMarketData(String pairName) {
        TradingPair tradingPair = tradingPairRepository.findByPairName(pairName)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));

        // 尝试从Redis缓存获取
        String cacheKey = MARKET_DATA_PREFIX + pairName;
        try {
            MarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            Map<String, Object> tickerData = adapter.get24hrTicker(symbol);
            Map<String, Object> depthData = adapter.getDepth(symbol, 20);

            MarketDataResponse response = new MarketDataResponse();
            response.setPairName(tradingPair.getPairName());
            response.setCurrentPrice((BigDecimal) tickerData.get("currentPrice"));
            response.setPriceChange24h((BigDecimal) tickerData.get("priceChange24h"));
            response.setVolume24h((BigDecimal) tickerData.get("volume24h"));
            response.setHigh24h((BigDecimal) tickerData.get("high24h"));
            response.setLow24h((BigDecimal) tickerData.get("low24h"));
            response.setBidPrice((BigDecimal) tickerData.get("bidPrice"));
            response.setAskPrice((BigDecimal) tickerData.get("askPrice"));
            response.setLastPrice((BigDecimal) tickerData.get("currentPrice"));
            response.setBids((List<Map<String, Object>>) depthData.get("bids"));
            response.setAsks((List<Map<String, Object>>) depthData.get("asks"));

            return response;
        } catch (Exception e) {
            // 如果API调用失败，返回数据库中的数据
            MarketDataResponse response = new MarketDataResponse();
            response.setPairName(tradingPair.getPairName());
            response.setCurrentPrice(tradingPair.getCurrentPrice());
            response.setPriceChange24h(tradingPair.getPriceChange24h());
            response.setVolume24h(tradingPair.getVolume24h());
            response.setHigh24h(tradingPair.getHigh24h());
            response.setLow24h(tradingPair.getLow24h());
            return response;
        }
    }

    @Override
    public KlineDataResponse getKlineData(String pairName, String interval, Integer limit) {
        try {
            MarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            List<Map<String, Object>> klines = adapter.getKlines(symbol, interval, limit != null ? limit : 100);

            KlineDataResponse response = new KlineDataResponse();
            List<KlineDataResponse.KlineItem> items = new ArrayList<>();
            
            for (Map<String, Object> kline : klines) {
                KlineDataResponse.KlineItem item = new KlineDataResponse.KlineItem();
                Long openTime = Long.parseLong(kline.get("openTime").toString());
                item.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(openTime), ZoneId.systemDefault()));
                item.setOpen((BigDecimal) kline.get("open"));
                item.setHigh((BigDecimal) kline.get("high"));
                item.setLow((BigDecimal) kline.get("low"));
                item.setClose((BigDecimal) kline.get("close"));
                item.setVolume((BigDecimal) kline.get("volume"));
                items.add(item);
            }
            
            response.setKlines(items);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("获取K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMarketDepth(String pairName, Integer limit) {
        try {
            MarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            return adapter.getDepth(symbol, limit != null ? limit : 20);
        } catch (Exception e) {
            throw new RuntimeException("获取市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public MarketDepthResponse getMarketDepthEnhanced(MarketDepthRequest request) {
        String pair = request.getPair();
        Integer limit = request.getLimit() != null ? request.getLimit() : 20;
        String depthType = request.getDepthType() != null ? request.getDepthType() : "BOTH";
        String pricePrecisionStr = request.getPricePrecision();
        
        // 解析价格精度
        BigDecimal pricePrecision = null;
        if (pricePrecisionStr != null && !pricePrecisionStr.isEmpty()) {
            try {
                pricePrecision = new BigDecimal(pricePrecisionStr);
            } catch (Exception e) {
                // 忽略精度解析错误
            }
        }
        
        // 获取原始市场深度数据
        Map<String, Object> rawDepth = getMarketDepth(pair, limit);
        
        // 获取当前价格
        MarketDataResponse marketData = getMarketData(pair);
        BigDecimal currentPrice = marketData.getCurrentPrice();
        if (pricePrecision != null && currentPrice != null) {
            currentPrice = PricePrecisionUtil.formatPrice(currentPrice, pricePrecision);
        }
        
        // 构建响应
        MarketDepthResponse response = new MarketDepthResponse();
        response.setPair(pair);
        response.setPricePrecision(pricePrecisionStr);
        response.setCurrentPrice(currentPrice);
        
        // 处理买盘
        List<MarketDepthResponse.DepthItem> bids = new ArrayList<>();
        if ("BUY".equals(depthType) || "BOTH".equals(depthType)) {
            @SuppressWarnings("unchecked")
            List<List<Object>> rawBids = (List<List<Object>>) rawDepth.get("bids");
            if (rawBids != null) {
                BigDecimal cumulative = BigDecimal.ZERO;
                for (int i = 0; i < Math.min(limit, rawBids.size()); i++) {
                    List<Object> bid = rawBids.get(i);
                    BigDecimal price;
                    BigDecimal quantity;
                    
                    // 处理不同的数据格式
                    if (bid.get(0) instanceof BigDecimal) {
                        price = (BigDecimal) bid.get(0);
                        quantity = (BigDecimal) bid.get(1);
                    } else if (bid.get(0) instanceof String) {
                        price = new BigDecimal((String) bid.get(0));
                        quantity = new BigDecimal((String) bid.get(1));
                    } else {
                        price = new BigDecimal(bid.get(0).toString());
                        quantity = new BigDecimal(bid.get(1).toString());
                    }
                    
                    // 应用价格精度
                    if (pricePrecision != null) {
                        price = PricePrecisionUtil.formatPrice(price, pricePrecision);
                    }
                    
                    cumulative = cumulative.add(quantity);
                    
                    MarketDepthResponse.DepthItem item = new MarketDepthResponse.DepthItem();
                    item.setPrice(price);
                    item.setQuantity(quantity);
                    item.setCumulative(cumulative);
                    bids.add(item);
                }
            }
        }
        
        // 处理卖盘
        List<MarketDepthResponse.DepthItem> asks = new ArrayList<>();
        if ("SELL".equals(depthType) || "BOTH".equals(depthType)) {
            @SuppressWarnings("unchecked")
            List<List<Object>> rawAsks = (List<List<Object>>) rawDepth.get("asks");
            if (rawAsks != null) {
                BigDecimal cumulative = BigDecimal.ZERO;
                for (int i = 0; i < Math.min(limit, rawAsks.size()); i++) {
                    List<Object> ask = rawAsks.get(i);
                    BigDecimal price;
                    BigDecimal quantity;
                    
                    // 处理不同的数据格式
                    if (ask.get(0) instanceof BigDecimal) {
                        price = (BigDecimal) ask.get(0);
                        quantity = (BigDecimal) ask.get(1);
                    } else if (ask.get(0) instanceof String) {
                        price = new BigDecimal((String) ask.get(0));
                        quantity = new BigDecimal((String) ask.get(1));
                    } else {
                        price = new BigDecimal(ask.get(0).toString());
                        quantity = new BigDecimal(ask.get(1).toString());
                    }
                    
                    // 应用价格精度
                    if (pricePrecision != null) {
                        price = PricePrecisionUtil.formatPrice(price, pricePrecision);
                    }
                    
                    cumulative = cumulative.add(quantity);
                    
                    MarketDepthResponse.DepthItem item = new MarketDepthResponse.DepthItem();
                    item.setPrice(price);
                    item.setQuantity(quantity);
                    item.setCumulative(cumulative);
                    asks.add(item);
                }
            }
        }
        
        response.setBids(bids);
        response.setAsks(asks);
        
        return response;
    }

    @Override
    @Transactional
    public void switchDataSource(String dataSource) {
        // 验证数据源是否支持
        if (!isValidDataSource(dataSource)) {
            throw new RuntimeException("不支持的数据源: " + dataSource);
        }

        // 更新配置
        Optional<MarketDataConfig> activeConfig = marketDataConfigRepository.findByIsActiveTrue();
        if (activeConfig.isPresent()) {
            MarketDataConfig config = activeConfig.get();
            config.setIsActive(false);
            marketDataConfigRepository.save(config);
        }

        MarketDataConfig newConfig = marketDataConfigRepository.findByDataSource(dataSource)
                .orElseGet(() -> {
                    MarketDataConfig config = new MarketDataConfig();
                    config.setDataSource(dataSource);
                    return config;
                });

        newConfig.setIsActive(true);
        marketDataConfigRepository.save(newConfig);

        // 更新Redis缓存
        redisTemplate.opsForValue().set(CURRENT_DATA_SOURCE_KEY, dataSource);
    }

    @Override
    public String getCurrentDataSource() {
        String source = redisTemplate.opsForValue().get(CURRENT_DATA_SOURCE_KEY);
        if (source == null) {
            Optional<MarketDataConfig> activeConfig = marketDataConfigRepository.findByIsActiveTrue();
            if (activeConfig.isPresent()) {
                source = activeConfig.get().getDataSource();
                redisTemplate.opsForValue().set(CURRENT_DATA_SOURCE_KEY, source);
            } else {
                // 默认使用币安
                source = "BINANCE";
                switchDataSource(source);
            }
        }
        return source;
    }

    @Override
    @Transactional
    public void syncMarketData(String pairName) {
        TradingPair tradingPair = tradingPairRepository.findByPairName(pairName)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));

        try {
            MarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            Map<String, Object> tickerData = adapter.get24hrTicker(symbol);

            // 更新数据库中的市场数据
            tradingPair.setCurrentPrice((BigDecimal) tickerData.get("currentPrice"));
            tradingPair.setPriceChange24h((BigDecimal) tickerData.get("priceChange24h"));
            tradingPair.setVolume24h((BigDecimal) tickerData.get("volume24h"));
            tradingPair.setHigh24h((BigDecimal) tickerData.get("high24h"));
            tradingPair.setLow24h((BigDecimal) tickerData.get("low24h"));
            tradingPairRepository.save(tradingPair);

            // 更新Redis缓存
            String cacheKey = MARKET_DATA_PREFIX + pairName;
            // 可以在这里缓存数据到Redis
        } catch (Exception e) {
            throw new RuntimeException("同步市场数据失败: " + e.getMessage(), e);
        }
    }

    private MarketDataAdapter getCurrentAdapter() {
        String dataSource = getCurrentDataSource();
        switch (dataSource) {
            case "BINANCE":
                return binanceAdapter;
            case "OKX":
                return okxAdapter;
            case "GATE":
                return gateAdapter;
            case "HUOBI":
                return huobiAdapter;
            default:
                return binanceAdapter; // 默认使用币安
        }
    }

    private boolean isValidDataSource(String dataSource) {
        return "BINANCE".equals(dataSource) || 
               "OKX".equals(dataSource) || 
               "GATE".equals(dataSource) || 
               "HUOBI".equals(dataSource);
    }
}


