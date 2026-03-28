/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.repository;

import com.cryptotrade.futures.usdt.entity.FuturesPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuturesPositionRepository extends JpaRepository<FuturesPosition, Long> {
    List<FuturesPosition> findByUserIdAndStatus(Long userId, String status);
    List<FuturesPosition> findByUserIdAndPairNameAndStatus(Long userId, String pairName, String status);
    Optional<FuturesPosition> findByUserIdAndPairNameAndPositionSideAndStatus(
            Long userId, String pairName, String positionSide, String status);
    List<FuturesPosition> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}


