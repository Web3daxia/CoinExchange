/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.repository;

import com.cryptotrade.flashexchange.entity.FlashExchangeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashExchangeOrderRepository extends JpaRepository<FlashExchangeOrder, Long> {
    Optional<FlashExchangeOrder> findByOrderNo(String orderNo);

    List<FlashExchangeOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<FlashExchangeOrder> findByUserIdAndStatus(Long userId, String status);
}















