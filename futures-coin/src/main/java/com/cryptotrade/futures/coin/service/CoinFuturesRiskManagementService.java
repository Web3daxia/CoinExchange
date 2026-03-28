/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.response.CoinFuturesAccountRiskResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;

import java.util.List;

public interface CoinFuturesRiskManagementService {
    /**
     * 检查是否需要强平
     * @param position 仓位
     * @return true if liquidation is needed, false otherwise
     */
    boolean checkLiquidation(CoinFuturesPosition position);

    /**
     * 执行强平
     * @param positionId 仓位ID
     */
    void executeLiquidation(Long positionId);

    /**
     * 监控仓位风险（定时任务）
     */
    void monitorPositions();

    /**
     * 检查追加保证金警告
     * @param position 仓位
     * @return true if margin call warning is needed, false otherwise
     */
    boolean checkMarginCall(CoinFuturesPosition position);

    /**
     * 获取高风险仓位列表
     * @return 高风险仓位列表
     */
    List<CoinFuturesPosition> getHighRiskPositions();

    /**
     * 查询账户风险
     * @param userId 用户ID
     * @return 账户风险信息
     */
    CoinFuturesAccountRiskResponse getAccountRisk(Long userId);
}
