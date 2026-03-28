/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.entity.OptionRiskAlert;
import com.cryptotrade.options.repository.OptionPositionRepository;
import com.cryptotrade.options.repository.OptionRiskAlertRepository;
import com.cryptotrade.options.service.OptionOrderService;
import com.cryptotrade.options.service.OptionRiskManagementService;
import com.cryptotrade.wallet.service.WalletService;
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
 * 期权风险管理服务实现类
 */
@Service
public class OptionRiskManagementServiceImpl implements OptionRiskManagementService {

    @Autowired
    private OptionPositionRepository optionPositionRepository;

    @Autowired
    private OptionRiskAlertRepository optionRiskAlertRepository;

    @Autowired
    private OptionOrderService optionOrderService;

    @Autowired
    private WalletService walletService;

    @Override
    public Map<String, Object> getAccountRisk(Long userId) {
        List<OptionPosition> positions = optionPositionRepository.findByUserIdAndStatus(userId, "ACTIVE");

        BigDecimal totalMargin = BigDecimal.ZERO;
        BigDecimal totalUnrealizedPnl = BigDecimal.ZERO;
        BigDecimal totalRealizedPnl = BigDecimal.ZERO;
        int activePositions = 0;
        int inTheMoneyPositions = 0;
        int nearExpiryPositions = 0;

        for (OptionPosition position : positions) {
            totalMargin = totalMargin.add(position.getMargin() != null ? position.getMargin() : BigDecimal.ZERO);
            totalUnrealizedPnl = totalUnrealizedPnl.add(
                    position.getUnrealizedPnl() != null ? position.getUnrealizedPnl() : BigDecimal.ZERO);
            totalRealizedPnl = totalRealizedPnl.add(
                    position.getRealizedPnl() != null ? position.getRealizedPnl() : BigDecimal.ZERO);
            activePositions++;

            if (Boolean.TRUE.equals(position.getIsInTheMoney())) {
                inTheMoneyPositions++;
            }

            if (position.getExpiryDate().isBefore(LocalDateTime.now().plusDays(7))) {
                nearExpiryPositions++;
            }
        }

        // 计算风险等级
        String riskLevel = "LOW";
        if (totalUnrealizedPnl.compareTo(new BigDecimal("-0.5").multiply(totalMargin)) < 0) {
            riskLevel = "CRITICAL";
        } else if (totalUnrealizedPnl.compareTo(new BigDecimal("-0.3").multiply(totalMargin)) < 0) {
            riskLevel = "HIGH";
        } else if (totalUnrealizedPnl.compareTo(new BigDecimal("-0.1").multiply(totalMargin)) < 0) {
            riskLevel = "MEDIUM";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalMargin", totalMargin);
        result.put("totalUnrealizedPnl", totalUnrealizedPnl);
        result.put("totalRealizedPnl", totalRealizedPnl);
        result.put("activePositions", activePositions);
        result.put("inTheMoneyPositions", inTheMoneyPositions);
        result.put("nearExpiryPositions", nearExpiryPositions);
        result.put("riskLevel", riskLevel);
        result.put("needLiquidation", riskLevel.equals("CRITICAL"));

        return result;
    }

    @Override
    @Transactional
    public OptionRiskAlert setStopLoss(Long userId, Long positionId, String alertType,
                                       BigDecimal threshold, BigDecimal thresholdPercentage) {
        OptionRiskAlert alert = new OptionRiskAlert();
        alert.setUserId(userId);
        alert.setPositionId(positionId);
        alert.setAlertType(alertType);
        alert.setThreshold(threshold);
        alert.setThresholdPercentage(thresholdPercentage);
        alert.setIsTriggered(false);
        alert.setStatus("ACTIVE");

        return optionRiskAlertRepository.save(alert);
    }

    @Override
    @Scheduled(fixedRate = 30000) // 每30秒执行一次
    public void checkAndTriggerRiskAlerts() {
        List<OptionRiskAlert> alerts = optionRiskAlertRepository.findByIsTriggeredFalseAndStatus("ACTIVE");

        for (OptionRiskAlert alert : alerts) {
            try {
                if (alert.getPositionId() != null) {
                    // 检查特定持仓
                    OptionPosition position = optionPositionRepository.findById(alert.getPositionId())
                            .orElse(null);
                    if (position != null && checkAlertCondition(alert, position)) {
                        triggerAlert(alert);
                    }
                } else {
                    // 检查用户所有持仓
                    List<OptionPosition> positions = optionPositionRepository.findByUserIdAndStatus(
                            alert.getUserId(), "ACTIVE");
                    for (OptionPosition position : positions) {
                        if (checkAlertCondition(alert, position)) {
                            triggerAlert(alert);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("检查风险警报失败: " + alert.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void checkAndLiquidate() {
        List<OptionPosition> positions = optionPositionRepository.findByStatus("ACTIVE");

        for (OptionPosition position : positions) {
            try {
                // 检查是否需要强平（亏损超过保证金的50%）
                if (position.getMargin() != null && position.getUnrealizedPnl() != null) {
                    BigDecimal lossThreshold = position.getMargin().multiply(new BigDecimal("-0.5"));
                    if (position.getUnrealizedPnl().compareTo(lossThreshold) < 0) {
                        // 执行强平
                        liquidatePosition(position);
                    }
                }
            } catch (Exception e) {
                System.err.println("强平检查失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void monitorPositions() {
        List<OptionPosition> positions = optionPositionRepository.findByStatus("ACTIVE");

        for (OptionPosition position : positions) {
            try {
                // 更新未实现盈亏
                updateUnrealizedPnl(position);
            } catch (Exception e) {
                System.err.println("监控持仓失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    /**
     * 检查警报条件
     */
    private boolean checkAlertCondition(OptionRiskAlert alert, OptionPosition position) {
        if ("PROFIT".equals(alert.getAlertType())) {
            if (alert.getThreshold() != null && position.getUnrealizedPnl() != null) {
                return position.getUnrealizedPnl().compareTo(alert.getThreshold()) >= 0;
            }
            if (alert.getThresholdPercentage() != null && position.getMargin() != null) {
                BigDecimal threshold = position.getMargin().multiply(alert.getThresholdPercentage())
                        .divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP);
                return position.getUnrealizedPnl() != null &&
                        position.getUnrealizedPnl().compareTo(threshold) >= 0;
            }
        } else if ("LOSS".equals(alert.getAlertType())) {
            if (alert.getThreshold() != null && position.getUnrealizedPnl() != null) {
                return position.getUnrealizedPnl().compareTo(alert.getThreshold().negate()) <= 0;
            }
            if (alert.getThresholdPercentage() != null && position.getMargin() != null) {
                BigDecimal threshold = position.getMargin().multiply(alert.getThresholdPercentage())
                        .divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP);
                return position.getUnrealizedPnl() != null &&
                        position.getUnrealizedPnl().compareTo(threshold.negate()) <= 0;
            }
        }
        return false;
    }

    /**
     * 触发警报
     */
    private void triggerAlert(OptionRiskAlert alert) {
        alert.setIsTriggered(true);
        alert.setTriggeredAt(LocalDateTime.now());
        optionRiskAlertRepository.save(alert);

        // TODO: 发送通知给用户
    }

    /**
     * 强平持仓
     */
    private void liquidatePosition(OptionPosition position) {
        // TODO: 实现强平逻辑，需要平仓该持仓
        // 这里需要找到对应的合约，创建平仓订单
    }

    /**
     * 更新未实现盈亏
     */
    private void updateUnrealizedPnl(OptionPosition position) {
        // TODO: 根据当前市场价格计算未实现盈亏
        // 需要从市场数据服务获取当前期权价格
    }
}

