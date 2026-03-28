/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 期权合约交易对详细配置实体
 */
@Entity
@Table(name = "options_trading_pairs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionsTradingPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", unique = true, nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "visible", nullable = false)
    private Boolean visible = true; // 是否显示

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // 是否启用

    @Column(name = "contract_name", nullable = false)
    private String contractName; // 合约名称

    @Column(name = "base_currency", nullable = false)
    private String baseCurrency; // 基础币种

    @Column(name = "quote_currency", nullable = false)
    private String quoteCurrency; // 计价货币

    @Column(name = "option_type", nullable = false)
    private String optionType; // 期权类型: CALL（看涨）, PUT（看跌）

    @Column(name = "strike_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal strikePrice; // 行权价格

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate; // 到期日期

    @Column(name = "contract_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal contractValue; // 合约面值

    @Column(name = "premium", precision = 20, scale = 8)
    private BigDecimal premium; // 期权费用

    @Column(name = "open_long_enabled", nullable = false)
    private Boolean openLongEnabled = true; // 是否启用允许开多

    @Column(name = "open_short_enabled", nullable = false)
    private Boolean openShortEnabled = true; // 是否启用允许开空

    @Column(name = "market_open_long_enabled", nullable = false)
    private Boolean marketOpenLongEnabled = true; // 是否启用允许市价开多

    @Column(name = "market_open_short_enabled", nullable = false)
    private Boolean marketOpenShortEnabled = true; // 是否允许市价开空

    @Column(name = "stop_order_enabled", nullable = false)
    private Boolean stopOrderEnabled = true; // 是否启用计划委托

    @Column(name = "advanced_order_enabled", nullable = false)
    private Boolean advancedOrderEnabled = true; // 是否启用其余的高级委托方式

    @Column(name = "tradeable", nullable = false)
    private Boolean tradeable = true; // 是否可交易

    @Column(name = "open_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal openFeeRate; // 开仓手续费率

    @Column(name = "close_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal closeFeeRate; // 平仓手续费率

    @Column(name = "min_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal minQuantity; // 最小张数（数量）

    @Column(name = "max_quantity", precision = 20, scale = 8)
    private BigDecimal maxQuantity; // 最大张数（数量）

    @Column(name = "slippage_type", nullable = false)
    private String slippageType = "PERCENTAGE"; // 滑点类型: PERCENTAGE, FIXED

    @Column(name = "slippage", precision = 10, scale = 6)
    private BigDecimal slippage; // 滑点

    @Column(name = "base_currency_precision", nullable = false)
    private Integer baseCurrencyPrecision = 8; // 基币小数精度

    @Column(name = "currency_precision", nullable = false)
    private Integer currencyPrecision = 8; // 币种小数精度

    @Column(name = "taker_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal takerFeeRate; // Taker费率

    @Column(name = "maker_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal makerFeeRate; // Maker费率

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排序

    @Column(name = "limit_order_lower_bound", precision = 20, scale = 8)
    private BigDecimal limitOrderLowerBound; // 限价委托下限

    @Column(name = "limit_order_upper_bound", precision = 20, scale = 8)
    private BigDecimal limitOrderUpperBound; // 限价委托上限

    @Column(name = "price_valid_time_ms")
    private Long priceValidTimeMs; // 价格有效时间(毫秒)

    @Column(name = "trade_price_spread", precision = 10, scale = 6)
    private BigDecimal tradePriceSpread; // 成交价价差

    @Column(name = "rb_max_order_quantity", precision = 20, scale = 8)
    private BigDecimal rbMaxOrderQuantity; // RB排单最大数量

    @Column(name = "min_trade_time_seconds")
    private Integer minTradeTimeSeconds; // 最小成交时间（秒）

    @Column(name = "rb_max_trade_quantity", precision = 20, scale = 8)
    private BigDecimal rbMaxTradeQuantity; // RB成交最大数量

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














