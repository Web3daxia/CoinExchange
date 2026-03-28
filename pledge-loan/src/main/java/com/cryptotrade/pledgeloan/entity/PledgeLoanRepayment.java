/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质押借币还款记录实体
 */
@Entity
@Table(name = "pledge_loan_repayments")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 订单ID

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "repayment_no", unique = true, nullable = false, length = 50)
    private String repaymentNo; // 还款单号（唯一标识）

    @Column(name = "repayment_type", nullable = false, length = 20)
    private String repaymentType; // 还款类型: PRINCIPAL, INTEREST, FULL, PARTIAL

    @Column(name = "principal_amount", precision = 20, scale = 8)
    private BigDecimal principalAmount = BigDecimal.ZERO; // 还款本金

    @Column(name = "interest_amount", precision = 20, scale = 8)
    private BigDecimal interestAmount = BigDecimal.ZERO; // 还款利息

    @Column(name = "total_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalAmount; // 还款总额

    @Column(name = "repayment_currency", nullable = false, length = 20)
    private String repaymentCurrency; // 还款币种

    @Column(name = "repayment_time", nullable = false)
    private LocalDateTime repaymentTime; // 还款时间

    @Column(name = "is_early_repayment", nullable = false)
    private Boolean isEarlyRepayment = false; // 是否提前还款

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














