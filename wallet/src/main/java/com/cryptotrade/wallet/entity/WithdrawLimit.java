/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现限制配置实体（后台管理）
 */
@Entity
@Table(name = "withdraw_limits")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WithdrawLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "chain", nullable = false)
    private String chain; // 链类型

    @Column(name = "min_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal minAmount; // 最小提现金额

    @Column(name = "max_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal maxAmount; // 单笔最大提现金额

    @Column(name = "daily_max_amount", precision = 20, scale = 8)
    private BigDecimal dailyMaxAmount; // 每日最大提现金额

    @Column(name = "fee_rate", precision = 10, scale = 4)
    private BigDecimal feeRate; // 手续费率

    @Column(name = "fixed_fee", precision = 20, scale = 8)
    private BigDecimal fixedFee; // 固定手续费

    @Column(name = "required_confirmations")
    private Integer requiredConfirmations; // 所需确认数

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、DISABLED（禁用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















