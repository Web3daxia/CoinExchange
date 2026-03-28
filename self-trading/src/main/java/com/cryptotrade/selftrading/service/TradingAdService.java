/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service;

import com.cryptotrade.selftrading.entity.TradingAd;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易广告服务接口
 */
public interface TradingAdService {
    /**
     * 创建广告
     * @param merchantId 商家ID
     * @param adType 广告类型
     * @param cryptoCurrency 加密货币
     * @param fiatCurrency 法币
     * @param price 价格
     * @param minAmount 最小金额
     * @param maxAmount 最大金额
     * @param paymentMethods 支付方式
     * @param settings 其他设置
     * @return 广告
     */
    TradingAd createAd(Long merchantId, String adType, String cryptoCurrency, String fiatCurrency,
                      BigDecimal price, BigDecimal minAmount, BigDecimal maxAmount,
                      List<String> paymentMethods, Map<String, Object> settings);

    /**
     * 更新广告
     * @param merchantId 商家ID
     * @param adId 广告ID
     * @param updates 更新内容
     * @return 广告
     */
    TradingAd updateAd(Long merchantId, Long adId, Map<String, Object> updates);

    /**
     * 暂停/恢复广告
     * @param merchantId 商家ID
     * @param adId 广告ID
     * @param paused 是否暂停
     * @return 广告
     */
    TradingAd pauseAd(Long merchantId, Long adId, Boolean paused);

    /**
     * 关闭广告
     * @param merchantId 商家ID
     * @param adId 广告ID
     */
    void closeAd(Long merchantId, Long adId);

    /**
     * 查询广告列表
     * @param filters 筛选条件
     * @return 广告列表
     */
    List<TradingAd> getAds(Map<String, Object> filters);

    /**
     * 查询广告详情
     * @param adId 广告ID
     * @return 广告
     */
    TradingAd getAd(Long adId);

    /**
     * 查询商家的广告
     * @param merchantId 商家ID
     * @return 广告列表
     */
    List<TradingAd> getMerchantAds(Long merchantId);
}















