/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.CurrencyCreateRequest;
import com.cryptotrade.systemmanagement.dto.request.CurrencyUpdateRequest;
import com.cryptotrade.systemmanagement.entity.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {
    /**
     * 创建币种（使用新的DTO）
     */
    Currency createCurrency(CurrencyCreateRequest request);
    
    /**
     * 更新币种（使用新的DTO）
     */
    Currency updateCurrency(Long currencyId, CurrencyUpdateRequest request);
    
    /**
     * 创建币种（旧方法，保持兼容性）
     */
    @Deprecated
    Currency createCurrency(String currencyCode, String currencyName, String symbol, 
                           Integer decimals, Boolean spotEnabled, Boolean futuresUsdtEnabled,
                           Boolean futuresCoinEnabled, Boolean optionsEnabled, Boolean leveragedEnabled);
    
    /**
     * 更新币种（旧方法，保持兼容性）
     */
    @Deprecated
    Currency updateCurrency(Long currencyId, String currencyName, String symbol, String iconUrl,
                           Integer decimals, BigDecimal minWithdrawAmount, BigDecimal maxWithdrawAmount,
                           BigDecimal withdrawFee, Boolean depositEnabled, Boolean withdrawEnabled,
                           Boolean spotEnabled, Boolean futuresUsdtEnabled, Boolean futuresCoinEnabled,
                           Boolean optionsEnabled, Boolean leveragedEnabled, String status, Integer sortOrder,
                           String description);
    
    void deleteCurrency(Long currencyId);
    
    Currency getCurrencyById(Long currencyId);
    
    Currency getCurrencyByCode(String currencyCode);
    
    List<Currency> getAllCurrencies();
    
    List<Currency> getActiveCurrencies();
    
    List<Currency> getCurrenciesBySpotEnabled();
    
    List<Currency> getCurrenciesByFuturesUsdtEnabled();
    
    List<Currency> getCurrenciesByFuturesCoinEnabled();
    
    List<Currency> getCurrenciesByOptionsEnabled();
    
    List<Currency> getCurrenciesByLeveragedEnabled();
}
