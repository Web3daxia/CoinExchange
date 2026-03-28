/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.schedule;

import com.cryptotrade.leveraged.service.LeveragedOrderService;
import com.cryptotrade.leveraged.service.LeveragedPositionService;
import com.cryptotrade.leveraged.service.LeveragedRiskManagementService;
import com.cryptotrade.leveraged.service.LeveragedStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 杠杆交易定时任务
 */
@Component
public class LeveragedScheduleTask {

    @Autowired
    private LeveragedOrderService leveragedOrderService;

    @Autowired
    private LeveragedPositionService leveragedPositionService;

    @Autowired
    private LeveragedRiskManagementService leveragedRiskManagementService;

    @Autowired
    private LeveragedStrategyService leveragedStrategyService;

    /**
     * 检查并触发条件单
     * 每10秒执行一次（已在LeveragedOrderService中实现）
     */
    @Scheduled(fixedRate = 10000)
    public void checkConditionalOrders() {
        try {
            leveragedOrderService.checkAndTriggerConditionalOrders();
        } catch (Exception e) {
            System.err.println("检查条件单任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 更新所有仓位盈亏
     * 每30秒执行一次（已在LeveragedPositionService中实现）
     */
    @Scheduled(fixedRate = 30000)
    public void updatePositionsPnl() {
        try {
            leveragedPositionService.updateAllPositionsPnl();
        } catch (Exception e) {
            System.err.println("更新仓位盈亏任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 检查并触发风险警报
     * 每30秒执行一次（已在LeveragedRiskManagementService中实现）
     */
    @Scheduled(fixedRate = 30000)
    public void checkRiskAlerts() {
        try {
            leveragedRiskManagementService.checkAndTriggerRiskAlerts();
        } catch (Exception e) {
            System.err.println("检查风险警报任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 检查并执行强平
     * 每分钟执行一次（已在LeveragedRiskManagementService中实现）
     */
    @Scheduled(fixedRate = 60000)
    public void checkLiquidation() {
        try {
            leveragedRiskManagementService.checkAndLiquidate();
        } catch (Exception e) {
            System.err.println("检查强平任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 监控仓位风险
     * 每30秒执行一次（已在LeveragedRiskManagementService中实现）
     */
    @Scheduled(fixedRate = 30000)
    public void monitorPositions() {
        try {
            leveragedRiskManagementService.monitorPositions();
        } catch (Exception e) {
            System.err.println("监控仓位风险任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行所有活跃策略
     * 每30秒执行一次（已在LeveragedStrategyService中实现）
     */
    @Scheduled(fixedRate = 30000)
    public void executeStrategies() {
        try {
            leveragedStrategyService.executeAllStrategies();
        } catch (Exception e) {
            System.err.println("执行策略任务失败: " + e.getMessage());
        }
    }
}















