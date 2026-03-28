/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.dto.response.FuturesPositionResponse;
import com.cryptotrade.futures.usdt.entity.FuturesPosition;

import java.math.BigDecimal;
import java.util.List;

public interface PositionService {
    /**
     * 查询用户仓位列表
     * @param userId 用户ID
     * @param pairName 交易对名称（可选，用于筛选）
     */
    List<FuturesPositionResponse> getPositions(Long userId, String pairName);

    /**
     * 查询单个仓位详情
     */
    FuturesPositionResponse getPosition(Long userId, Long positionId);

    /**
     * 计算未实现盈亏
     * @param position 仓位
     * @param currentPrice 当前价格（标记价格）
     * @return 未实现盈亏
     */
    BigDecimal calculateUnrealizedPnl(FuturesPosition position, BigDecimal currentPrice);

    /**
     * 计算保证金率
     * @param position 仓位
     * @param currentPrice 当前价格
     * @return 保证金率
     */
    BigDecimal calculateMarginRatio(FuturesPosition position, BigDecimal currentPrice);

    /**
     * 更新标记价格
     * @param positionId 仓位ID
     */
    void updateMarkPrice(Long positionId);

    /**
     * 批量更新所有仓位的标记价格
     */
    void updateAllMarkPrices();

    /**
     * 更新仓位（开仓/加仓/减仓/平仓）
     * @param position 仓位
     * @param quantity 数量变化（正数表示增加，负数表示减少）
     * @param price 成交价格
     * @param isOpenPosition 是否为开仓（true=开仓/加仓，false=减仓/平仓）
     */
    FuturesPosition updatePosition(FuturesPosition position, BigDecimal quantity, BigDecimal price, boolean isOpenPosition);

    /**
     * 计算强平价格
     * @param position 仓位
     * @return 强平价格
     */
    BigDecimal calculateLiquidationPrice(FuturesPosition position);
}












