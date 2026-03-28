/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinFuturesPositionRepository extends JpaRepository<CoinFuturesPosition, Long> {
    List<CoinFuturesPosition> findByUserIdAndStatus(Long userId, String status);
    Optional<CoinFuturesPosition> findByUserIdAndPairNameAndPositionSideAndStatus(
            Long userId, String pairName, String positionSide, String status);
    List<CoinFuturesPosition> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}















