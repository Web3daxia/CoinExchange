/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinFuturesOrderRepository extends JpaRepository<CoinFuturesOrder, Long> {
    List<CoinFuturesOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<CoinFuturesOrder> findByUserIdAndStatus(Long userId, String status);
    List<CoinFuturesOrder> findByPositionId(Long positionId);
    List<CoinFuturesOrder> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
    List<CoinFuturesOrder> findByStatus(String status);
}















