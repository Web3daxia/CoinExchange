/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 机器人测算响应DTO
 */
@Data
public class BotSimulationResponse {
    // 预估数据
    private BigDecimal estimatedTradeCoinAverage; // 预估交易币(平均)
    private BigDecimal estimatedTradeCoinSafe; // 预估交易币(安全)
    private BigDecimal estimatedSettlementCoinAverage; // 预估结算币(平均)
    private BigDecimal estimatedSettlementCoinSafe; // 预估结算币(安全)

    // 卖盘数据
    private BigDecimal sellMaxPrice; // 卖盘最高
    private BigDecimal sellMinPrice; // 卖盘最低
    private BigDecimal sellPriceDiff; // 卖盘差价
    private BigDecimal sellPriceDiffPercent; // 差价百分比
    private BigDecimal sellCoinQuantity; // 卖盘币种量(BTC)
    private BigDecimal sellAmount; // 卖盘金额(USDT)

    // 买盘数据
    private BigDecimal buyMaxPrice; // 买盘最高
    private BigDecimal buyMinPrice; // 买盘最低
    private BigDecimal buyPriceDiff; // 买盘差价
    private BigDecimal buyPriceDiffPercent; // 差价百分比
    private BigDecimal buyCoinQuantity; // 买盘币种量(BTC)
    private BigDecimal buyAmount; // 买盘金额(USDT)

    // 盘口数据（50条）
    private List<OrderBookItem> orderBookItems;
}














