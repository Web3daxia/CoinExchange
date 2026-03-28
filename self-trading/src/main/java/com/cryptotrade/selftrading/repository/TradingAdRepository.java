/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.repository;

import com.cryptotrade.selftrading.entity.TradingAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingAdRepository extends JpaRepository<TradingAd, Long> {
    List<TradingAd> findByMerchantIdAndStatus(Long merchantId, String status);

    List<TradingAd> findByStatus(String status);

    List<TradingAd> findByAdTypeAndStatus(String adType, String status);

    List<TradingAd> findByCryptoCurrencyAndFiatCurrencyAndStatus(String cryptoCurrency, String fiatCurrency, String status);
}















