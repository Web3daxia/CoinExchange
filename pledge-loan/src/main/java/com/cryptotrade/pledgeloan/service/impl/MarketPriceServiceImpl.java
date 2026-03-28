/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.service.MarketPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 市场价格服务实现
 * TODO: 实际应该从市场数据服务获取实时价格
 */
@Service
public class MarketPriceServiceImpl implements MarketPriceService {

    // 模拟价格数据（实际应该从市场数据服务获取）
    private static final Map<String, BigDecimal> DEFAULT_PRICES = new HashMap<>();
    
    static {
        DEFAULT_PRICES.put("BTC", BigDecimal.valueOf(50000));
        DEFAULT_PRICES.put("ETH", BigDecimal.valueOf(3000));
        DEFAULT_PRICES.put("USDT", BigDecimal.ONE);
        DEFAULT_PRICES.put("BNB", BigDecimal.valueOf(400));
        DEFAULT_PRICES.put("LTC", BigDecimal.valueOf(150));
        DEFAULT_PRICES.put("BCH", BigDecimal.valueOf(300));
    }

    @Override
    public BigDecimal getCurrentPrice(String currency) {
        // TODO: 从市场数据服务获取实时价格
        // 这里使用模拟数据
        return DEFAULT_PRICES.getOrDefault(currency.toUpperCase(), BigDecimal.valueOf(100));
    }

    @Override
    public Map<String, BigDecimal> getCurrentPrices(String... currencies) {
        Map<String, BigDecimal> prices = new HashMap<>();
        for (String currency : currencies) {
            prices.put(currency, getCurrentPrice(currency));
        }
        return prices;
    }
}














