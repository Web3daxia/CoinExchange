/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.schedule;

import com.cryptotrade.spot.entity.TradingPair;
import com.cryptotrade.spot.repository.TradingPairRepository;
import com.cryptotrade.spot.service.AdvancedOrderService;
import com.cryptotrade.spot.service.MarketAlertService;
import com.cryptotrade.spot.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradingScheduleTask {

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private AdvancedOrderService advancedOrderService;

    @Autowired
    private MarketAlertService marketAlertService;

    @Autowired
    private TradingPairRepository tradingPairRepository;

    /**
     * 同步市场数据
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void syncMarketData() {
        try {
            List<TradingPair> activePairs = tradingPairRepository.findByStatus("ACTIVE");
            for (TradingPair pair : activePairs) {
                try {
                    marketDataService.syncMarketData(pair.getPairName());
                } catch (Exception e) {
                    // 记录错误，继续处理下一个交易对
                    System.err.println("同步市场数据失败: " + pair.getPairName() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("同步市场数据任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行高级订单
     * 每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void executeAdvancedOrders() {
        try {
            advancedOrderService.executeAdvancedOrders();
        } catch (Exception e) {
            System.err.println("执行高级订单任务失败: " + e.getMessage());
        }
    }

    /**
     * 检查市场异动提醒
     * 每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void checkMarketAlerts() {
        try {
            List<TradingPair> activePairs = tradingPairRepository.findByStatus("ACTIVE");
            for (TradingPair pair : activePairs) {
                try {
                    marketAlertService.checkAndTriggerAlerts(pair.getPairName());
                } catch (Exception e) {
                    // 记录错误，继续处理下一个交易对
                    System.err.println("检查市场异动提醒失败: " + pair.getPairName() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("检查市场异动提醒任务执行失败: " + e.getMessage());
        }
    }
}















