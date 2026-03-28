/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MarketDataAdapter {
    /**
     * 获取24小时统计数据
     */
    Map<String, Object> get24hrTicker(String symbol);

    /**
     * 获取K线数据
     */
    List<Map<String, Object>> getKlines(String symbol, String interval, Integer limit);

    /**
     * 获取市场深度
     */
    Map<String, Object> getDepth(String symbol, Integer limit);

    /**
     * 转换交易对符号（BTC/USDT -> BTCUSDT）
     */
    String convertSymbol(String pairName);
}















