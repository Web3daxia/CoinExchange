/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * K线数据请求DTO
 * K线周期最短为5分钟，最长为1周（1w）
 * K线的时段与行情时段相同，即K线数据的开盘时间和收盘时间对应实际的行情时间段
 */
@Data
public class KlineDataRequest {
    private String pairName; // 交易对名称，如: BTC/USDT

    /**
     * K线周期: 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）
     * 5m = 5分钟, 15m = 15分钟, 30m = 30分钟, 1h = 1小时, 4h = 4小时, 1d = 1天, 1w = 1周
     */
    private String interval;

    private LocalDateTime openTime; // 开盘时间（对应行情时段的开始时间）

    private LocalDateTime closeTime; // 收盘时间（对应行情时段的结束时间）

    private BigDecimal open; // 开盘价

    private BigDecimal high; // 最高价

    private BigDecimal low; // 最低价

    private BigDecimal close; // 收盘价

    private BigDecimal volume; // 成交量

    private BigDecimal amount; // 成交额
}
