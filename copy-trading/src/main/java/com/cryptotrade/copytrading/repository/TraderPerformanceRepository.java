/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.repository;

import com.cryptotrade.copytrading.entity.TraderPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraderPerformanceRepository extends JpaRepository<TraderPerformance, Long> {
    List<TraderPerformance> findByTraderIdOrderByPeriodStartDesc(Long traderId);

    List<TraderPerformance> findByTraderIdAndPeriodTypeOrderByPeriodStartDesc(Long traderId, String periodType);

    Optional<TraderPerformance> findByTraderIdAndPeriodTypeAndPeriodStartAndPeriodEnd(
            Long traderId, String periodType, LocalDateTime periodStart, LocalDateTime periodEnd);

    List<TraderPerformance> findByTraderIdAndPeriodStartBetweenOrderByPeriodStartDesc(
            Long traderId, LocalDateTime start, LocalDateTime end);
}















