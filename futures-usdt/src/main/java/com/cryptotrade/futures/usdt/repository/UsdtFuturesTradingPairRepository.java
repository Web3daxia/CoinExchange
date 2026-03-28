/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.repository;

import com.cryptotrade.futures.usdt.entity.UsdtFuturesTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsdtFuturesTradingPairRepository extends JpaRepository<UsdtFuturesTradingPair, Long> {
    Optional<UsdtFuturesTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<UsdtFuturesTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<UsdtFuturesTradingPair> findByVisibleTrueOrderBySortOrderAsc();
    
    List<UsdtFuturesTradingPair> findByStatus(String status);
}














