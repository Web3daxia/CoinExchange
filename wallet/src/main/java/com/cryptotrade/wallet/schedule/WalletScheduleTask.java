/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.schedule;

import com.cryptotrade.wallet.service.WalletAssetSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 钱包定时任务
 */
@Component
public class WalletScheduleTask {

    @Autowired
    private WalletAssetSummaryService walletAssetSummaryService;

    /**
     * 创建资产快照
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 3600000)
    public void createBalanceSnapshots() {
        // TODO: 获取所有用户并创建快照
        // List<Long> userIds = userService.getAllUserIds();
        // for (Long userId : userIds) {
        //     walletAssetSummaryService.createBalanceSnapshot(userId);
        // }
    }
}















