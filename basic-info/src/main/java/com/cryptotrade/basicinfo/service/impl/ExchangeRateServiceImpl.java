/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.entity.ExchangeRate;
import com.cryptotrade.basicinfo.repository.ExchangeRateRepository;
import com.cryptotrade.basicinfo.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 汇率服务实现类
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        Optional<ExchangeRate> rateOpt = exchangeRateRepository
                .findByFromCurrencyAndToCurrencyAndStatus(fromCurrency, toCurrency, "ACTIVE");

        if (rateOpt.isPresent()) {
            return rateOpt.get().getRate();
        }

        // 如果没有直接汇率，尝试通过USDT中转
        if (!fromCurrency.equals("USDT") && !toCurrency.equals("USDT")) {
            Optional<ExchangeRate> fromToUsdt = exchangeRateRepository
                    .findByFromCurrencyAndToCurrencyAndStatus(fromCurrency, "USDT", "ACTIVE");
            Optional<ExchangeRate> usdtToTo = exchangeRateRepository
                    .findByFromCurrencyAndToCurrencyAndStatus("USDT", toCurrency, "ACTIVE");

            if (fromToUsdt.isPresent() && usdtToTo.isPresent()) {
                return fromToUsdt.get().getRate().multiply(usdtToTo.get().getRate());
            }
        }

        // 默认返回1（实际应该从外部API获取）
        return BigDecimal.ONE;
    }

    @Override
    public List<Map<String, Object>> getAllExchangeRates(String baseCurrency) {
        List<ExchangeRate> rates;
        if (baseCurrency != null && !baseCurrency.isEmpty()) {
            rates = exchangeRateRepository.findByFromCurrencyAndStatus(baseCurrency, "ACTIVE");
        } else {
            rates = exchangeRateRepository.findByStatus("ACTIVE");
        }

        return rates.stream().map(rate -> {
            Map<String, Object> result = new HashMap<>();
            result.put("fromCurrency", rate.getFromCurrency());
            result.put("toCurrency", rate.getToCurrency());
            result.put("rate", rate.getRate());
            result.put("source", rate.getSource());
            result.put("updatedAt", rate.getUpdatedAt());
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateExchangeRate(String fromCurrency, String toCurrency, BigDecimal rate, String source) {
        Optional<ExchangeRate> existing = exchangeRateRepository
                .findByFromCurrencyAndToCurrencyAndStatus(fromCurrency, toCurrency, "ACTIVE");

        if (existing.isPresent()) {
            ExchangeRate exchangeRate = existing.get();
            exchangeRate.setRate(rate);
            exchangeRate.setSource(source);
            exchangeRateRepository.save(exchangeRate);
        } else {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setFromCurrency(fromCurrency);
            exchangeRate.setToCurrency(toCurrency);
            exchangeRate.setRate(rate);
            exchangeRate.setSource(source);
            exchangeRate.setStatus("ACTIVE");
            exchangeRateRepository.save(exchangeRate);
        }
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate);
    }
}















