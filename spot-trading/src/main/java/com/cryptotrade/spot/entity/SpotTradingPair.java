/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 现货交易对详细配置实体
 */
@Entity
@Table(name = "spot_trading_pairs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SpotTradingPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", unique = true, nullable = false)
    private String pairName; // 交易对名称，如: BTC/USDT

    @Column(name = "trade_currency", nullable = false)
    private String tradeCurrency; // 交易币种，如: BTC

    @Column(name = "settlement_currency", nullable = false)
    private String settlementCurrency; // 结算币种，如: USDT

    @Column(name = "trading_fee", precision = 10, scale = 6, nullable = false)
    private BigDecimal tradingFee = new BigDecimal("0.001"); // 交易手续费，如: 0.001

    @Column(name = "currency_precision", nullable = false)
    private Integer currencyPrecision = 8; // 币种精度（小数位数）

    @Column(name = "base_currency_precision", nullable = false)
    private Integer baseCurrencyPrecision = 8; // 基币小数单位（小数位数）

    @Column(name = "min_sell_price", precision = 20, scale = 8)
    private BigDecimal minSellPrice; // 最低卖单价

    @Column(name = "max_buy_price", precision = 20, scale = 8)
    private BigDecimal maxBuyPrice; // 最高买单价

    @Column(name = "min_order_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal minOrderQuantity; // 最小下单量

    @Column(name = "max_order_quantity", precision = 20, scale = 8)
    private BigDecimal maxOrderQuantity; // 最大下单量

    @Column(name = "trading_area", nullable = false)
    private String tradingArea = "TRADING"; // 交易区类型: TRADING（交易区）, INNOVATION（创新板）

    @Column(name = "min_order_amount", precision = 20, scale = 8)
    private BigDecimal minOrderAmount; // 最小挂单额

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 交易对排序

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // 是否启用

    @Column(name = "frontend_visible", nullable = false)
    private Boolean frontendVisible = true; // 前端是否显示

    @Column(name = "tradeable", nullable = false)
    private Boolean tradeable = true; // 是否可交易

    @Column(name = "market_sell_enabled", nullable = false)
    private Boolean marketSellEnabled = true; // 是否可以市价卖

    @Column(name = "market_buy_enabled", nullable = false)
    private Boolean marketBuyEnabled = true; // 是否可以市价买

    @Column(name = "buy_enabled", nullable = false)
    private Boolean buyEnabled = true; // 是否可买

    @Column(name = "sell_enabled", nullable = false)
    private Boolean sellEnabled = true; // 是否可卖

    @Column(name = "public_visible", nullable = false)
    private Boolean publicVisible = true; // 是否对外显示

    @Column(name = "display_ratio", precision = 5, scale = 2)
    private BigDecimal displayRatio = new BigDecimal("100.00"); // 显示的比例（%）

    @Column(name = "bot_enabled", nullable = false)
    private Boolean botEnabled = false; // 是否马上启用交易机器人

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














