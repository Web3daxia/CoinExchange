/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedRiskAlert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 杠杆风险管理服务接口
 */
public interface LeveragedRiskManagementService {
    /**
     * 查询账户风险信息
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @return 风险信息
     */
    Map<String, Object> getAccountRisk(Long userId, String pairName);

    /**
     * 设置风险警报
     * @param userId 用户ID
     * @param accountId 账户ID
     * @param positionId 仓位ID
     * @param alertType 警报类型
     * @param threshold 阈值
     * @param thresholdPercentage 阈值百分比
     * @param notificationMethod 通知方式
     * @return 风险警报
     */
    LeveragedRiskAlert setRiskAlert(Long userId, Long accountId, Long positionId, String alertType,
                                    BigDecimal threshold, BigDecimal thresholdPercentage, String notificationMethod);

    /**
     * 检查并触发风险警报
     */
    void checkAndTriggerRiskAlerts();

    /**
     * 检查并执行强平
     */
    void checkAndLiquidate();

    /**
     * 监控所有仓位风险
     */
    void monitorPositions();
}















