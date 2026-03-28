/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.schedule;

import com.cryptotrade.futures.usdt.entity.FuturesContract;
import com.cryptotrade.futures.usdt.repository.FuturesContractRepository;
import com.cryptotrade.futures.usdt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FuturesScheduleTask {

    @Autowired
    private FuturesMarketDataService futuresMarketDataService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private FundingRateService fundingRateService;

    @Autowired
    private RiskManagementService riskManagementService;

    @Autowired
    private FuturesAdvancedOrderService futuresAdvancedOrderService;

    @Autowired
    private GradientService gradientService;

    @Autowired
    private FuturesContractRepository futuresContractRepository;

    /**
     * 同步市场数据
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void syncMarketData() {
        try {
            List<FuturesContract> activeContracts = futuresContractRepository
                    .findByContractTypeAndStatus("USDT_MARGINED", "ACTIVE");
            
            for (FuturesContract contract : activeContracts) {
                try {
                    futuresMarketDataService.syncMarketData(contract.getPairName());
                } catch (Exception e) {
                    // 记录错误，继续处理下一个合约
                    System.err.println("同步市场数据失败: " + contract.getPairName() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("同步市场数据任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 更新标记价格
     * 每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void updateMarkPrices() {
        try {
            positionService.updateAllMarkPrices();
        } catch (Exception e) {
            System.err.println("更新标记价格任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 结算资金费用
     * 每8小时执行一次（0:00, 8:00, 16:00）
     */
    @Scheduled(cron = "0 0 0,8,16 * * ?")
    public void settleFundingFees() {
        try {
            fundingRateService.settleAllFundingFees();
        } catch (Exception e) {
            System.err.println("结算资金费用任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 监控仓位风险
     * 每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void monitorPositions() {
        try {
            riskManagementService.monitorPositions();
        } catch (Exception e) {
            System.err.println("监控仓位风险任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行高级订单
     * 每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void executeAdvancedOrders() {
        try {
            futuresAdvancedOrderService.executeAdvancedOrders();
        } catch (Exception e) {
            System.err.println("执行高级订单任务失败: " + e.getMessage());
        }
    }

    /**
     * 自动调整梯度杠杆
     * 每1分钟执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void autoAdjustLeverage() {
        try {
            gradientService.autoAdjustLeverage();
        } catch (Exception e) {
            System.err.println("自动调整梯度杠杆任务执行失败: " + e.getMessage());
        }
    }
}















