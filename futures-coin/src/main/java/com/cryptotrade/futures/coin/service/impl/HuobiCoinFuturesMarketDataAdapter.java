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
 * 火币币本位永续合约市场数据适配器
 * API文档: https://huobiapi.github.io/docs/dm/v1/cn/
 */
@Component
public class HuobiCoinFuturesMarketDataAdapter implements CoinFuturesMarketDataAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 火币币本位永续合约API基础地址
    private static final String HUOBI_COIN_FUTURES_API_BASE = "https://api.hbdm.com";

    @Override
    public String convertSymbol(String pairName) {
        // BTC/BTC -> BTC-USD (火币币本位永续合约使用连字符，合约代码为BTC-USD)
        String base = pairName.split("/")[0];
        return base + "-USD";
    }

    @Override
    public Map<String, Object> get24hrTicker(String symbol) {
        try {
            // 火币币本位永续合约24小时行情: GET /swap-ex/market/detail/merged?contract_code=BTC-USD
            String url = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/detail/merged?contract_code=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.BTC-USD.detail.merged","ts":1234567890,"tick":{...}}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                throw new RuntimeException("火币API返回状态错误: " + status);
            }
            
            Map<String, Object> tick = (Map<String, Object>) data.get("tick");
            if (tick == null) {
                throw new RuntimeException("火币API返回数据为空");
            }
            
            // 获取24小时统计数据
            String tickerUrl = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/detail?contract_code=" + symbol;
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
                BigDecimal amount = (BigDecimal) tickerTick.get("amount");
                
                result.put("high24h", high);
                result.put("low24h", low);
                result.put("volume24h", vol);
                result.put("amount24h", amount);
                
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
            throw new RuntimeException("获取火币币本位永续合约24小时统计数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDepth(String symbol, Integer limit) {
        try {
            // 火币币本位永续合约订单簿: GET /swap-ex/market/depth?contract_code=BTC-USD&type=step0
            String url = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/depth?contract_code=" + symbol + "&type=step0";
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.BTC-USD.depth.step0","ts":1234567890,"tick":{"bids":[[price,quantity],...],"asks":[[price,quantity],...]}}
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
            throw new RuntimeException("获取火币币本位永续合约市场深度失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getKlineData(String symbol, String interval, Integer limit) {
        try {
            String huobiInterval = convertInterval(interval);
            // 火币币本位永续合约K线数据: GET /swap-ex/market/history/kline?contract_code=BTC-USD&period=1min&size=100
            String url = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/history/kline?contract_code=" + symbol 
                    + "&period=" + huobiInterval + "&size=" + limit;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.BTC-USD.kline.1min","ts":1234567890,"data":[{...}]}
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
            throw new RuntimeException("获取火币币本位永续合约K线数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMarkPriceAndIndexPrice(String symbol) {
        try {
            // 火币币本位永续合约标记价格: GET /swap-ex/market/detail/merged?contract_code=BTC-USD
            String url = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/detail/merged?contract_code=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","ch":"market.BTC-USD.detail.merged","ts":1234567890,"tick":{...}}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                throw new RuntimeException("火币API返回状态错误: " + status);
            }
            
            Map<String, Object> tick = (Map<String, Object>) data.get("tick");
            if (tick == null) {
                throw new RuntimeException("火币API返回数据为空");
            }
            
            Map<String, Object> result = new HashMap<>();
            // 火币的标记价格在mark_price字段
            if (tick.get("mark_price") != null) {
                result.put("markPrice", new BigDecimal(tick.get("mark_price").toString()));
            }
            // 火币的指数价格在index_price字段
            if (tick.get("index_price") != null) {
                result.put("indexPrice", new BigDecimal(tick.get("index_price").toString()));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取火币币本位永续合约标记价格和指数价格失败: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getFundingRate(String symbol) {
        try {
            // 火币币本位永续合约资金费率: GET /swap-ex/market/batch_funding_rate?contract_code=BTC-USD
            String url = HUOBI_COIN_FUTURES_API_BASE + "/swap-ex/market/batch_funding_rate?contract_code=" + symbol;
            String response = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            
            // 火币返回格式: {"status":"ok","data":[{"contract_code":"BTC-USD","funding_rate":"0.0001",...}],...}
            String status = (String) data.get("status");
            if (!"ok".equals(status)) {
                return BigDecimal.ZERO;
            }
            
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data.get("data");
            if (dataList == null || dataList.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            Map<String, Object> fundingData = dataList.get(0);
            if (fundingData.get("funding_rate") != null) {
                return new BigDecimal(fundingData.get("funding_rate").toString());
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("获取火币币本位永续合约资金费率失败: " + e.getMessage(), e);
        }
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

