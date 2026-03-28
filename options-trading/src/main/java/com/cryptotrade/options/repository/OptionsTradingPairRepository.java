/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionsTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionsTradingPairRepository extends JpaRepository<OptionsTradingPair, Long> {
    Optional<OptionsTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<OptionsTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<OptionsTradingPair> findByVisibleTrueOrderBySortOrderAsc();
    
    List<OptionsTradingPair> findByStatus(String status);
}














