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
public class OKXMarketDataAdapter implements MarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String OKX_API_BASE = "https://www.okx.com/api/v5";

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // OKX API: GET /api/v5/market/ticker?instId=BTC-USDT
            String okxSymbol = convertSymbol(symbol);
            String url = OKX_API_BASE + "/market/ticker?instId=" + okxSymbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // OKX返回格式: {"code":"0","data":[{"instId":"BTC-USDT","last":"50000",...}],...}
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data.get("data");
            if (dataList == null || dataList.isEmpty()) {
                throw new RuntimeException("OKX API返回数据为空");
            }
            
            Map<String, Object> ticker = dataList.get(0);
            
            Map<String, Object> result = new HashMap<>();
            result.put("currentPrice", new BigDecimal(ticker.get("last").toString()));
            result.put("priceChange24h", new BigDecimal(ticker.get("changePct").toString())
                    .multiply(new BigDecimal("100"))); // OKX返回的是小数，转换为百分比
            result.put("volume24h", new BigDecimal(ticker.get("vol24h").toString()));
            result.put("high24h", new BigDecimal(ticker.get("high24h").toString()));
            result.put("low24h", new BigDecimal(ticker.get("low24h").toString()));
            result.put("bidPrice", new BigDecimal(ticker.get("bidPx").toString()));
            result.put("askPrice", new BigDecimal(ticker.get("askPx").toString()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取OKX 24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlines(String symbol, String interval, Integer limit) {
        try {
            String okxSymbol = convertSymbol(symbol);
            String okxInterval = convertInterval(interval);
            String url = OKX_API_BASE + "/market/candles?instId=" + okxSymbol 
                    + "&bar=" + okxInterval + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // OKX K线数据格式: {"code":"0","data":[["1698744060000","50000","50100","49900","50050","100.5"],...]}
            List<List<String>> klines = (List<List<String>>) data.get("data");
            if (klines == null) {
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (List<String> kline : klines) {
                // OKX K线数据格式: [timestamp, open, high, low, close, volume]
                Map<String, Object> item = new HashMap<>();
                item.put("openTime", Long.parseLong(kline.get(0)));
                item.put("open", new BigDecimal(kline.get(1)));
                item.put("high", new BigDecimal(kline.get(2)));
                item.put("low", new BigDecimal(kline.get(3)));
                item.put("close", new BigDecimal(kline.get(4)));
                item.put("volume", new BigDecimal(kline.get(5)));
                result.add(item);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取OKX K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            String okxSymbol = convertSymbol(symbol);
            String url = OKX_API_BASE + "/market/books?instId=" + okxSymbol + "&sz=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // OKX返回格式: {"code":"0","data":[{"asks":[[price,quantity],...],"bids":[[price,quantity],...]}],...}
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data.get("data");
            if (dataList == null || dataList.isEmpty()) {
                throw new RuntimeException("OKX API返回数据为空");
            }
            
            Map<String, Object> bookData = dataList.get(0);
            
            // 转换格式为统一格式
            List<List<String>> asks = (List<List<String>>) bookData.get("asks");
            List<List<String>> bids = (List<List<String>>) bookData.get("bids");
            
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
            throw new RuntimeException("获取OKX市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertSymbol(String pairName) {
        // BTC/USDT -> BTC-USDT (OKX使用连字符)
        return pairName.replace("/", "-");
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
        intervalMap.put("1h", "1H");
        intervalMap.put("2h", "2H");
        intervalMap.put("3h", "3H");
        intervalMap.put("4h", "4H");
        intervalMap.put("8h", "8H");
        intervalMap.put("12h", "12H");
        intervalMap.put("1d", "1D");
        intervalMap.put("2d", "2D");
        intervalMap.put("1w", "1W");
        intervalMap.put("1M", "1M");
        return intervalMap.getOrDefault(interval, "1H");
    }
}


