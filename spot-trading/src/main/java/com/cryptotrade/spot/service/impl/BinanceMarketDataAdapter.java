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
public class BinanceMarketDataAdapter implements MarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BINANCE_API_BASE = "https://api.binance.com/api/v3";

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            String url = BINANCE_API_BASE + "/ticker/24hr?symbol=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> result = new HashMap<>();
            result.put("currentPrice", new BigDecimal(data.get("lastPrice").toString()));
            result.put("priceChange24h", new BigDecimal(data.get("priceChangePercent").toString()));
            result.put("volume24h", new BigDecimal(data.get("volume").toString()));
            result.put("high24h", new BigDecimal(data.get("highPrice").toString()));
            result.put("low24h", new BigDecimal(data.get("lowPrice").toString()));
            result.put("bidPrice", new BigDecimal(data.get("bidPrice").toString()));
            result.put("askPrice", new BigDecimal(data.get("askPrice").toString()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlines(String symbol, String interval, Integer limit) {
        try {
            String binanceInterval = convertInterval(interval);
            String url = BINANCE_API_BASE + "/klines?symbol=" + symbol + "&interval=" + binanceInterval + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            List<List<Object>> klines = objectMapper.readValue(response, new TypeReference<List<List<Object>>>() {});
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (List<Object> kline : klines) {
                Map<String, Object> item = new HashMap<>();
                // Binance K线数据格式: [openTime, open, high, low, close, volume, ...]
                item.put("openTime", kline.get(0));
                item.put("open", new BigDecimal(kline.get(1).toString()));
                item.put("high", new BigDecimal(kline.get(2).toString()));
                item.put("low", new BigDecimal(kline.get(3).toString()));
                item.put("close", new BigDecimal(kline.get(4).toString()));
                item.put("volume", new BigDecimal(kline.get(5).toString()));
                result.add(item);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            String url = BINANCE_API_BASE + "/depth?symbol=" + symbol + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> result = new HashMap<>();
            result.put("bids", data.get("bids"));
            result.put("asks", data.get("asks"));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertSymbol(String pairName) {
        // BTC/USDT -> BTCUSDT
        return pairName.replace("/", "");
    }

    private String convertInterval(String interval) {
        // 转换时间粒度: 30s, 1m, 5m, 10m, 15m, 30m, 1h, 2h, 3h, 4h, 8h, 12h, 1d, 2d, 1w, 1M
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
        intervalMap.put("1w", "1w");
        intervalMap.put("1M", "1M");
        return intervalMap.getOrDefault(interval, "1h");
    }
}


