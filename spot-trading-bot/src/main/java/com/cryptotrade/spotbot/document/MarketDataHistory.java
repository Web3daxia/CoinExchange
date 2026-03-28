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
 * 市场数据历史记录文档（MongoDB）
 * 存储现货和合约区域的历史行情数据
 */
@Document(collection = "market_data_history")
@Data
public class MarketDataHistory {
    @Id
    private String id;

    private String pairName; // 交易对名称

    private String marketType; // 市场类型: SPOT, FUTURES_USDT, FUTURES_COIN

    private BigDecimal price; // 价格

    private BigDecimal volume; // 成交量

    private BigDecimal amount; // 成交额

    private BigDecimal high; // 最高价

    private BigDecimal low; // 最低价

    private BigDecimal open; // 开盘价

    private BigDecimal close; // 收盘价

    private BigDecimal changePercent; // 涨跌幅%

    private String interval; // K线周期: 1m, 5m, 15m, 30m, 1h, 4h, 1d

    private LocalDateTime timestamp; // 时间戳

    private LocalDateTime createdAt; // 创建时间

    // 索引字段
    private String dateHour; // 日期小时，格式: 2024010110，用于查询优化
    private Long timestampMillis; // 时间戳（毫秒），用于范围查询
}














