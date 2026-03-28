/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.common.dto.MarketDepthRequest;
import com.cryptotrade.common.dto.MarketDepthResponse;
import com.cryptotrade.common.util.PricePrecisionUtil;
import com.cryptotrade.futures.usdt.dto.response.FuturesMarketDataResponse;
import com.cryptotrade.futures.usdt.entity.FuturesContract;
import com.cryptotrade.futures.usdt.repository.FuturesContractRepository;
import com.cryptotrade.futures.usdt.service.FuturesMarketDataAdapter;
import com.cryptotrade.futures.usdt.service.FuturesMarketDataService;
import com.cryptotrade.spot.entity.MarketDataConfig;
import com.cryptotrade.spot.repository.MarketDataConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FuturesMarketDataServiceImpl implements FuturesMarketDataService {

    @Autowired
    private FuturesContractRepository futuresContractRepository;

    @Autowired
    private MarketDataConfigRepository marketDataConfigRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private com.cryptotrade.futures.usdt.service.impl.BinanceFuturesMarketDataAdapter binanceAdapter;

    @Autowired
    private com.cryptotrade.futures.usdt.service.impl.OKXFuturesMarketDataAdapter okxAdapter;

    @Autowired
    private com.cryptotrade.futures.usdt.service.impl.GateFuturesMarketDataAdapter gateAdapter;

    @Autowired
    private com.cryptotrade.futures.usdt.service.impl.HuobiFuturesMarketDataAdapter huobiAdapter;

    private static final String MARKET_DATA_PREFIX = "futures:market:data:";
    private static final String CURRENT_DATA_SOURCE_KEY = "futures:market:data:current_source";

    @Override
    public FuturesMarketDataResponse getMarketData(String pairName) {
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(pairName, "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        // 尝试从实时API获取最新数据
        try {
            FuturesMarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            
            // 获取24小时行情数据
            Map<String, Object> tickerData = adapter.get24hrTicker(symbol);
            
            // 获取标记价格和指数价格
            Map<String, Object> markPriceData = adapter.getMarkPriceAndIndexPrice(symbol);
            
            // 获取资金费率
            BigDecimal fundingRate = adapter.getFundingRate(symbol);
            
            // 获取市场深度以获取买一卖一价
            Map<String, Object> depth = adapter.getDepth(symbol, 1);
            BigDecimal bidPrice = contract.getCurrentPrice();
            BigDecimal askPrice = contract.getCurrentPrice();
            
            if (depth != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> bids = (List<Map<String, Object>>) depth.get("bids");
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> asks = (List<Map<String, Object>>) depth.get("asks");
                
                if (bids != null && !bids.isEmpty()) {
                    bidPrice = (BigDecimal) bids.get(0).get("price");
                }
                if (asks != null && !asks.isEmpty()) {
                    askPrice = (BigDecimal) asks.get(0).get("price");
                }
            }

            FuturesMarketDataResponse response = new FuturesMarketDataResponse();
            response.setPairName(contract.getPairName());
            response.setCurrentPrice(tickerData.get("currentPrice") != null ? 
                    (BigDecimal) tickerData.get("currentPrice") : contract.getCurrentPrice());
            response.setIndexPrice(markPriceData.get("indexPrice") != null ? 
                    (BigDecimal) markPriceData.get("indexPrice") : contract.getIndexPrice());
            response.setMarkPrice(markPriceData.get("markPrice") != null ? 
                    (BigDecimal) markPriceData.get("markPrice") : contract.getMarkPrice());
            response.setFundingRate(fundingRate != null ? fundingRate : contract.getFundingRate());
            response.setNextFundingTime(markPriceData.get("nextFundingTime") != null ? 
                    (java.time.LocalDateTime) markPriceData.get("nextFundingTime") : contract.getNextFundingTime());
            response.setPriceChange24h(tickerData.get("priceChange24h") != null ? 
                    (BigDecimal) tickerData.get("priceChange24h") : contract.getPriceChange24h());
            response.setVolume24h(tickerData.get("volume24h") != null ? 
                    (BigDecimal) tickerData.get("volume24h") : contract.getVolume24h());
            response.setAmount24h(tickerData.get("amount24h") != null ? 
                    (BigDecimal) tickerData.get("amount24h") : contract.getAmount24h());
            response.setHigh24h(tickerData.get("high24h") != null ? 
                    (BigDecimal) tickerData.get("high24h") : contract.getHigh24h());
            response.setLow24h(tickerData.get("low24h") != null ? 
                    (BigDecimal) tickerData.get("low24h") : contract.getLow24h());
            response.setBidPrice(bidPrice);
            response.setAskPrice(askPrice);
            response.setMaxLeverage(contract.getMaxLeverage());
            response.setMinLeverage(contract.getMinLeverage());

            return response;
        } catch (Exception e) {
            // 如果API调用失败，返回数据库中的数据
            FuturesMarketDataResponse response = new FuturesMarketDataResponse();
            response.setPairName(contract.getPairName());
            response.setCurrentPrice(contract.getCurrentPrice());
            response.setIndexPrice(contract.getIndexPrice());
            response.setMarkPrice(contract.getMarkPrice());
            response.setFundingRate(contract.getFundingRate());
            response.setNextFundingTime(contract.getNextFundingTime());
            response.setPriceChange24h(contract.getPriceChange24h());
            response.setVolume24h(contract.getVolume24h());
            response.setAmount24h(contract.getAmount24h());
            response.setHigh24h(contract.getHigh24h());
            response.setLow24h(contract.getLow24h());
            response.setBidPrice(contract.getCurrentPrice());
            response.setAskPrice(contract.getCurrentPrice());
            response.setMaxLeverage(contract.getMaxLeverage());
            response.setMinLeverage(contract.getMinLeverage());
            return response;
        }
    }

    @Override
    public List<Map<String, Object>> getKlineData(String pairName, String interval, Integer limit) {
        try {
            FuturesMarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            return adapter.getKlineData(symbol, interval, limit);
        } catch (Exception e) {
            throw new RuntimeException("获取K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMarketDepth(String pairName, Integer limit) {
        try {
            FuturesMarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            return adapter.getDepth(symbol, limit);
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
        FuturesMarketDataResponse marketData = getMarketData(pair);
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
    public void syncMarketData(String pairName) {
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(pairName, "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        try {
            // 从当前数据源获取市场数据
            FuturesMarketDataAdapter adapter = getCurrentAdapter();
            String symbol = adapter.convertSymbol(pairName);
            
            // 获取24小时行情数据
            Map<String, Object> tickerData = adapter.get24hrTicker(symbol);
            
            // 获取标记价格和指数价格
            Map<String, Object> markPriceData = adapter.getMarkPriceAndIndexPrice(symbol);
            
            // 获取资金费率
            BigDecimal fundingRate = adapter.getFundingRate(symbol);

            // 更新合约数据
            if (tickerData.get("currentPrice") != null) {
                contract.setCurrentPrice((BigDecimal) tickerData.get("currentPrice"));
            }
            if (markPriceData.get("indexPrice") != null) {
                contract.setIndexPrice((BigDecimal) markPriceData.get("indexPrice"));
            }
            if (markPriceData.get("markPrice") != null) {
                contract.setMarkPrice((BigDecimal) markPriceData.get("markPrice"));
            }
            if (fundingRate != null) {
                contract.setFundingRate(fundingRate);
            }
            if (tickerData.get("priceChange24h") != null) {
                contract.setPriceChange24h((BigDecimal) tickerData.get("priceChange24h"));
            }
            if (tickerData.get("volume24h") != null) {
                contract.setVolume24h((BigDecimal) tickerData.get("volume24h"));
            }
            if (tickerData.get("amount24h") != null) {
                contract.setAmount24h((BigDecimal) tickerData.get("amount24h"));
            }
            if (tickerData.get("high24h") != null) {
                contract.setHigh24h((BigDecimal) tickerData.get("high24h"));
            }
            if (tickerData.get("low24h") != null) {
                contract.setLow24h((BigDecimal) tickerData.get("low24h"));
            }
            // 更新下次资金费率时间
            if (markPriceData.get("nextFundingTime") != null) {
                contract.setNextFundingTime((java.time.LocalDateTime) markPriceData.get("nextFundingTime"));
            }

            futuresContractRepository.save(contract);
        } catch (Exception e) {
            throw new RuntimeException("同步市场数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void switchDataSource(String dataSource) {
        // 验证数据源是否支持
        if (!isValidDataSource(dataSource)) {
            throw new RuntimeException("不支持的数据源: " + dataSource);
        }

        // 更新配置（与现货交易共享配置表）
        Optional<MarketDataConfig> activeConfigOpt = marketDataConfigRepository.findByIsActiveTrue();
        if (activeConfigOpt.isPresent()) {
            MarketDataConfig config = activeConfigOpt.get();
            config.setIsActive(false);
            marketDataConfigRepository.save(config);
        }

        // 查找或创建新配置
        Optional<MarketDataConfig> existingConfigOpt = marketDataConfigRepository.findByDataSource(dataSource);
        MarketDataConfig newConfig = existingConfigOpt.orElseGet(() -> {
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

    /**
     * 验证数据源是否支持
     */
    private boolean isValidDataSource(String dataSource) {
        return "BINANCE".equals(dataSource) || 
               "OKX".equals(dataSource) || 
               "GATE".equals(dataSource) || 
               "HUOBI".equals(dataSource);
    }

    /**
     * 获取当前数据源对应的适配器
     */
    private FuturesMarketDataAdapter getCurrentAdapter() {
        String source = getCurrentDataSource();
        switch (source) {
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
}

