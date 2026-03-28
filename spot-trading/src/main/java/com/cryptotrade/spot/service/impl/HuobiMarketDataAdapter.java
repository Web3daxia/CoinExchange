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
public class HuobiMarketDataAdapter implements MarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String HUOBI_API_BASE = "https://api.huobi.pro/market";

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // 火币API: GET /market/detail/merged?symbol=btcusdt
            String huobiSymbol = convertSymbol(symbol).toLowerCase();
            String url = HUOBI_API_BASE + "/detail/merged?symbol=" + huobiSymbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.btcusdt.detail.merged","ts":1234567890,"tick":{...}}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                throw new RuntimeException("火币API返回状态错误: " + status);
            }
            
            Map<String, Object> tick = (Map<String, Object>) data.get("tick");
            if (tick == null) {
                throw new RuntimeException("火币API返回数据为空");
            }
            
            // 获取24小时统计数据
            String tickerUrl = HUOBI_API_BASE + "/detail?symbol=" + huobiSymbol;
            String tickerResponse = restTemplate.getForObject(tickerUrl, String.class);
            Map<String, Object> tickerData = objectMapper.readValue(tickerResponse, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> tickerTick = (Map<String, Object>) tickerData.get("tick");
            
            Map<String, Object> result = new HashMap<>();
            List<BigDecimal> bid = (List<BigDecimal>) tick.get("bid");
            List<BigDecimal> ask = (List<BigDecimal>) tick.get("ask");
            result.put("currentPrice", (BigDecimal) tick.get("close"));
            result.put("bidPrice", bid != null && !bid.isEmpty() ? bid.get(0) : BigDecimal.ZERO);
            result.put("askPrice", ask != null && !ask.isEmpty() ? ask.get(0) : BigDecimal.ZERO);
            
            if (tickerTick != null) {
                BigDecimal open = (BigDecimal) tickerTick.get("open");
                BigDecimal close = (BigDecimal) tickerTick.get("close");
                BigDecimal high = (BigDecimal) tickerTick.get("high");
                BigDecimal low = (BigDecimal) tickerTick.get("low");
                BigDecimal vol = (BigDecimal) tickerTick.get("vol");
                
                result.put("high24h", high);
                result.put("low24h", low);
                result.put("volume24h", vol);
                
                if (open != null && open.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal changePercent = close.subtract(open)
                            .divide(open, 4, java.math.RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    result.put("priceChange24h", changePercent);
                } else {
                    result.put("priceChange24h", BigDecimal.ZERO);
                }
            }
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取火币24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlines(String symbol, String interval, Integer limit) {
        try {
            String huobiSymbol = convertSymbol(symbol).toLowerCase();
            String huobiInterval = convertInterval(interval);
            String url = HUOBI_API_BASE + "/history/kline?symbol=" + huobiSymbol 
                    + "&period=" + huobiInterval + "&size=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.btcusdt.kline.1min","ts":1234567890,"data":[{...}]}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                throw new RuntimeException("火币API返回状态错误: " + status);
            }
            
            List<Map<String, Object>> klines = (List<Map<String, Object>>) data.get("data");
            if (klines == null) {
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> kline : klines) {
                // 火币K线数据格式: {"id":1234567890,"open":50000,"close":50050,"low":49900,"high":50100,"vol":100.5,...}
                Map<String, Object> item = new HashMap<>();
                Long id = Long.parseLong(kline.get("id").toString());
                item.put("openTime", id * 1000); // 火币返回的是秒，转换为毫秒
                item.put("open", new BigDecimal(kline.get("open").toString()));
                item.put("high", new BigDecimal(kline.get("high").toString()));
                item.put("low", new BigDecimal(kline.get("low").toString()));
                item.put("close", new BigDecimal(kline.get("close").toString()));
                item.put("volume", new BigDecimal(kline.get("vol").toString()));
                result.add(item);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取火币K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            String huobiSymbol = convertSymbol(symbol).toLowerCase();
            String url = HUOBI_API_BASE + "/depth?symbol=" + huobiSymbol + "&type=step0";
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.btcusdt.depth.step0","ts":1234567890,"tick":{"bids":[[price,quantity],...],"asks":[[price,quantity],...]}}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                throw new RuntimeException("火币API返回状态错误: " + status);
            }
            
            Map<String, Object> tick = (Map<String, Object>) data.get("tick");
            if (tick == null) {
                throw new RuntimeException("火币API返回数据为空");
            }
            
            List<List<BigDecimal>> asks = (List<List<BigDecimal>>) tick.get("asks");
            List<List<BigDecimal>> bids = (List<List<BigDecimal>>) tick.get("bids");
            
            List<Map<String, Object>> askList = new ArrayList<>();
            if (asks != null) {
                int count = Math.min(asks.size(), limit);
                for (int i = 0; i < count; i++) {
                    List<BigDecimal> ask = asks.get(i);
                    Map<String, Object> item = new HashMap<>();
                    item.put("price", ask.get(0));
                    item.put("quantity", ask.get(1));
                    askList.add(item);
                }
            }
            
            List<Map<String, Object>> bidList = new ArrayList<>();
            if (bids != null) {
                int count = Math.min(bids.size(), limit);
                for (int i = 0; i < count; i++) {
                    List<BigDecimal> bid = bids.get(i);
                    Map<String, Object> item = new HashMap<>();
                    item.put("price", bid.get(0));
                    item.put("quantity", bid.get(1));
                    bidList.add(item);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("bids", bidList);
            result.put("asks", askList);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取火币市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertSymbol(String pairName) {
        // BTC/USDT -> btcusdt (火币使用小写，无分隔符)
        return pairName.replace("/", "").toLowerCase();
    }

    private String convertInterval(String interval) {
        // 转换时间粒度
        Map<String, String> intervalMap = new HashMap<>();
        intervalMap.put("30s", "30");
        intervalMap.put("1m", "1min");
        intervalMap.put("5m", "5min");
        intervalMap.put("10m", "10min");
        intervalMap.put("15m", "15min");
        intervalMap.put("30m", "30min");
        intervalMap.put("1h", "60min");
        intervalMap.put("2h", "2hour");
        intervalMap.put("3h", "3hour");
        intervalMap.put("4h", "4hour");
        intervalMap.put("8h", "8hour");
        intervalMap.put("12h", "12hour");
        intervalMap.put("1d", "1day");
        intervalMap.put("2d", "2day");
        intervalMap.put("1w", "1week");
        intervalMap.put("1M", "1mon");
        return intervalMap.getOrDefault(interval, "60min");
    }
}


