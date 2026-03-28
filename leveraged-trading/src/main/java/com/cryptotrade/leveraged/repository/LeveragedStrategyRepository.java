/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeveragedStrategyRepository extends JpaRepository<LeveragedStrategy, Long> {
    List<LeveragedStrategy> findByUserIdAndStatus(Long userId, String status);

    List<LeveragedStrategy> findByUserId(Long userId);

    List<LeveragedStrategy> findByAccountIdAndStatus(Long accountId, String status);

    List<LeveragedStrategy> findByStrategyType(String strategyType);

    List<LeveragedStrategy> findByStatus(String status);
}

