/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 币种实体
 */
@Entity
@Table(name = "currencies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", unique = true, nullable = false)
    private String currencyCode; // BTC, ETH, USDT等

    @Column(name = "currency_name", nullable = false)
    private String currencyName; // 币种名称

    @Column(name = "symbol")
    private String symbol; // 符号，如: ₿, Ξ

    @Column(name = "icon_url")
    private String iconUrl; // 图标URL

    @Column(name = "decimals", nullable = false)
    private Integer decimals = 8; // 小数位数

    @Column(name = "min_withdraw_amount", precision = 20, scale = 8)
    private BigDecimal minWithdrawAmount; // 最小提现金额

    @Column(name = "max_withdraw_amount", precision = 20, scale = 8)
    private BigDecimal maxWithdrawAmount; // 最大提现金额

    @Column(name = "withdraw_fee", precision = 20, scale = 8)
    private BigDecimal withdrawFee; // 提现手续费

    @Column(name = "deposit_enabled", nullable = false)
    private Boolean depositEnabled = true; // 是否启用充值

    @Column(name = "withdraw_enabled", nullable = false)
    private Boolean withdrawEnabled = true; // 是否启用提现

    // 交易区启用状态
    @Column(name = "spot_enabled", nullable = false)
    private Boolean spotEnabled = false; // 现货交易区是否启用

    @Column(name = "futures_usdt_enabled", nullable = false)
    private Boolean futuresUsdtEnabled = false; // USDT本位合约是否启用

    @Column(name = "futures_coin_enabled", nullable = false)
    private Boolean futuresCoinEnabled = false; // 币本位合约是否启用

    @Column(name = "options_enabled", nullable = false)
    private Boolean optionsEnabled = false; // 期权交易是否启用

    @Column(name = "leveraged_enabled", nullable = false)
    private Boolean leveragedEnabled = false; // 杠杆交易是否启用

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排序顺序

    @Column(name = "description")
    private String description; // 币种描述

    @Column(name = "agent_id")
    private Long agentId; // 币种所属代理商ID（关联agents表，可不填）

    @Column(name = "currency_unit", length = 20)
    private String currencyUnit; // 币种单位

    @Column(name = "total_supply", precision = 30, scale = 8)
    private BigDecimal totalSupply; // 总数量（总供应量）

    @Column(name = "detail_url", length = 500)
    private String detailUrl; // 详情链接

    @Column(name = "listing_date")
    private LocalDate listingDate; // 上线日期

    @Column(name = "logo_url", length = 500)
    private String logoUrl; // 币种logo URL（如果icon_url为空则使用此字段）

    @Column(name = "logo_file_path", length = 500)
    private String logoFilePath; // 币种logo文件路径

    @Column(name = "intro", columnDefinition = "TEXT")
    private String intro; // 币种简介

    @Column(name = "base_exchange_rate_cny", precision = 20, scale = 8)
    private BigDecimal baseExchangeRateCny; // 基础汇率（相对于人民币CNY）

    @Column(name = "base_exchange_rate_usd", precision = 20, scale = 8)
    private BigDecimal baseExchangeRateUsd; // 基础汇率（相对于美元USD）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

