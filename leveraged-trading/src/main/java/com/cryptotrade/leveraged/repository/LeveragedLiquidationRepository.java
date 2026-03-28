/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedLiquidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeveragedLiquidationRepository extends JpaRepository<LeveragedLiquidation, Long> {
    List<LeveragedLiquidation> findByUserIdOrderByLiquidationTimeDesc(Long userId);

    List<LeveragedLiquidation> findByAccountId(Long accountId);

    List<LeveragedLiquidation> findByPositionId(Long positionId);
}















