/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.repository;

import com.cryptotrade.spot.entity.SpotTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotTradingPairRepository extends JpaRepository<SpotTradingPair, Long> {
    Optional<SpotTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<SpotTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<SpotTradingPair> findByFrontendVisibleTrueOrderBySortOrderAsc();
    
    List<SpotTradingPair> findByTradingArea(String tradingArea);
    
    List<SpotTradingPair> findByStatus(String status);
}














