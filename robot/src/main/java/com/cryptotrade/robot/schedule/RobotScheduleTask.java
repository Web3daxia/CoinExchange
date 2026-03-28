/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.schedule;

import com.cryptotrade.robot.service.TradingRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 交易机器人定时任务
 * 定时执行机器人策略
 */
@Component
public class RobotScheduleTask {

    @Autowired
    private TradingRobotService tradingRobotService;

    /**
     * 执行机器人策略
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void executeRobotStrategies() {
        try {
            tradingRobotService.executeRobotStrategies();
        } catch (Exception e) {
            System.err.println("执行交易机器人策略任务失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}















