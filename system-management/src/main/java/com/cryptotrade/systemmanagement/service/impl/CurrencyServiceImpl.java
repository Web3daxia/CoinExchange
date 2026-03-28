/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.CurrencyCreateRequest;
import com.cryptotrade.systemmanagement.dto.request.CurrencyUpdateRequest;
import com.cryptotrade.systemmanagement.entity.Currency;
import com.cryptotrade.systemmanagement.entity.CurrencyCategoryRelation;
import com.cryptotrade.systemmanagement.repository.CurrencyCategoryRelationRepository;
import com.cryptotrade.systemmanagement.repository.CurrencyRepository;
import com.cryptotrade.systemmanagement.service.CurrencyExchangeRateService;
import com.cryptotrade.systemmanagement.service.CurrencyNameService;
import com.cryptotrade.systemmanagement.service.CurrencyService;
import com.cryptotrade.systemmanagement.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    
    @Autowired
    private CurrencyRepository currencyRepository;
    
    @Autowired
    private CurrencyNameService currencyNameService;
    
    @Autowired
    private CurrencyExchangeRateService exchangeRateService;
    
    @Autowired
    private CurrencyCategoryRelationRepository categoryRelationRepository;
    
    @Override
    @Transactional
    public Currency createCurrency(CurrencyCreateRequest request) {
        if (currencyRepository.existsByCurrencyCode(request.getCurrencyCode())) {
            throw new RuntimeException("币种代码已存在: " + request.getCurrencyCode());
        }
        
        Currency currency = new Currency();
        currency.setCurrencyCode(request.getCurrencyCode());
        currency.setCurrencyName(request.getCurrencyName());
        currency.setSymbol(request.getSymbol());
        currency.setIconUrl(request.getIconUrl());
        currency.setDecimals(request.getDecimals() != null ? request.getDecimals() : 8);
        currency.setMinWithdrawAmount(request.getMinWithdrawAmount());
        currency.setMaxWithdrawAmount(request.getMaxWithdrawAmount());
        currency.setWithdrawFee(request.getWithdrawFee());
        currency.setDepositEnabled(request.getDepositEnabled() != null ? request.getDepositEnabled() : true);
        currency.setWithdrawEnabled(request.getWithdrawEnabled() != null ? request.getWithdrawEnabled() : true);
        currency.setSpotEnabled(request.getSpotEnabled() != null ? request.getSpotEnabled() : false);
        currency.setFuturesUsdtEnabled(request.getFuturesUsdtEnabled() != null ? request.getFuturesUsdtEnabled() : false);
        currency.setFuturesCoinEnabled(request.getFuturesCoinEnabled() != null ? request.getFuturesCoinEnabled() : false);
        currency.setOptionsEnabled(request.getOptionsEnabled() != null ? request.getOptionsEnabled() : false);
        currency.setLeveragedEnabled(request.getLeveragedEnabled() != null ? request.getLeveragedEnabled() : false);
        currency.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        currency.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        currency.setDescription(request.getDescription());
        
        // 新增字段
        currency.setAgentId(request.getAgentId());
        currency.setCurrencyUnit(request.getCurrencyUnit());
        currency.setTotalSupply(request.getTotalSupply());
        currency.setDetailUrl(request.getDetailUrl());
        currency.setListingDate(request.getListingDate());
        currency.setLogoUrl(request.getLogoUrl());
        currency.setLogoFilePath(request.getLogoFilePath());
        currency.setIntro(request.getIntro());
        currency.setBaseExchangeRateCny(request.getBaseExchangeRateCny());
        currency.setBaseExchangeRateUsd(request.getBaseExchangeRateUsd());
        
        // 保存币种
        Currency savedCurrency = currencyRepository.save(currency);
        
        // 创建多语言名称（默认使用英文作为源语言）
        if (request.getCurrencyName() != null) {
            currencyNameService.createCurrencyNames(savedCurrency.getId(), request.getCurrencyName(), "en-US");
        }
        
        // 创建汇率（如果提供了CNY或USD汇率）
        if (request.getBaseExchangeRateCny() != null || request.getBaseExchangeRateUsd() != null) {
            exchangeRateService.createOrUpdateExchangeRates(
                    savedCurrency.getId(),
                    request.getBaseExchangeRateCny(),
                    request.getBaseExchangeRateUsd()
            );
        }
        
        // 保存币种分类关联
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            for (Long categoryId : request.getCategoryIds()) {
                CurrencyCategoryRelation relation = new CurrencyCategoryRelation();
                relation.setCurrencyId(savedCurrency.getId());
                relation.setCategoryId(categoryId);
                categoryRelationRepository.save(relation);
            }
        }
        
        return savedCurrency;
    }
    
    @Override
    @Transactional
    public Currency updateCurrency(Long currencyId, CurrencyUpdateRequest request) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new RuntimeException("币种不存在: " + currencyId));
        
        // 更新基本字段
        if (request.getCurrencyName() != null) {
            currency.setCurrencyName(request.getCurrencyName());
            // 更新多语言名称
            currencyNameService.updateCurrencyNames(currencyId, request.getCurrencyName(), "en-US");
        }
        if (request.getSymbol() != null) {
            currency.setSymbol(request.getSymbol());
        }
        if (request.getIconUrl() != null) {
            currency.setIconUrl(request.getIconUrl());
        }
        if (request.getDecimals() != null) {
            currency.setDecimals(request.getDecimals());
        }
        if (request.getMinWithdrawAmount() != null) {
            currency.setMinWithdrawAmount(request.getMinWithdrawAmount());
        }
        if (request.getMaxWithdrawAmount() != null) {
            currency.setMaxWithdrawAmount(request.getMaxWithdrawAmount());
        }
        if (request.getWithdrawFee() != null) {
            currency.setWithdrawFee(request.getWithdrawFee());
        }
        if (request.getDepositEnabled() != null) {
            currency.setDepositEnabled(request.getDepositEnabled());
        }
        if (request.getWithdrawEnabled() != null) {
            currency.setWithdrawEnabled(request.getWithdrawEnabled());
        }
        if (request.getSpotEnabled() != null) {
            currency.setSpotEnabled(request.getSpotEnabled());
        }
        if (request.getFuturesUsdtEnabled() != null) {
            currency.setFuturesUsdtEnabled(request.getFuturesUsdtEnabled());
        }
        if (request.getFuturesCoinEnabled() != null) {
            currency.setFuturesCoinEnabled(request.getFuturesCoinEnabled());
        }
        if (request.getOptionsEnabled() != null) {
            currency.setOptionsEnabled(request.getOptionsEnabled());
        }
        if (request.getLeveragedEnabled() != null) {
            currency.setLeveragedEnabled(request.getLeveragedEnabled());
        }
        if (request.getStatus() != null) {
            currency.setStatus(request.getStatus());
        }
        if (request.getSortOrder() != null) {
            currency.setSortOrder(request.getSortOrder());
        }
        if (request.getDescription() != null) {
            currency.setDescription(request.getDescription());
        }
        
        // 更新新增字段
        if (request.getAgentId() != null) {
            currency.setAgentId(request.getAgentId());
        }
        if (request.getCurrencyUnit() != null) {
            currency.setCurrencyUnit(request.getCurrencyUnit());
        }
        if (request.getTotalSupply() != null) {
            currency.setTotalSupply(request.getTotalSupply());
        }
        if (request.getDetailUrl() != null) {
            currency.setDetailUrl(request.getDetailUrl());
        }
        if (request.getListingDate() != null) {
            currency.setListingDate(request.getListingDate());
        }
        if (request.getLogoUrl() != null) {
            currency.setLogoUrl(request.getLogoUrl());
        }
        if (request.getLogoFilePath() != null) {
            currency.setLogoFilePath(request.getLogoFilePath());
        }
        if (request.getIntro() != null) {
            currency.setIntro(request.getIntro());
        }
        if (request.getBaseExchangeRateCny() != null) {
            currency.setBaseExchangeRateCny(request.getBaseExchangeRateCny());
        }
        if (request.getBaseExchangeRateUsd() != null) {
            currency.setBaseExchangeRateUsd(request.getBaseExchangeRateUsd());
        }
        
        // 更新汇率
        if (request.getBaseExchangeRateCny() != null || request.getBaseExchangeRateUsd() != null) {
            exchangeRateService.createOrUpdateExchangeRates(
                    currencyId,
                    request.getBaseExchangeRateCny() != null ? request.getBaseExchangeRateCny() : currency.getBaseExchangeRateCny(),
                    request.getBaseExchangeRateUsd() != null ? request.getBaseExchangeRateUsd() : currency.getBaseExchangeRateUsd()
            );
        }
        
        // 更新分类关联
        if (request.getCategoryIds() != null) {
            // 删除旧的关联
            categoryRelationRepository.deleteByCurrencyId(currencyId);
            // 创建新的关联
            for (Long categoryId : request.getCategoryIds()) {
                CurrencyCategoryRelation relation = new CurrencyCategoryRelation();
                relation.setCurrencyId(currencyId);
                relation.setCategoryId(categoryId);
                categoryRelationRepository.save(relation);
            }
        }
        
        return currencyRepository.save(currency);
    }
    
    @Override
    @Transactional
    @Deprecated
    public Currency createCurrency(String currencyCode, String currencyName, String symbol, 
                                   Integer decimals, Boolean spotEnabled, Boolean futuresUsdtEnabled,
                                   Boolean futuresCoinEnabled, Boolean optionsEnabled, Boolean leveragedEnabled) {
        CurrencyCreateRequest request = new CurrencyCreateRequest();
        request.setCurrencyCode(currencyCode);
        request.setCurrencyName(currencyName);
        request.setSymbol(symbol);
        request.setDecimals(decimals);
        request.setSpotEnabled(spotEnabled);
        request.setFuturesUsdtEnabled(futuresUsdtEnabled);
        request.setFuturesCoinEnabled(futuresCoinEnabled);
        request.setOptionsEnabled(optionsEnabled);
        request.setLeveragedEnabled(leveragedEnabled);
        return createCurrency(request);
    }
    
    @Override
    @Transactional
    @Deprecated
    public Currency updateCurrency(Long currencyId, String currencyName, String symbol, String iconUrl,
                                   Integer decimals, BigDecimal minWithdrawAmount, BigDecimal maxWithdrawAmount,
                                   BigDecimal withdrawFee, Boolean depositEnabled, Boolean withdrawEnabled,
                                   Boolean spotEnabled, Boolean futuresUsdtEnabled, Boolean futuresCoinEnabled,
                                   Boolean optionsEnabled, Boolean leveragedEnabled, String status, Integer sortOrder,
                                   String description) {
        CurrencyUpdateRequest request = new CurrencyUpdateRequest();
        request.setCurrencyName(currencyName);
        request.setSymbol(symbol);
        request.setIconUrl(iconUrl);
        request.setDecimals(decimals);
        request.setMinWithdrawAmount(minWithdrawAmount);
        request.setMaxWithdrawAmount(maxWithdrawAmount);
        request.setWithdrawFee(withdrawFee);
        request.setDepositEnabled(depositEnabled);
        request.setWithdrawEnabled(withdrawEnabled);
        request.setSpotEnabled(spotEnabled);
        request.setFuturesUsdtEnabled(futuresUsdtEnabled);
        request.setFuturesCoinEnabled(futuresCoinEnabled);
        request.setOptionsEnabled(optionsEnabled);
        request.setLeveragedEnabled(leveragedEnabled);
        request.setStatus(status);
        request.setSortOrder(sortOrder);
        request.setDescription(description);
        return updateCurrency(currencyId, request);
    }
    
    @Override
    @Transactional
    public void deleteCurrency(Long currencyId) {
        if (!currencyRepository.existsById(currencyId)) {
            throw new RuntimeException("币种不存在: " + currencyId);
        }
        
        // 删除关联数据
        currencyNameService.deleteCurrencyNames(currencyId);
        exchangeRateService.deleteExchangeRates(currencyId);
        categoryRelationRepository.deleteByCurrencyId(currencyId);
        
        // 删除币种
        currencyRepository.deleteById(currencyId);
    }
    
    @Override
    public Currency getCurrencyById(Long currencyId) {
        return currencyRepository.findById(currencyId)
                .orElseThrow(() -> new RuntimeException("币种不存在: " + currencyId));
    }
    
    @Override
    public Currency getCurrencyByCode(String currencyCode) {
        return currencyRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("币种不存在: " + currencyCode));
    }
    
    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
    
    @Override
    public List<Currency> getActiveCurrencies() {
        return currencyRepository.findByStatus("ACTIVE");
    }
    
    @Override
    public List<Currency> getCurrenciesBySpotEnabled() {
        return currencyRepository.findBySpotEnabledTrue();
    }
    
    @Override
    public List<Currency> getCurrenciesByFuturesUsdtEnabled() {
        return currencyRepository.findByFuturesUsdtEnabledTrue();
    }
    
    @Override
    public List<Currency> getCurrenciesByFuturesCoinEnabled() {
        return currencyRepository.findByFuturesCoinEnabledTrue();
    }
    
    @Override
    public List<Currency> getCurrenciesByOptionsEnabled() {
        return currencyRepository.findByOptionsEnabledTrue();
    }
    
    @Override
    public List<Currency> getCurrenciesByLeveragedEnabled() {
        return currencyRepository.findByLeveragedEnabledTrue();
    }
}
