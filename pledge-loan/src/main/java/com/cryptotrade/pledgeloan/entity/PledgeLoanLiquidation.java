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
 * 质押借币平仓记录实体
 */
@Entity
@Table(name = "pledge_loan_liquidations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanLiquidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 订单ID

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "liquidation_no", unique = true, nullable = false, length = 50)
    private String liquidationNo; // 平仓单号（唯一标识）

    @Column(name = "liquidation_type", nullable = false, length = 20)
    private String liquidationType; // 平仓类型: AUTO, MANUAL

    @Column(name = "liquidation_reason", nullable = false, length = 500)
    private String liquidationReason; // 平仓原因

    @Column(name = "pledge_currency", nullable = false, length = 20)
    private String pledgeCurrency; // 质押币种

    @Column(name = "pledge_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal pledgeAmount; // 质押数量

    @Column(name = "pledge_value_before", precision = 20, scale = 8, nullable = false)
    private BigDecimal pledgeValueBefore; // 平仓前质押价值（USDT）

    @Column(name = "pledge_value_after", precision = 20, scale = 8)
    private BigDecimal pledgeValueAfter; // 平仓后质押价值（USDT）

    @Column(name = "loan_currency", nullable = false, length = 20)
    private String loanCurrency; // 借款币种

    @Column(name = "loan_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal loanAmount; // 借款数量

    @Column(name = "loan_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal loanValue; // 借款价值（USDT）

    @Column(name = "health_rate_before", precision = 10, scale = 6, nullable = false)
    private BigDecimal healthRateBefore; // 平仓前健康度

    @Column(name = "liquidation_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal liquidationPrice; // 平仓价格

    @Column(name = "liquidation_time", nullable = false)
    private LocalDateTime liquidationTime; // 平仓时间

    @Column(name = "liquidator_id")
    private Long liquidatorId; // 平仓操作人ID

    @Column(name = "debt_amount", precision = 20, scale = 8)
    private BigDecimal debtAmount; // 用户欠款金额（借款币种）

    @Column(name = "debt_amount_usdt", precision = 20, scale = 8)
    private BigDecimal debtAmountUsdt; // 用户欠款金额（USDT等值）

    @Column(name = "has_debt", nullable = false)
    private Boolean hasDebt = false; // 是否有欠款

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}













