/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.schedule;

import com.cryptotrade.futures.coin.entity.CoinFuturesContract;
import com.cryptotrade.futures.coin.repository.CoinFuturesContractRepository;
import com.cryptotrade.futures.coin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoinFuturesScheduleTask {

    @Autowired
    private CoinFuturesMarketDataService coinFuturesMarketDataService;

    @Autowired
    private CoinFuturesPositionService coinFuturesPositionService;

    @Autowired
    private CoinFuturesFundingRateService coinFuturesFundingRateService;

    @Autowired
    private CoinFuturesRiskManagementService coinFuturesRiskManagementService;

    @Autowired
    private CoinFuturesAdvancedOrderService coinFuturesAdvancedOrderService;

    @Autowired
    private CoinFuturesGradientService coinFuturesGradientService;

    @Autowired
    private CoinFuturesContractRepository coinFuturesContractRepository;

    @Autowired
    private com.cryptotrade.futures.coin.service.CoinFuturesTradingRobotService coinFuturesTradingRobotService;

    @Autowired
    private com.cryptotrade.futures.coin.service.CoinFuturesStrategyService coinFuturesStrategyService;

    /**
     * 同步市场数据
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void syncMarketData() {
        try {
            List<CoinFuturesContract> activeContracts = coinFuturesContractRepository
                    .findByContractTypeAndStatus("COIN_MARGINED", "ACTIVE");
            
            for (CoinFuturesContract contract : activeContracts) {
                try {
                    coinFuturesMarketDataService.syncMarketData(contract.getPairName());
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
            coinFuturesPositionService.updateAllMarkPrices();
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
            coinFuturesFundingRateService.settleAllFundingFees();
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
            coinFuturesRiskManagementService.monitorPositions();
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
            coinFuturesAdvancedOrderService.executeAdvancedOrders();
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
            coinFuturesGradientService.autoAdjustLeverage();
        } catch (Exception e) {
            System.err.println("自动调整梯度杠杆任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行交易机器人策略
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void executeTradingRobots() {
        try {
            coinFuturesTradingRobotService.executeRobotStrategies();
        } catch (Exception e) {
            System.err.println("执行交易机器人任务失败: " + e.getMessage());
        }
    }

    /**
     * 执行合约策略
     * 每1分钟执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void executeStrategies() {
        try {
            coinFuturesStrategyService.executeAllStrategies();
        } catch (Exception e) {
            System.err.println("执行合约策略任务失败: " + e.getMessage());
        }
    }
}

