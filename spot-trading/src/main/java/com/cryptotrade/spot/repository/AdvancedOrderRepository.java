/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.repository;

import com.cryptotrade.spot.entity.AdvancedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvancedOrderRepository extends JpaRepository<AdvancedOrder, Long> {
    List<AdvancedOrder> findByUserId(Long userId);
    List<AdvancedOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<AdvancedOrder> findByUserIdAndStatus(Long userId, String status);
    List<AdvancedOrder> findByStatus(String status);
    List<AdvancedOrder> findByStatusAndNextExecutionTimeBefore(String status, LocalDateTime time);
    List<AdvancedOrder> findByStatusIn(java.util.List<String> statuses);
}















