/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryContractTradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryContractTradingPairRepository extends JpaRepository<DeliveryContractTradingPair, Long> {
    Optional<DeliveryContractTradingPair> findByPairName(String pairName);
    
    boolean existsByPairName(String pairName);
    
    List<DeliveryContractTradingPair> findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    
    List<DeliveryContractTradingPair> findByVisibleTrueOrderBySortOrderAsc();
    
    List<DeliveryContractTradingPair> findByStatus(String status);
}














