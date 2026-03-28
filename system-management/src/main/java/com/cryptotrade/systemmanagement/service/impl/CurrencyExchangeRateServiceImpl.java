/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.Currency;
import com.cryptotrade.systemmanagement.entity.CurrencyExchangeRate;
import com.cryptotrade.systemmanagement.repository.CurrencyExchangeRateRepository;
import com.cryptotrade.systemmanagement.repository.CurrencyRepository;
import com.cryptotrade.systemmanagement.service.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 币种汇率Service实现
 * 支持基于CNY/USD汇率自动计算其他法币汇率
 */
@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    @Autowired
    private CurrencyExchangeRateRepository exchangeRateRepository;
    
    @Autowired
    private CurrencyRepository currencyRepository;

    // 法币列表（相对于USD的基准汇率，这些值应该从外部API获取或配置）
    private static final Map<String, BigDecimal> BASE_CURRENCY_RATES_TO_USD = new HashMap<>();
    
    static {
        // 初始化法币相对于USD的基准汇率（示例数据，实际应该从外部API获取）
        BASE_CURRENCY_RATES_TO_USD.put("USD", BigDecimal.ONE);
        BASE_CURRENCY_RATES_TO_USD.put("CNY", new BigDecimal("7.2")); // 1 USD = 7.2 CNY（示例）
        BASE_CURRENCY_RATES_TO_USD.put("EUR", new BigDecimal("0.92")); // 1 USD = 0.92 EUR
        BASE_CURRENCY_RATES_TO_USD.put("JPY", new BigDecimal("150")); // 1 USD = 150 JPY
        BASE_CURRENCY_RATES_TO_USD.put("KRW", new BigDecimal("1300")); // 1 USD = 1300 KRW
        BASE_CURRENCY_RATES_TO_USD.put("GBP", new BigDecimal("0.79")); // 1 USD = 0.79 GBP
        BASE_CURRENCY_RATES_TO_USD.put("HKD", new BigDecimal("7.8")); // 1 USD = 7.8 HKD
        BASE_CURRENCY_RATES_TO_USD.put("SGD", new BigDecimal("1.34")); // 1 USD = 1.34 SGD
        BASE_CURRENCY_RATES_TO_USD.put("AUD", new BigDecimal("1.52")); // 1 USD = 1.52 AUD
        BASE_CURRENCY_RATES_TO_USD.put("CAD", new BigDecimal("1.36")); // 1 USD = 1.36 CAD
        BASE_CURRENCY_RATES_TO_USD.put("CHF", new BigDecimal("0.92")); // 1 USD = 0.92 CHF
        BASE_CURRENCY_RATES_TO_USD.put("INR", new BigDecimal("83")); // 1 USD = 83 INR
        BASE_CURRENCY_RATES_TO_USD.put("IDR", new BigDecimal("15600")); // 1 USD = 15600 IDR
        BASE_CURRENCY_RATES_TO_USD.put("MYR", new BigDecimal("4.7")); // 1 USD = 4.7 MYR
        BASE_CURRENCY_RATES_TO_USD.put("PHP", new BigDecimal("56")); // 1 USD = 56 PHP
        BASE_CURRENCY_RATES_TO_USD.put("THB", new BigDecimal("36")); // 1 USD = 36 THB
    }

    @Override
    @Transactional
    public void createOrUpdateExchangeRates(Long currencyId, BigDecimal cnyRate, BigDecimal usdRate) {
        // 删除已存在的汇率
        exchangeRateRepository.deleteByCurrencyId(currencyId);

        // 获取币种代码（用于target_currency字段）
        String currencyCode = getCurrencyCode(currencyId);

        // 如果提供了USD汇率，优先使用USD作为基准
        if (usdRate != null && usdRate.compareTo(BigDecimal.ZERO) > 0) {
            // 基于USD汇率计算其他法币汇率
            for (Map.Entry<String, BigDecimal> entry : BASE_CURRENCY_RATES_TO_USD.entrySet()) {
                String baseCurrency = entry.getKey();
                BigDecimal baseRateToUsd = entry.getValue();
                
                CurrencyExchangeRate rate = new CurrencyExchangeRate();
                rate.setCurrencyId(currencyId);
                rate.setBaseCurrency(baseCurrency);
                rate.setTargetCurrency(currencyCode);
                
                if ("USD".equals(baseCurrency)) {
                    rate.setExchangeRate(usdRate);
                } else {
                    // 计算：1 币种 = usdRate USD = usdRate * baseRateToUsd baseCurrency
                    BigDecimal calculatedRate = usdRate.multiply(baseRateToUsd).setScale(8, RoundingMode.HALF_UP);
                    rate.setExchangeRate(calculatedRate);
                }
                
                exchangeRateRepository.save(rate);
            }
        } else if (cnyRate != null && cnyRate.compareTo(BigDecimal.ZERO) > 0) {
            // 如果只提供了CNY汇率，先转换为USD汇率，再计算其他法币汇率
            BigDecimal cnyToUsd = BASE_CURRENCY_RATES_TO_USD.get("CNY");
            BigDecimal usdRateFromCny = cnyRate.divide(cnyToUsd, 8, RoundingMode.HALF_UP);
            
            // 使用计算出的USD汇率
            createOrUpdateExchangeRates(currencyId, cnyRate, usdRateFromCny);
        }
    }

    @Override
    public List<CurrencyExchangeRate> getExchangeRates(Long currencyId) {
        return exchangeRateRepository.findByCurrencyId(currencyId);
    }

    @Override
    public BigDecimal getExchangeRate(Long currencyId, String baseCurrency) {
        String currencyCode = getCurrencyCode(currencyId);
        Optional<CurrencyExchangeRate> rate = exchangeRateRepository
                .findByCurrencyIdAndBaseCurrencyAndTargetCurrency(currencyId, baseCurrency, currencyCode);
        return rate.map(CurrencyExchangeRate::getExchangeRate).orElse(null);
    }

    @Override
    public Map<String, BigDecimal> getExchangeRateMap(Long currencyId) {
        List<CurrencyExchangeRate> rates = getExchangeRates(currencyId);
        Map<String, BigDecimal> rateMap = new HashMap<>();
        for (CurrencyExchangeRate rate : rates) {
            rateMap.put(rate.getBaseCurrency(), rate.getExchangeRate());
        }
        return rateMap;
    }

    @Override
    @Transactional
    public void deleteExchangeRates(Long currencyId) {
        exchangeRateRepository.deleteByCurrencyId(currencyId);
    }

    @Override
    @Transactional
    public void updateExchangeRatesFromAPI(Long currencyId) {
        // TODO: 从外部API获取最新汇率
        // 示例：可以调用CoinGecko API、CoinMarketCap API等获取实时汇率
        
        // 临时实现：获取现有的USD或CNY汇率，然后重新计算
        BigDecimal usdRate = getExchangeRate(currencyId, "USD");
        BigDecimal cnyRate = getExchangeRate(currencyId, "CNY");
        
        if (usdRate != null || cnyRate != null) {
            createOrUpdateExchangeRates(currencyId, cnyRate, usdRate);
        }
        
        // 实际实现示例（伪代码）：
        // BigDecimal latestUsdRate = externalApiService.getLatestRate(currencyCode, "USD");
        // BigDecimal latestCnyRate = externalApiService.getLatestRate(currencyCode, "CNY");
        // createOrUpdateExchangeRates(currencyId, latestCnyRate, latestUsdRate);
    }

    /**
     * 获取币种代码
     */
    private String getCurrencyCode(Long currencyId) {
        return currencyRepository.findById(currencyId)
                .map(Currency::getCurrencyCode)
                .orElse("UNKNOWN");
    }
}

