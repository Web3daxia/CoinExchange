/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.repository;

import com.cryptotrade.futures.usdt.entity.FuturesAdvancedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FuturesAdvancedOrderRepository extends JpaRepository<FuturesAdvancedOrder, Long> {
    List<FuturesAdvancedOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<FuturesAdvancedOrder> findByUserIdAndStatus(Long userId, String status);
    List<FuturesAdvancedOrder> findByStatusAndExpireTimeBefore(String status, LocalDateTime time);
    List<FuturesAdvancedOrder> findByStatusIn(List<String> statuses);
}


