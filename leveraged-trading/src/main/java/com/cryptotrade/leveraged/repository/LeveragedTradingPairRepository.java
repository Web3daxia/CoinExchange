/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeveragedTradingPairRepository extends JpaRepository<LeveragedTradingPair, Long> {
    Optional<LeveragedTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<LeveragedTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<LeveragedTradingPair> findByVisibleTrueOrderBySortOrderAsc();
    
    List<LeveragedTradingPair> findByStatus(String status);
}














