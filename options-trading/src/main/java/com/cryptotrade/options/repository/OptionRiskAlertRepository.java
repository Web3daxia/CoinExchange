/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.repository;

import com.cryptotrade.options.entity.OptionRiskAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRiskAlertRepository extends JpaRepository<OptionRiskAlert, Long> {
    List<OptionRiskAlert> findByUserIdAndStatus(Long userId, String status);

    List<OptionRiskAlert> findByUserId(Long userId);

    List<OptionRiskAlert> findByIsTriggeredFalseAndStatus(String status);
}















