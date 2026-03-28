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
 * 用户理财产品投资记录实体
 */
@Entity
@Table(name = "finance_investments")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FinanceInvestment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "product_id", nullable = false)
    private Long productId; // 理财产品ID

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode; // 产品代码（冗余字段）

    @Column(name = "investment_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal investmentAmount; // 投资金额

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 投资币种

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 投资开始时间

    @Column(name = "end_date")
    private LocalDateTime endDate; // 投资结束时间（定期理财），活期理财为NULL

    @Column(name = "expected_profit", precision = 20, scale = 8)
    private BigDecimal expectedProfit = BigDecimal.ZERO; // 预期收益

    @Column(name = "actual_profit", precision = 20, scale = 8)
    private BigDecimal actualProfit = BigDecimal.ZERO; // 实际收益

    @Column(name = "accumulated_profit", precision = 20, scale = 8)
    private BigDecimal accumulatedProfit = BigDecimal.ZERO; // 累计收益

    @Column(name = "last_settlement_time")
    private LocalDateTime lastSettlementTime; // 上次结算时间

    @Column(name = "next_settlement_time")
    private LocalDateTime nextSettlementTime; // 下次结算时间

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 投资状态: ACTIVE, MATURED, CANCELLED, REDEEMED

    @Column(name = "lock_until")
    private LocalDateTime lockUntil; // 锁仓到期时间

    @Column(name = "redeemed_amount", precision = 20, scale = 8)
    private BigDecimal redeemedAmount = BigDecimal.ZERO; // 已赎回金额

    @Column(name = "remaining_principal", precision = 20, scale = 8, nullable = false)
    private BigDecimal remainingPrincipal; // 剩余本金

    @Column(name = "investment_order_no", unique = true, nullable = false, length = 50)
    private String investmentOrderNo; // 投资订单号

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














