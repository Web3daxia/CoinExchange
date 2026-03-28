/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedRiskAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeveragedRiskAlertRepository extends JpaRepository<LeveragedRiskAlert, Long> {
    List<LeveragedRiskAlert> findByUserIdAndStatus(Long userId, String status);

    List<LeveragedRiskAlert> findByUserId(Long userId);

    List<LeveragedRiskAlert> findByIsTriggeredFalseAndStatus(String status);
}















