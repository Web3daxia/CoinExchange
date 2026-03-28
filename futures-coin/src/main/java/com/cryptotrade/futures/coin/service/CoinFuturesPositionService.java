/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.response.CoinFuturesPositionResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;

import java.math.BigDecimal;
import java.util.List;

public interface CoinFuturesPositionService {
    /**
     * 查询用户仓位列表
     */
    List<CoinFuturesPositionResponse> getPositions(Long userId);

    /**
     * 查询单个仓位详情
     */
    CoinFuturesPositionResponse getPosition(Long userId, Long positionId);

    /**
     * 计算未实现盈亏
     * @param position 仓位
     * @param currentPrice 当前价格（标记价格）
     * @return 未实现盈亏
     */
    BigDecimal calculateUnrealizedPnl(CoinFuturesPosition position, BigDecimal currentPrice);

    /**
     * 计算保证金率
     * @param position 仓位
     * @param currentPrice 当前价格
     * @return 保证金率
     */
    BigDecimal calculateMarginRatio(CoinFuturesPosition position, BigDecimal currentPrice);

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
     * @return 更新后的仓位
     */
    CoinFuturesPosition updatePosition(CoinFuturesPosition position, BigDecimal quantity, BigDecimal price, boolean isOpenPosition);

    /**
     * 计算强平价格
     * @param position 仓位
     * @return 强平价格
     */
    BigDecimal calculateLiquidationPrice(CoinFuturesPosition position);
}















