/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import java.util.Map;

/**
 * 钱包资产汇总服务接口
 */
public interface WalletAssetSummaryService {
    /**
     * 获取资产汇总数据（用于图表展示）
     * @param userId 用户ID
     * @param period 周期（REAL_TIME, TODAY, TWO_DAYS, THREE_DAYS, WEEK, MONTH, THREE_MONTHS, HALF_YEAR, YEAR）
     * @param currencyType 货币类型（CRYPTO, FIAT）
     * @return 资产汇总数据
     */
    Map<String, Object> getAssetSummary(Long userId, String period, String currencyType);

    /**
     * 创建资产快照（定时任务调用）
     * @param userId 用户ID
     */
    void createBalanceSnapshot(Long userId);
}















