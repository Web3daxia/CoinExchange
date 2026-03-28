/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

import com.cryptotrade.basicinfo.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 汇率服务接口
 */
public interface ExchangeRateService {
    /**
     * 获取汇率
     * @param fromCurrency 源货币
     * @param toCurrency 目标货币
     * @return 汇率
     */
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);

    /**
     * 获取所有支持的汇率
     * @param baseCurrency 基础货币（可选）
     * @return 汇率列表
     */
    List<Map<String, Object>> getAllExchangeRates(String baseCurrency);

    /**
     * 更新汇率（定时任务调用）
     * @param fromCurrency 源货币
     * @param toCurrency 目标货币
     * @param rate 汇率
     * @param source 数据源
     */
    void updateExchangeRate(String fromCurrency, String toCurrency, BigDecimal rate, String source);

    /**
     * 转换货币金额
     * @param amount 金额
     * @param fromCurrency 源货币
     * @param toCurrency 目标货币
     * @return 转换后的金额
     */
    BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency);
}















