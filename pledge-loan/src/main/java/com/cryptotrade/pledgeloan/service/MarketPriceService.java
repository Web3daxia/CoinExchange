/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import java.math.BigDecimal;

/**
 * 市场价格服务接口
 * 用于获取加密货币的实时价格
 */
public interface MarketPriceService {
    
    /**
     * 获取当前价格（USDT计价）
     * @param currency 币种代码，如: BTC, ETH, USDT
     * @return 当前价格（USDT）
     */
    BigDecimal getCurrentPrice(String currency);
    
    /**
     * 批量获取价格
     * @param currencies 币种代码数组
     * @return 价格Map，key为币种代码，value为价格
     */
    java.util.Map<String, BigDecimal> getCurrentPrices(String... currencies);
}














