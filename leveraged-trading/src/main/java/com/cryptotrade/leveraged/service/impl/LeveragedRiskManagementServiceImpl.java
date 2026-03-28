/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedPosition;
import com.cryptotrade.leveraged.entity.LeveragedRiskAlert;
import com.cryptotrade.leveraged.repository.LeveragedPositionRepository;
import com.cryptotrade.leveraged.repository.LeveragedRiskAlertRepository;
import com.cryptotrade.leveraged.service.LeveragedPositionService;
import com.cryptotrade.leveraged.service.LeveragedRiskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 杠杆风险管理服务实现类
 */
@Service
public class LeveragedRiskManagementServiceImpl implements LeveragedRiskManagementService {

    @Autowired
    private LeveragedPositionRepository leveragedPositionRepository;

    @Autowired
    private LeveragedRiskAlertRepository leveragedRiskAlertRepository;

    @Autowired
    private LeveragedPositionService leveragedPositionService;

    @Override
    public Map<String, Object> getAccountRisk(Long userId, String pairName) {
        return leveragedPositionService.getAccountRisk(userId, pairName);
    }

    @Override
    @Transactional
    public LeveragedRiskAlert setRiskAlert(Long userId, Long accountId, Long positionId, String alertType,
                                          BigDecimal threshold, BigDecimal thresholdPercentage,
                                          String notificationMethod) {
        LeveragedRiskAlert alert = new LeveragedRiskAlert();
        alert.setUserId(userId);
        alert.setAccountId(accountId);
        alert.setPositionId(positionId);
        alert.setAlertType(alertType);
        alert.setThreshold(threshold);
        alert.setThresholdPercentage(thresholdPercentage);
        alert.setNotificationMethod(notificationMethod);
        alert.setIsTriggered(false);
        alert.setStatus("ACTIVE");

        return leveragedRiskAlertRepository.save(alert);
    }

    @Override
    @Scheduled(fixedRate = 30000) // 每30秒执行一次
    public void checkAndTriggerRiskAlerts() {
        List<LeveragedRiskAlert> alerts = leveragedRiskAlertRepository.findByIsTriggeredFalseAndStatus("ACTIVE");

        for (LeveragedRiskAlert alert : alerts) {
            try {
                if (checkAlertCondition(alert)) {
                    triggerAlert(alert);
                }
            } catch (Exception e) {
                System.err.println("检查风险警报失败: " + alert.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void checkAndLiquidate() {
        List<LeveragedPosition> positions = leveragedPositionRepository.findByStatus("ACTIVE");

        for (LeveragedPosition position : positions) {
            try {
                // 更新仓位盈亏
                leveragedPositionService.updatePositionPnl(position.getId());

                // 重新获取仓位（获取更新后的数据）
                position = leveragedPositionRepository.findById(position.getId()).orElse(null);
                if (position == null || !"ACTIVE".equals(position.getStatus())) {
                    continue;
                }

                // 检查是否需要强平
                BigDecimal marginRatio = position.getMarginRatio();
                if (marginRatio != null && marginRatio.compareTo(new BigDecimal("1.0")) < 0) {
                    // 保证金率低于1.0，触发强平
                    leveragedPositionService.liquidatePosition(position.getUserId(), position.getId());
                }
            } catch (Exception e) {
                System.err.println("检查强平失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 30000) // 每30秒执行一次
    public void monitorPositions() {
        leveragedPositionService.updateAllPositionsPnl();
    }

    /**
     * 检查警报条件
     */
    private boolean checkAlertCondition(LeveragedRiskAlert alert) {
        if (alert.getPositionId() != null) {
            // 检查特定仓位
            LeveragedPosition position = leveragedPositionRepository.findById(alert.getPositionId()).orElse(null);
            if (position == null || !"ACTIVE".equals(position.getStatus())) {
                return false;
            }

            return checkPositionAlert(alert, position);
        } else {
            // 检查账户所有仓位
            List<LeveragedPosition> positions = leveragedPositionRepository
                    .findByAccountIdAndStatus(alert.getAccountId(), "ACTIVE");
            for (LeveragedPosition position : positions) {
                if (checkPositionAlert(alert, position)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查仓位警报
     */
    private boolean checkPositionAlert(LeveragedRiskAlert alert, LeveragedPosition position) {
        if ("MARGIN_LOW".equals(alert.getAlertType())) {
            BigDecimal marginRatio = position.getMarginRatio();
            if (marginRatio != null && alert.getThresholdPercentage() != null) {
                BigDecimal threshold = new BigDecimal("100").subtract(alert.getThresholdPercentage())
                        .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
                return marginRatio.compareTo(threshold) < 0;
            }
        } else if ("LIQUIDATION_RISK".equals(alert.getAlertType())) {
            BigDecimal marginRatio = position.getMarginRatio();
            if (marginRatio != null && marginRatio.compareTo(new BigDecimal("1.1")) < 0) {
                return true;
            }
        } else if ("LEVERAGE_HIGH".equals(alert.getAlertType())) {
            // TODO: 检查杠杆是否过高
        }
        return false;
    }

    /**
     * 触发警报
     */
    private void triggerAlert(LeveragedRiskAlert alert) {
        alert.setIsTriggered(true);
        alert.setTriggeredAt(LocalDateTime.now());
        leveragedRiskAlertRepository.save(alert);

        // TODO: 发送通知（短信、邮件、站内信）
    }
}















