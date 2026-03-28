/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理财产品实体
 */
@Entity
@Table(name = "finance_products")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FinanceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName; // 产品名称

    @Column(name = "product_code", unique = true, nullable = false, length = 50)
    private String productCode; // 产品代码（唯一标识）

    @Column(name = "product_type", nullable = false, length = 50)
    private String productType; // 产品类型: FIXED（定期理财）, FLEXIBLE（活期理财）

    @Column(name = "risk_level", nullable = false, length = 50)
    private String riskLevel; // 风险等级: CONSERVATIVE（稳健型）, HIGH_RISK（高风险高收益）

    @Column(name = "annual_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal annualRate; // 年化收益率

    @Column(name = "investment_cycle")
    private Integer investmentCycle; // 投资周期（天数），灵活型为NULL

    @Column(name = "min_investment_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal minInvestmentAmount; // 最小投资金额

    @Column(name = "max_investment_amount", precision = 20, scale = 8)
    private BigDecimal maxInvestmentAmount; // 最大投资金额

    @Column(name = "total_raise_amount", precision = 20, scale = 8)
    private BigDecimal totalRaiseAmount; // 总募集金额

    @Column(name = "invested_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal investedAmount = BigDecimal.ZERO; // 已投资金额

    @Column(name = "available_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal availableAmount = BigDecimal.ZERO; // 剩余可投资金额

    @Column(name = "supported_currency", nullable = false, length = 20)
    private String supportedCurrency; // 支持的投资币种

    @Column(name = "lock_period")
    private Integer lockPeriod; // 锁仓期（天数），活期理财为NULL

    @Column(name = "status", nullable = false, length = 20)
    private String status = "AVAILABLE"; // 产品状态: AVAILABLE, SOLD_OUT, ENDED, SUSPENDED

    @Column(name = "start_time")
    private LocalDateTime startTime; // 产品开始时间

    @Column(name = "end_time")
    private LocalDateTime endTime; // 产品结束时间

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 产品描述

    @Column(name = "risk_warning", columnDefinition = "TEXT")
    private String riskWarning; // 风险提示

    @Column(name = "settlement_method", nullable = false, length = 50)
    private String settlementMethod = "AUTO"; // 结算方式: AUTO, MANUAL

    @Column(name = "settlement_cycle", length = 50)
    private String settlementCycle; // 收益结算周期: DAILY, WEEKLY, MONTHLY, MATURITY

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排序顺序

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














