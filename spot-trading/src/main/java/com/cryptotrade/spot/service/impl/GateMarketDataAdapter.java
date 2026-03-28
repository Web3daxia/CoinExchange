/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.service.MarketDataAdapter;
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

@Component
public class GateMarketDataAdapter implements MarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String GATE_API_BASE = "https://api.gateio.ws/api/v4";

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // Gate.io API: GET /api/v4/spot/tickers?currency_pair=BTC_USDT
            String gateSymbol = convertSymbol(symbol);
            String url = GATE_API_BASE + "/spot/tickers?currency_pair=" + gateSymbol;
            String response = restTemplate.getForObject(url, String.class);
            List<Map<String, Object>> dataList = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            
            if (dataList == null || dataList.isEmpty()) {
                throw new RuntimeException("Gate API返回数据为空");
            }
            
            Map<String, Object> ticker = dataList.get(0);
            
            Map<String, Object> result = new HashMap<>();
            result.put("currentPrice", new BigDecimal(ticker.get("last").toString()));
            result.put("priceChange24h", new BigDecimal(ticker.get("change_percentage").toString()));
            result.put("volume24h", new BigDecimal(ticker.get("base_volume").toString()));
            result.put("high24h", new BigDecimal(ticker.get("high_24h").toString()));
            result.put("low24h", new BigDecimal(ticker.get("low_24h").toString()));
            result.put("bidPrice", new BigDecimal(ticker.get("highest_bid").toString()));
            result.put("askPrice", new BigDecimal(ticker.get("lowest_ask").toString()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取Gate 24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlines(String symbol, String interval, Integer limit) {
        try {
            String gateSymbol = convertSymbol(symbol);
            String gateInterval = convertInterval(interval);
            String url = GATE_API_BASE + "/spot/candlesticks?currency_pair=" + gateSymbol 
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
            throw new RuntimeException("获取Gate K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            String gateSymbol = convertSymbol(symbol);
            String url = GATE_API_BASE + "/spot/order_book?currency_pair=" + gateSymbol + "&limit=" + limit;
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
            throw new RuntimeException("获取Gate市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertSymbol(String pairName) {
        // BTC/USDT -> BTC_USDT (Gate.io使用下划线)
        return pairName.replace("/", "_");
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


