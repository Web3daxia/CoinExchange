/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.CurrencyName;

import java.util.List;
import java.util.Map;

/**
 * 币种多语言名称Service接口
 */
public interface CurrencyNameService {
    
    /**
     * 为币种创建多语言名称（自动翻译）
     */
    void createCurrencyNames(Long currencyId, String defaultName, String sourceLanguage);
    
    /**
     * 更新币种的多语言名称（自动翻译）
     */
    void updateCurrencyNames(Long currencyId, String newName, String sourceLanguage);
    
    /**
     * 获取币种的所有语言名称
     */
    List<CurrencyName> getCurrencyNames(Long currencyId);
    
    /**
     * 获取币种的指定语言名称
     */
    String getCurrencyName(Long currencyId, String languageCode);
    
    /**
     * 获取币种的所有语言名称（Map格式：languageCode -> name）
     */
    Map<String, String> getCurrencyNameMap(Long currencyId);
    
    /**
     * 删除币种的所有语言名称
     */
    void deleteCurrencyNames(Long currencyId);
}














