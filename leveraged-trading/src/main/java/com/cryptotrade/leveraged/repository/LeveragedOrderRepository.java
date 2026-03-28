/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeveragedOrderRepository extends JpaRepository<LeveragedOrder, Long> {
    List<LeveragedOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<LeveragedOrder> findByUserIdAndStatus(Long userId, String status);

    List<LeveragedOrder> findByAccountIdAndStatus(Long accountId, String status);

    List<LeveragedOrder> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<LeveragedOrder> findByOrderTypeAndStatus(String orderType, String status);

    List<LeveragedOrder> findByStatusAndTriggerPriceIsNotNull(String status);
}















