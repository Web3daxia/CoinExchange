/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.CurrencyExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 币种汇率Service接口
 */
public interface CurrencyExchangeRateService {
    
    /**
     * 创建或更新币种汇率（基于CNY或USD汇率自动计算其他法币汇率）
     */
    void createOrUpdateExchangeRates(Long currencyId, BigDecimal cnyRate, BigDecimal usdRate);
    
    /**
     * 获取币种的所有汇率
     */
    List<CurrencyExchangeRate> getExchangeRates(Long currencyId);
    
    /**
     * 获取币种相对于指定法币的汇率
     */
    BigDecimal getExchangeRate(Long currencyId, String baseCurrency);
    
    /**
     * 获取币种的所有汇率（Map格式：baseCurrency -> rate）
     */
    Map<String, BigDecimal> getExchangeRateMap(Long currencyId);
    
    /**
     * 删除币种的所有汇率
     */
    void deleteExchangeRates(Long currencyId);
    
    /**
     * 更新汇率（自动从外部API获取最新汇率）
     */
    void updateExchangeRatesFromAPI(Long currencyId);
}














