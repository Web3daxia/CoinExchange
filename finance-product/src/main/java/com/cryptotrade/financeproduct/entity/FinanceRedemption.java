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
 * 理财产品赎回记录实体
 */
@Entity
@Table(name = "finance_redemptions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FinanceRedemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "investment_id", nullable = false)
    private Long investmentId; // 投资记录ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "product_id", nullable = false)
    private Long productId; // 理财产品ID

    @Column(name = "redemption_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal redemptionAmount; // 赎回金额

    @Column(name = "principal_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal principalAmount; // 赎回本金

    @Column(name = "profit_amount", precision = 20, scale = 8)
    private BigDecimal profitAmount = BigDecimal.ZERO; // 赎回收益

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 币种

    @Column(name = "redemption_type", nullable = false, length = 50)
    private String redemptionType; // 赎回类型: FULL, PARTIAL, AUTO

    @Column(name = "redemption_fee", precision = 20, scale = 8)
    private BigDecimal redemptionFee = BigDecimal.ZERO; // 赎回手续费

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING"; // 赎回状态: PENDING, PROCESSING, COMPLETED, FAILED

    @Column(name = "redemption_order_no", unique = true, nullable = false, length = 50)
    private String redemptionOrderNo; // 赎回订单号

    @Column(name = "completed_time")
    private LocalDateTime completedTime; // 完成时间

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














