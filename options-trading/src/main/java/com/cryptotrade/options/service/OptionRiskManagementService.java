/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.entity.OptionRiskAlert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 期权风险管理服务接口
 */
public interface OptionRiskManagementService {
    /**
     * 查询账户风险情况
     * @param userId 用户ID
     * @return 风险信息
     */
    Map<String, Object> getAccountRisk(Long userId);

    /**
     * 设置止损策略
     * @param userId 用户ID
     * @param positionId 持仓ID（如果为空，则适用于所有持仓）
     * @param alertType 警报类型（PROFIT盈利、LOSS亏损）
     * @param threshold 阈值金额
     * @param thresholdPercentage 阈值百分比
     * @return 风险警报
     */
    OptionRiskAlert setStopLoss(Long userId, Long positionId, String alertType,
                               BigDecimal threshold, BigDecimal thresholdPercentage);

    /**
     * 检查并触发风险警报
     */
    void checkAndTriggerRiskAlerts();

    /**
     * 检查并执行强平
     */
    void checkAndLiquidate();

    /**
     * 监控所有持仓风险
     */
    void monitorPositions();
}















