/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service;

import com.cryptotrade.deliverycontract.entity.DeliveryPosition;

import java.util.List;
import java.util.Map;

/**
 * 交割合约风险管理服务接口
 */
public interface DeliveryRiskManagementService {
    /**
     * 查询账户风险状态
     */
    Map<String, Object> getAccountRiskStatus(Long userId);

    /**
     * 查询强平记录
     */
    List<Map<String, Object>> getLiquidationRecords(Long userId, Long contractId, String startTime, String endTime);

    /**
     * 查询风险警报
     */
    List<Map<String, Object>> getRiskAlerts(Long userId);

    /**
     * 检查强制平仓条件
     */
    void checkLiquidation(Long userId, Long positionId);

    /**
     * 执行强制平仓
     */
    DeliveryPosition executeLiquidation(Long userId, Long positionId, String reason);
}













