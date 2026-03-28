/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 控盘机器人配置请求DTO
 */
@Data
public class ControlPanelBotConfigRequest {
    private String pairName; // 交易对名称，如: BTC/USDT

    private String baseCurrency; // 基础货币，如: BTC

    private String quoteCurrency; // 计价货币，如: USDT

    private BigDecimal initialPrice; // 起始价格

    private Integer pricePrecision = 8; // 价格精度（小数位数）

    private Integer quantityPrecision = 8; // 数量精度（小数位数）

    // 下单参数
    private Integer orderIntervalSeconds; // 下单时间间隔（秒）

    private BigDecimal initialOrderQuantity; // 初始订单数量

    private String priceDiffType; // 差价类型: RATIO（比例）, VALUE（值）

    private BigDecimal maxPriceDiff; // 买卖盘最高差价

    private BigDecimal priceChangeStepPercent; // 价格变化步涨%（比例）

    private BigDecimal minTradeQuantity; // 最低交易量

    // 交易量随机因子（7个因子）
    private BigDecimal volumeRandomFactor1;
    private BigDecimal volumeRandomFactor2;
    private BigDecimal volumeRandomFactor3;
    private BigDecimal volumeRandomFactor4;
    private BigDecimal volumeRandomFactor5;
    private BigDecimal volumeRandomFactor6;
    private BigDecimal volumeRandomFactor7;

    // 二十四小时基础信息
    private BigDecimal dailyHigh; // 二十四小时最高价
    private BigDecimal dailyLow; // 二十四小时最低价
    private BigDecimal dailyVolume; // 二十四小时交易量
    private BigDecimal dailyAmount; // 二十四小时交易额

    private String status; // 状态: ACTIVE, INACTIVE
}














