/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.repository;

import com.cryptotrade.copytrading.entity.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraderRepository extends JpaRepository<Trader, Long> {
    Optional<Trader> findByUserId(Long userId);

    List<Trader> findByStatus(String status);

    List<Trader> findByTraderTypeAndStatus(String traderType, String status);

    List<Trader> findByPublicEnabledTrueAndStatus(String status);

    List<Trader> findByLevel(String level);

    List<Trader> findByStatusOrderByTotalAumDesc(String status);

    List<Trader> findByStatusOrderByTotalProfitDesc(String status);
}















