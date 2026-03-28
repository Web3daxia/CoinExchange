/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinFuturesTradingRobotRepository extends JpaRepository<CoinFuturesTradingRobot, Long> {
    List<CoinFuturesTradingRobot> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<CoinFuturesTradingRobot> findByUserIdAndStatus(Long userId, String status);
    List<CoinFuturesTradingRobot> findByStatus(String status);
}















