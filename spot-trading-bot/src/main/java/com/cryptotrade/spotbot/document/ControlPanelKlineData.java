/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 控盘机器人K线数据文档（MongoDB）
 * 存储用户绘制的K线行情数据
 */
@Document(collection = "control_panel_kline_data")
@Data
public class ControlPanelKlineData {
    @Id
    private String id;

    private String pairName; // 交易对名称，如: BTC/USDT

    private String interval; // K线周期: 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）

    private LocalDateTime openTime; // 开盘时间（对应行情时段的开始时间）

    private LocalDateTime closeTime; // 收盘时间（对应行情时段的结束时间）

    private BigDecimal open; // 开盘价

    private BigDecimal high; // 最高价

    private BigDecimal low; // 最低价

    private BigDecimal close; // 收盘价

    private BigDecimal volume; // 成交量

    private BigDecimal amount; // 成交额

    private Long openTimeMillis; // 开盘时间戳（毫秒），用于查询优化

    private Long closeTimeMillis; // 收盘时间戳（毫秒），用于查询优化

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间
}

