/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.repository;

import com.cryptotrade.selftrading.entity.SelfTradingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelfTradingOrderRepository extends JpaRepository<SelfTradingOrder, Long> {
    Optional<SelfTradingOrder> findByOrderNo(String orderNo);

    List<SelfTradingOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<SelfTradingOrder> findByMerchantIdOrderByCreatedAtDesc(Long merchantId);

    List<SelfTradingOrder> findByUserIdAndStatus(Long userId, String status);

    List<SelfTradingOrder> findByMerchantIdAndStatus(Long merchantId, String status);
}















