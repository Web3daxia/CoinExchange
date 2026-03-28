/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.service.FuturesMarketDataAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币安永续合约市场数据适配器
 * API文档: https://binance-docs.github.io/apidocs/futures/cn/
 */
@Component
public class BinanceFuturesMarketDataAdapter implements FuturesMarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 币安永续合约API基础地址
    private static final String BINANCE_FUTURES_API_BASE = "https://fapi.binance.com/fapi/v1";

    @Override
    public String convertSymbol(String pairName) {
        // BTC/USDT -> BTCUSDT (币安永续合约使用USDT保证金，符号格式与现货相同)
        return pairName.replace("/", "");
    }

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // 币安永续合约24小时价格变动统计: GET /fapi/v1/ticker/24hr
            String url = BINANCE_FUTURES_API_BASE + "/ticker/24hr?symbol=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> result = new HashMap<>();
            result.put("currentPrice", new BigDecimal(data.get("lastPrice").toString()));
            result.put("priceChange24h", new BigDecimal(data.get("priceChangePercent").toString()));
            result.put("volume24h", new BigDecimal(data.get("volume").toString()));
            result.put("amount24h", new BigDecimal(data.get("quoteVolume").toString()));
            result.put("high24h", new BigDecimal(data.get("highPrice").toString()));
            result.put("low24h", new BigDecimal(data.get("lowPrice").toString()));
            result.put("bidPrice", new BigDecimal(data.get("bidPrice").toString()));
            result.put("askPrice", new BigDecimal(data.get("askPrice").toString()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安永续合约24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            // 币安永续合约订单簿: GET /fapi/v1/depth
            String url = BINANCE_FUTURES_API_BASE + "/depth?symbol=" + symbol + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 转换格式为统一格式
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
            throw new RuntimeException("获取币安永续合约市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlineData(String symbol, String interval, Integer limit) {
        try {
            String binanceInterval = convertInterval(interval);
            // 币安永续合约K线数据: GET /fapi/v1/klines
            String url = BINANCE_FUTURES_API_BASE + "/klines?symbol=" + symbol + "&interval=" + binanceInterval + "&limit=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            List<List<Object>> klines = objectMapper.readValue(response, new TypeReference<List<List<Object>>>() {});
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (List<Object> kline : klines) {
                // 币安永续合约K线数据格式: [openTime, open, high, low, close, volume, closeTime, ...]
                Map<String, Object> item = new HashMap<>();
                item.put("openTime", kline.get(0));
                item.put("open", new BigDecimal(kline.get(1).toString()));
                item.put("high", new BigDecimal(kline.get(2).toString()));
                item.put("low", new BigDecimal(kline.get(3).toString()));
                item.put("close", new BigDecimal(kline.get(4).toString()));
                item.put("volume", new BigDecimal(kline.get(5).toString()));
                item.put("closeTime", kline.get(6));
                result.add(item);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安永续合约K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMarkPriceAndIndexPrice(String symbol) {
        try {
            // 币安永续合约标记价格和指数价格: GET /fapi/v1/premiumIndex
            String url = BINANCE_FUTURES_API_BASE + "/premiumIndex?symbol=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            Map<String, Object> result = new HashMap<>();
            result.put("markPrice", new BigDecimal(data.get("markPrice").toString()));
            result.put("indexPrice", new BigDecimal(data.get("indexPrice").toString()));
            result.put("estimatedSettlePrice", data.get("estimatedSettlePrice") != null ? 
                    new BigDecimal(data.get("estimatedSettlePrice").toString()) : null);
            result.put("lastFundingRate", data.get("lastFundingRate") != null ? 
                    new BigDecimal(data.get("lastFundingRate").toString()) : null);
            // 下次资金费率时间（毫秒时间戳）
            if (data.get("nextFundingTime") != null) {
                long nextFundingTime = Long.parseLong(data.get("nextFundingTime").toString());
                result.put("nextFundingTime", LocalDateTime.ofEpochSecond(nextFundingTime / 1000, 0, ZoneOffset.UTC));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取币安永续合约标记价格和指数价格失败: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getFundingRate(String symbol) {
        try {
            // 币安永续合约当前资金费率: GET /fapi/v1/premiumIndex
            String url = BINANCE_FUTURES_API_BASE + "/premiumIndex?symbol=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 币安返回的是lastFundingRate（上次资金费率），通常与当前资金费率相同
            if (data.get("lastFundingRate") != null) {
                return new BigDecimal(data.get("lastFundingRate").toString());
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("获取币安永续合约资金费率失败: " + e.getMessage(), e);
        }
    }

    private String convertInterval(String interval) {
        // 转换时间粒度: 币安永续合约支持的时间粒度
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















