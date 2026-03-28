/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理财产品收益结算记录实体
 */
@Entity
@Table(name = "finance_profit_settlements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FinanceProfitSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "investment_id", nullable = false)
    private Long investmentId; // 投资记录ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "product_id", nullable = false)
    private Long productId; // 理财产品ID

    @Column(name = "settlement_period_start", nullable = false)
    private LocalDateTime settlementPeriodStart; // 结算周期开始时间

    @Column(name = "settlement_period_end", nullable = false)
    private LocalDateTime settlementPeriodEnd; // 结算周期结束时间

    @Column(name = "principal_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal principalAmount; // 本金金额（结算周期内的平均本金）

    @Column(name = "profit_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal profitAmount; // 收益金额

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 币种

    @Column(name = "annual_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal annualRate; // 年化收益率（结算时的利率）

    @Column(name = "settlement_type", nullable = false, length = 50)
    private String settlementType; // 结算类型: INTEREST, PRINCIPAL, BOTH

    @Column(name = "settlement_status", nullable = false, length = 20)
    private String settlementStatus = "PENDING"; // 结算状态: PENDING, SETTLED, FAILED

    @Column(name = "settlement_time")
    private LocalDateTime settlementTime; // 结算时间

    @Column(name = "settlement_order_no", length = 50)
    private String settlementOrderNo; // 结算订单号

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














