/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 现货交易机器人配置实体
 */
@Entity
@Table(name = "spot_trading_bot_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SpotTradingBotConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", unique = true, nullable = false)
    private String pairName; // 交易对名称，如: BTC/USDT

    @Column(name = "base_currency", nullable = false)
    private String baseCurrency; // 基础货币，如: BTC

    @Column(name = "quote_currency", nullable = false)
    private String quoteCurrency; // 计价货币，如: USDT

    // 下单参数
    @Column(name = "order_interval_seconds", nullable = false)
    private Integer orderIntervalSeconds; // 下单时间间隔（秒）

    @Column(name = "initial_order_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialOrderQuantity; // 初始订单数量

    @Column(name = "price_precision", nullable = false)
    private Integer pricePrecision; // 价格精度要求（小数位数）

    @Column(name = "quantity_precision", nullable = false)
    private Integer quantityPrecision; // 数量精度要求（小数位数）

    @Column(name = "price_diff_type", nullable = false)
    private String priceDiffType; // 差价类型: RATIO（比例）, VALUE（值）

    @Column(name = "max_price_diff", precision = 20, scale = 8, nullable = false)
    private BigDecimal maxPriceDiff; // 买卖盘最高差价

    @Column(name = "price_change_step_percent", precision = 10, scale = 4)
    private BigDecimal priceChangeStepPercent; // 价格变化步涨%（比例）

    @Column(name = "min_trade_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal minTradeQuantity; // 最低交易量

    // 交易量随机因子（7个因子，总和应为100%）
    @Column(name = "volume_random_factor_1", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor1; // 交易量随机因子%1

    @Column(name = "volume_random_factor_2", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor2; // 交易量随机因子%2

    @Column(name = "volume_random_factor_3", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor3; // 交易量随机因子%3

    @Column(name = "volume_random_factor_4", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor4; // 交易量随机因子%4

    @Column(name = "volume_random_factor_5", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor5; // 交易量随机因子%5

    @Column(name = "volume_random_factor_6", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor6; // 交易量随机因子%6

    @Column(name = "volume_random_factor_7", precision = 5, scale = 2)
    private BigDecimal volumeRandomFactor7; // 交易量随机因子%7

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 币种当前价格

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














