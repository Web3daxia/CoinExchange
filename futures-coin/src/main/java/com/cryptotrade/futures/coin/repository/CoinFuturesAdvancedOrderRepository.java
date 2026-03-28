/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesAdvancedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoinFuturesAdvancedOrderRepository extends JpaRepository<CoinFuturesAdvancedOrder, Long> {
    List<CoinFuturesAdvancedOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<CoinFuturesAdvancedOrder> findByUserIdAndStatus(Long userId, String status);
    List<CoinFuturesAdvancedOrder> findByStatusAndExpireTimeBefore(String status, LocalDateTime time);
    List<CoinFuturesAdvancedOrder> findByStatusIn(List<String> statuses);
}















