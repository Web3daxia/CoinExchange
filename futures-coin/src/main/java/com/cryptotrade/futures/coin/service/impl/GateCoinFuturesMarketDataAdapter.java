/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gate.io币本位永续合约市场数据适配器
 * API文档: https://www.gate.io/docs/developers/apiv4/
 */
@Component
public class GateCoinFuturesMarketDataAdapter implements CoinFuturesMarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Gate.io币本位永续合约API基础地址
    private static final String GATE_COIN_FUTURES_API_BASE = "https://api.gateio.ws/api/v4";

    @Override
    public String convertSymbol(String pairName) {
        // BTC/BTC -> BTC_USD (Gate.io币本位永续合约使用下划线，合约类型为币本位永续)
        String base = pairName.split("/")[0];
        return base + "_USD";
    }

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // Gate.io币本位永续合约24小时行情: GET /api/v4/futures/btc/tickers?settle=btc&contract=BTC_USD
            // symbol已经是转换后的格式（如BTC_USD），需要提取基础货币
            String baseCurrency = symbol.split("_")[0].toLowerCase(); // 获取基础货币（如btc）
            String url = GATE_COIN_FUTURES_API_BASE + "/futures/" + baseCurrency + "/tickers?settle=" + baseCurrency + "&contract=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            List<Map<String, Object>> dataList = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            
            if (dataList == null || dataList.isEmpty()) {
                throw new RuntimeException("Gate API返回数据为空");
            }
            
            Map<String, Object> ticker = dataList.get(0);
            
            Map<String, Object> result = new HashMap<>();
            result.put("currentPrice", new BigDecimal(ticker.get("last").toString()));
            result.put("priceChange24h", new BigDecimal(ticker.get("change_percentage").toString()));
            result.put("volume24h", new BigDecimal(ticker.get("volume_24h").toString()));
            result.put("amount24h", new BigDecimal(ticker.get("volume_24h_base").toString()));
            result.put("high24h", new BigDecimal(ticker.get("high_24h").toString()));
            result.put("low24h", new BigDecimal(ticker.get("low_24h").toString()));
            result.put("bidPrice", new BigDecimal(ticker.get("highest_bid").toString()));
            result.put("askPrice", new BigDecimal(ticker.get("lowest_ask").toString()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate币本位永续合约24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            String baseCurrency = symbol.split("_")[0].toLowerCase();
            // Gate.io币本位永续合约订单簿: GET /api/v4/futures/btc/order_book?settle=btc&contract=BTC_USD
            String url = GATE_COIN_FUTURES_API_BASE + "/futures/" + baseCurrency + "/order_book?settle=" + baseCurrency + "&contract=" + symbol + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // Gate.io返回格式: {"asks":[[price,quantity],...],"bids":[[price,quantity],...]}
            List<List<String>> asks = (List<List<String>>) data.get("asks");
            List<List<String>> bids = (List<List<String>>) data.get("bids");
            
            List<Map<String, Object>> askList = new ArrayList<>();
            if (asks != null) {
                for (List<String> ask : asks) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("price", new BigDecimal(ask.get(0)));
                    item.put("quantity", new BigDecimal(ask.get(1)));
                    askList.add(item);
                }
            }
            
            List<Map<String, Object>> bidList = new ArrayList<>();
            if (bids != null) {
                for (List<String> bid : bids) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("price", new BigDecimal(bid.get(0)));
                    item.put("quantity", new BigDecimal(bid.get(1)));
                    bidList.add(item);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("bids", bidList);
            result.put("asks", askList);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate币本位永续合约市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlineData(String symbol, String interval, Integer limit) {
        try {
            String baseCurrency = symbol.split("_")[0].toLowerCase();
            String gateInterval = convertInterval(interval);
            // Gate.io币本位永续合约K线数据: GET /api/v4/futures/btc/candlesticks?settle=btc&contract=BTC_USD
            String url = GATE_COIN_FUTURES_API_BASE + "/futures/" + baseCurrency + "/candlesticks?settle=" + baseCurrency + "&contract=" + symbol 
                    + "&interval=" + gateInterval + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            List<List<String>> klines = objectMapper.readValue(response, new TypeReference<List<List<String>>>() {});
            
            if (klines == null) {
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (List<String> kline : klines) {
                // Gate.io K线数据格式: [timestamp, volume, close, high, low, open]
                Map<String, Object> item = new HashMap<>();
                item.put("openTime", Long.parseLong(kline.get(0)) * 1000); // Gate.io返回的是秒，转换为毫秒
                item.put("open", new BigDecimal(kline.get(5)));
                item.put("high", new BigDecimal(kline.get(3)));
                item.put("low", new BigDecimal(kline.get(4)));
                item.put("close", new BigDecimal(kline.get(2)));
                item.put("volume", new BigDecimal(kline.get(1)));
                result.add(item);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate币本位永续合约K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMarkPriceAndIndexPrice(String symbol) {
        try {
            String baseCurrency = symbol.split("_")[0].toLowerCase();
            // Gate.io币本位永续合约标记价格: GET /api/v4/futures/btc/tickers?settle=btc&contract=BTC_USD
            String url = GATE_COIN_FUTURES_API_BASE + "/futures/" + baseCurrency + "/tickers?settle=" + baseCurrency + "&contract=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            List<Map<String, Object>> dataList = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            
            if (dataList == null || dataList.isEmpty()) {
                throw new RuntimeException("Gate API返回数据为空");
            }
            
            Map<String, Object> ticker = dataList.get(0);
            
            Map<String, Object> result = new HashMap<>();
            // Gate.io的标记价格在mark_price字段
            if (ticker.get("mark_price") != null) {
                result.put("markPrice", new BigDecimal(ticker.get("mark_price").toString()));
            }
            // Gate.io的指数价格在index_price字段
            if (ticker.get("index_price") != null) {
                result.put("indexPrice", new BigDecimal(ticker.get("index_price").toString()));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate币本位永续合约标记价格和指数价格失败: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getFundingRate(String symbol) {
        try {
            String baseCurrency = symbol.split("_")[0].toLowerCase();
            // Gate.io币本位永续合约资金费率: GET /api/v4/futures/btc/funding_rate?settle=btc&contract=BTC_USD
            String url = GATE_COIN_FUTURES_API_BASE + "/futures/" + baseCurrency + "/funding_rate?settle=" + baseCurrency + "&contract=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            List<Map<String, Object>> dataList = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            
            if (dataList == null || dataList.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            Map<String, Object> fundingData = dataList.get(0);
            if (fundingData.get("r") != null) {
                return new BigDecimal(fundingData.get("r").toString());
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate币本位永续合约资金费率失败: " + e.getMessage(), e);
        }
    }

    private String convertInterval(String interval) {
        // 转换时间粒度
        Map<String, String> intervalMap = new HashMap<>();
        intervalMap.put("30s", "30s");
        intervalMap.put("1m", "1m");
        intervalMap.put("5m", "5m");
        intervalMap.put("10m", "10m");
        intervalMap.put("15m", "15m");
        intervalMap.put("30m", "30m");
        intervalMap.put("1h", "1h");
        intervalMap.put("2h", "2h");
        intervalMap.put("3h", "3h");
        intervalMap.put("4h", "4h");
        intervalMap.put("8h", "8h");
        intervalMap.put("12h", "12h");
        intervalMap.put("1d", "1d");
        intervalMap.put("2d", "2d");
        intervalMap.put("1w", "7d");
        intervalMap.put("1M", "30d");
        return intervalMap.getOrDefault(interval, "1h");
    }
}

