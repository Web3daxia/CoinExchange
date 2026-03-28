/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质押币种配置实体
 */
@Entity
@Table(name = "pledge_currency_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeCurrencyConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", unique = true, nullable = false, length = 20)
    private String currencyCode; // 币种代码

    @Column(name = "currency_name", nullable = false, length = 100)
    private String currencyName; // 币种名称

    @Column(name = "loan_ratio", nullable = false, precision = 10, scale = 4)
    private BigDecimal loanRatio; // 借款比例（质押比例），如: 0.5000 表示50%

    @Column(name = "interest_rate", nullable = false, precision = 10, scale = 6)
    private BigDecimal interestRate; // 借款利率（年化），如: 0.050000 表示5%

    @Column(name = "min_pledge_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal minPledgeAmount = BigDecimal.ZERO; // 最小质押金额

    @Column(name = "max_loan_amount", precision = 20, scale = 8)
    private BigDecimal maxLoanAmount; // 最大借款额度（单个用户）

    @Column(name = "risk_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal riskRate = new BigDecimal("0.8000"); // 风险率（平仓线），如: 0.8000 表示80%

    @Column(name = "maintenance_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal maintenanceRate = new BigDecimal("0.8500"); // 维持质押率（预警线），如: 0.8500 表示85%

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排序顺序

    @Column(name = "description", length = 500)
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














