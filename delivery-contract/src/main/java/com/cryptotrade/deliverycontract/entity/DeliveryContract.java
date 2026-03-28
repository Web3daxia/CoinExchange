/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交割合约实体
 */
@Entity
@Table(name = "delivery_contracts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_symbol", unique = true, nullable = false)
    private String contractSymbol; // 合约交易对，如 BTC_USDT

    @Column(name = "contract_type", nullable = false)
    private String contractType; // 合约类型: USDT_MARGINED（USDT本位）、COIN_MARGINED（币本位）

    @Column(name = "underlying_asset", nullable = false)
    private String underlyingAsset; // 标的资产，如 BTC, ETH

    @Column(name = "quote_currency", nullable = false)
    private String quoteCurrency; // 计价货币，如 USDT

    @Column(name = "contract_unit", precision = 20, scale = 8, nullable = false)
    private BigDecimal contractUnit; // 合约单位

    @Column(name = "delivery_cycle", nullable = false)
    private String deliveryCycle; // 交割周期: HOURLY（每小时）、DAILY（每天）、WEEKLY（每周）、MONTHLY（每月）

    @Column(name = "max_leverage", precision = 10, scale = 2, nullable = false)
    private BigDecimal maxLeverage; // 最大杠杆倍数

    @Column(name = "maker_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal makerFeeRate; // Maker手续费率

    @Column(name = "taker_fee_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal takerFeeRate; // Taker手续费率

    @Column(name = "initial_margin_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal initialMarginRate; // 初始保证金率

    @Column(name = "maintenance_margin_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal maintenanceMarginRate; // 维持保证金率

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime; // 交割时间

    @Column(name = "settlement_price", precision = 20, scale = 8)
    private BigDecimal settlementPrice; // 结算价格

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）、SETTLED（已结算）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















