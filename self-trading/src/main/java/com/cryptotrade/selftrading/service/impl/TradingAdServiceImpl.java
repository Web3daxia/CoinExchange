/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service.impl;

import com.cryptotrade.selftrading.entity.TradingAd;
import com.cryptotrade.selftrading.repository.TradingAdRepository;
import com.cryptotrade.selftrading.service.TradingAdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易广告服务实现类
 */
@Service
public class TradingAdServiceImpl implements TradingAdService {

    @Autowired
    private TradingAdRepository tradingAdRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public TradingAd createAd(Long merchantId, String adType, String cryptoCurrency, String fiatCurrency,
                             BigDecimal price, BigDecimal minAmount, BigDecimal maxAmount,
                             List<String> paymentMethods, Map<String, Object> settings) {
        TradingAd ad = new TradingAd();
        ad.setMerchantId(merchantId);
        ad.setAdType(adType);
        ad.setCryptoCurrency(cryptoCurrency);
        ad.setFiatCurrency(fiatCurrency);
        ad.setPrice(price);
        ad.setMinAmount(minAmount);
        ad.setMaxAmount(maxAmount);
        ad.setAvailableAmount(maxAmount);
        
        try {
            ad.setPaymentMethods(objectMapper.writeValueAsString(paymentMethods));
        } catch (Exception e) {
            ad.setPaymentMethods("[]");
        }

        // 设置其他参数
        if (settings != null) {
            ad.setRequireKyc((Boolean) settings.getOrDefault("requireKyc", false));
            ad.setRequireAssetProof((Boolean) settings.getOrDefault("requireAssetProof", false));
            ad.setRequireTransactionHistory((Boolean) settings.getOrDefault("requireTransactionHistory", false));
            ad.setAutoReplyEnabled((Boolean) settings.getOrDefault("autoReplyEnabled", false));
            ad.setAutoReplyContent((String) settings.get("autoReplyContent"));
        }

        ad.setStatus("ACTIVE");
        ad.setOrderCount(0);

        return tradingAdRepository.save(ad);
    }

    @Override
    @Transactional
    public TradingAd updateAd(Long merchantId, Long adId, Map<String, Object> updates) {
        TradingAd ad = tradingAdRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("广告不存在"));

        if (!ad.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权操作此广告");
        }

        if (updates.containsKey("price")) {
            ad.setPrice(new BigDecimal(updates.get("price").toString()));
        }
        if (updates.containsKey("minAmount")) {
            ad.setMinAmount(new BigDecimal(updates.get("minAmount").toString()));
        }
        if (updates.containsKey("maxAmount")) {
            ad.setMaxAmount(new BigDecimal(updates.get("maxAmount").toString()));
        }
        if (updates.containsKey("paymentMethods")) {
            try {
                ad.setPaymentMethods(objectMapper.writeValueAsString(updates.get("paymentMethods")));
            } catch (Exception e) {
                // 忽略错误
            }
        }
        if (updates.containsKey("autoReplyEnabled")) {
            ad.setAutoReplyEnabled((Boolean) updates.get("autoReplyEnabled"));
        }
        if (updates.containsKey("autoReplyContent")) {
            ad.setAutoReplyContent(updates.get("autoReplyContent").toString());
        }

        return tradingAdRepository.save(ad);
    }

    @Override
    @Transactional
    public TradingAd pauseAd(Long merchantId, Long adId, Boolean paused) {
        TradingAd ad = tradingAdRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("广告不存在"));

        if (!ad.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权操作此广告");
        }

        ad.setStatus(paused ? "PAUSED" : "ACTIVE");
        return tradingAdRepository.save(ad);
    }

    @Override
    @Transactional
    public void closeAd(Long merchantId, Long adId) {
        TradingAd ad = tradingAdRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("广告不存在"));

        if (!ad.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权操作此广告");
        }

        ad.setStatus("CLOSED");
        tradingAdRepository.save(ad);
    }

    @Override
    public List<TradingAd> getAds(Map<String, Object> filters) {
        // TODO: 根据筛选条件查询广告
        // 这里简化处理，实际应该根据filters动态构建查询
        return tradingAdRepository.findByStatus("ACTIVE");
    }

    @Override
    public TradingAd getAd(Long adId) {
        return tradingAdRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("广告不存在"));
    }

    @Override
    public List<TradingAd> getMerchantAds(Long merchantId) {
        return tradingAdRepository.findByMerchantIdAndStatus(merchantId, "ACTIVE");
    }
}















