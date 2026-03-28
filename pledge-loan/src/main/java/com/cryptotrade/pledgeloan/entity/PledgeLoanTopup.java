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
 * 质押借币补仓记录实体
 */
@Entity
@Table(name = "pledge_loan_topups")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanTopup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 订单ID

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "topup_no", unique = true, nullable = false, length = 50)
    private String topupNo; // 补仓单号（唯一标识）

    @Column(name = "pledge_currency", nullable = false, length = 20)
    private String pledgeCurrency; // 质押币种

    @Column(name = "topup_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal topupAmount; // 补仓数量

    @Column(name = "topup_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal topupValue; // 补仓价值（USDT）

    @Column(name = "health_rate_before", precision = 10, scale = 6, nullable = false)
    private BigDecimal healthRateBefore; // 补仓前健康度

    @Column(name = "health_rate_after", precision = 10, scale = 6, nullable = false)
    private BigDecimal healthRateAfter; // 补仓后健康度

    @Column(name = "topup_time", nullable = false)
    private LocalDateTime topupTime; // 补仓时间

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














