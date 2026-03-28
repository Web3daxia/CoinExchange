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
 * 期权合约实体
 */
@Entity
@Table(name = "option_contracts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 如 BTC/USDT

    @Column(name = "base_currency", nullable = false)
    private String baseCurrency; // 如 BTC

    @Column(name = "quote_currency", nullable = false)
    private String quoteCurrency; // 如 USDT

    @Column(name = "option_type", nullable = false)
    private String optionType; // CALL（看涨期权）或 PUT（看跌期权）

    @Column(name = "exercise_type", nullable = false)
    private String exerciseType; // AMERICAN（美式期权）或 EUROPEAN（欧式期权）

    @Column(name = "strike_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal strikePrice; // 执行价格

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate; // 到期日期

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 当前期权价格（期权费）

    @Column(name = "underlying_price", precision = 20, scale = 8)
    private BigDecimal underlyingPrice; // 标的资产当前价格

    @Column(name = "theoretical_price", precision = 20, scale = 8)
    private BigDecimal theoreticalPrice; // 理论价格（通过定价模型计算）

    @Column(name = "implied_volatility", precision = 10, scale = 8)
    private BigDecimal impliedVolatility; // 隐含波动率

    @Column(name = "volume_24h", precision = 20, scale = 8)
    private BigDecimal volume24h; // 24小时交易量

    @Column(name = "open_interest", precision = 20, scale = 8)
    private BigDecimal openInterest; // 持仓量

    @Column(name = "status")
    private String status; // ACTIVE（活跃）、EXPIRED（已到期）、SETTLED（已结算）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

