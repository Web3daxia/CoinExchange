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
 * 质押借币订单实体
 */
@Entity
@Table(name = "pledge_loan_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo; // 订单号（唯一标识）

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "pledge_currency", nullable = false, length = 20)
    private String pledgeCurrency; // 质押币种

    @Column(name = "pledge_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal pledgeAmount; // 质押数量

    @Column(name = "pledge_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal pledgeValue; // 质押价值（USDT）

    @Column(name = "loan_currency", nullable = false, length = 20)
    private String loanCurrency; // 借款币种

    @Column(name = "loan_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal loanAmount; // 借款数量

    @Column(name = "loan_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal loanValue; // 借款价值（USDT）

    @Column(name = "interest_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal interestRate; // 借款利率（年化）

    @Column(name = "loan_term_days", nullable = false)
    private Integer loanTermDays; // 借款期限（天）

    @Column(name = "pledge_ratio", precision = 10, scale = 4, nullable = false)
    private BigDecimal pledgeRatio; // 质押比例（质押价值/借款价值）

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING"; // 订单状态: PENDING, APPROVED, REJECTED, ACTIVE, REPAID, LIQUIDATED, CLOSED

    @Column(name = "approval_status", length = 20)
    private String approvalStatus; // 审批状态: AUTO, MANUAL, PENDING

    @Column(name = "approver_id")
    private Long approverId; // 审批人ID

    @Column(name = "approval_time")
    private LocalDateTime approvalTime; // 审批时间

    @Column(name = "approval_remark", length = 500)
    private String approvalRemark; // 审批备注

    @Column(name = "start_time")
    private LocalDateTime startTime; // 借款开始时间

    @Column(name = "end_time")
    private LocalDateTime endTime; // 借款到期时间

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime; // 实际还款时间

    @Column(name = "total_interest", precision = 20, scale = 8)
    private BigDecimal totalInterest = BigDecimal.ZERO; // 总利息（累计应付利息）

    @Column(name = "paid_interest", precision = 20, scale = 8)
    private BigDecimal paidInterest = BigDecimal.ZERO; // 已付利息

    @Column(name = "remaining_principal", precision = 20, scale = 8, nullable = false)
    private BigDecimal remainingPrincipal; // 剩余本金

    @Column(name = "liquidation_price", precision = 20, scale = 8)
    private BigDecimal liquidationPrice; // 平仓价格

    @Column(name = "health_rate", precision = 10, scale = 6)
    private BigDecimal healthRate; // 健康度（质押价值/借款价值）

    @Column(name = "last_health_check_time")
    private LocalDateTime lastHealthCheckTime; // 上次健康度检查时间

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














