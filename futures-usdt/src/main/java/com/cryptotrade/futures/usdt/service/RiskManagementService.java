/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.entity.FuturesPosition;

import java.util.List;

public interface RiskManagementService {
    /**
     * 检查是否需要强平
     * @param position 仓位
     * @return true如果需要强平，false否则
     */
    boolean checkLiquidation(FuturesPosition position);

    /**
     * 执行强平
     * @param positionId 仓位ID
     */
    void executeLiquidation(Long positionId);

    /**
     * 监控仓位风险（定时任务调用）
     */
    void monitorPositions();

    /**
     * 检查追加保证金警告
     * @param position 仓位
     * @return true如果需要追加保证金，false否则
     */
    boolean checkMarginCall(FuturesPosition position);

    /**
     * 获取高风险仓位列表
     * @return 高风险仓位列表
     */
    List<FuturesPosition> getHighRiskPositions();
}















