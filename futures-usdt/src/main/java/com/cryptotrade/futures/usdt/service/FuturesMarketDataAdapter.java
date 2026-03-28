/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 永续合约市场数据适配器接口
 */
public interface FuturesMarketDataAdapter {
    /**
     * 转换交易对符号（如 BTC/USDT -> BTCUSDT）
     */
    String convertSymbol(String pairName);

    /**
     * 获取24小时行情数据
     */
    Map<String, Object> get24hrTicker(String symbol);

    /**
     * 获取市场深度
     */
    Map<String, Object> getDepth(String symbol, Integer limit);

    /**
     * 获取K线数据
     */
    List<Map<String, Object>> getKlineData(String symbol, String interval, Integer limit);

    /**
     * 获取标记价格和指数价格（永续合约特有）
     */
    Map<String, Object> getMarkPriceAndIndexPrice(String symbol);

    /**
     * 获取资金费率（永续合约特有）
     */
    BigDecimal getFundingRate(String symbol);
}

