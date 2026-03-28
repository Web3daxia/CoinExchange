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
 * 机器人交易记录文档（MongoDB）
 */
@Document(collection = "bot_trade_records")
@Data
public class BotTradeRecord {
    @Id
    private String id;

    private String pairName; // 交易对名称，如: BTC/USDT

    private String baseCurrency; // 基础货币

    private String quoteCurrency; // 计价货币

    private String side; // 方向: BUY, SELL

    private BigDecimal price; // 成交价格

    private BigDecimal quantity; // 成交数量

    private BigDecimal amount; // 成交金额

    private String orderType; // 订单类型: MARKET, LIMIT

    private String botOrderId; // 机器人订单ID

    private String userOrderId; // 用户订单ID（如果有撮合）

    private Boolean isMatchedWithUser; // 是否与用户订单撮合

    private Long userId; // 用户ID（如果是与用户撮合）

    private LocalDateTime tradeTime; // 交易时间

    private LocalDateTime createdAt; // 创建时间

    // 索引字段
    private String dateHour; // 日期小时，格式: 2024010110，用于查询优化
}














