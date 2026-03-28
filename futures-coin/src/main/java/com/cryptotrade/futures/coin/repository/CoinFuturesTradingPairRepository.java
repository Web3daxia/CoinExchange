/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinFuturesTradingPairRepository extends JpaRepository<CoinFuturesTradingPair, Long> {
    Optional<CoinFuturesTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<CoinFuturesTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<CoinFuturesTradingPair> findByVisibleTrueOrderBySortOrderAsc();
    
    List<CoinFuturesTradingPair> findByStatus(String status);
}














