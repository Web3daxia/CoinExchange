/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.repository;

import com.cryptotrade.futures.usdt.entity.GradientRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface GradientRuleRepository extends JpaRepository<GradientRule, Long> {
    @Query("SELECT g FROM GradientRule g WHERE g.pairName = :pairName " +
           "AND :position BETWEEN g.minPosition AND g.maxPosition " +
           "ORDER BY g.positionTier ASC")
    Optional<GradientRule> findApplicableRule(@Param("pairName") String pairName, 
                                               @Param("position") BigDecimal position);
}


