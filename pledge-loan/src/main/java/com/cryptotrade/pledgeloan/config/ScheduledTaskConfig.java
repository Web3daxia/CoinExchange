/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.config;

import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务配置
 * 用于定期检查订单健康度和处理风险订单
 */
@Configuration
@EnableScheduling
public class ScheduledTaskConfig {

    @Autowired
    private PledgeLoanOrderService orderService;

    /**
     * 每5分钟检查一次订单健康度
     */
    @Scheduled(fixedRate = 300000) // 5分钟 = 300000毫秒
    public void checkOrderHealthRate() {
        try {
            orderService.checkAndProcessRiskOrders();
        } catch (Exception e) {
            System.err.println("检查订单健康度失败: " + e.getMessage());
        }
    }
}














