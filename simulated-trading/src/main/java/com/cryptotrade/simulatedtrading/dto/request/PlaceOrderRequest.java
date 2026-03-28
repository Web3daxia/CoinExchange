/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 下单请求DTO
 */
@Data
public class PlaceOrderRequest {
    private Long accountId; // 账户ID
    private String tradeType; // 交易类型: SPOT, FUTURES_USDT, FUTURES_COIN, DELIVERY, LEVERAGE, OPTIONS
    private String contractType; // 合约类型（用于合约交易）
    private String pairName; // 交易对
    private String orderType; // 订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT等
    private String side; // 买卖方向: BUY, SELL
    private Integer leverage; // 杠杆倍数
    private BigDecimal quantity; // 交易数量
    private BigDecimal price; // 交易价格（限价单）
    private BigDecimal stopLossPrice; // 止损价格
    private BigDecimal takeProfitPrice; // 止盈价格
}














