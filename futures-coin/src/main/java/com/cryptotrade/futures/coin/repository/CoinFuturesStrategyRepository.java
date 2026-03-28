/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinFuturesStrategyRepository extends JpaRepository<CoinFuturesStrategy, Long> {
    List<CoinFuturesStrategy> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<CoinFuturesStrategy> findByUserIdAndStatus(Long userId, String status);
    List<CoinFuturesStrategy> findByStatus(String status);
}















