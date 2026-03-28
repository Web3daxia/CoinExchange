/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.repository;

import com.cryptotrade.spotbot.entity.SpotTradingBotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotTradingBotConfigRepository extends JpaRepository<SpotTradingBotConfig, Long> {
    Optional<SpotTradingBotConfig> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<SpotTradingBotConfig> findByStatus(String status);
    
    List<SpotTradingBotConfig> findByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);
}














