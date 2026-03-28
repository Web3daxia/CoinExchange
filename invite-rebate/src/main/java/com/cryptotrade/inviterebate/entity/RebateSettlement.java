/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 返佣结算记录实体
 */
@Entity
@Table(name = "rebate_settlements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RebateSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_no", unique = true, nullable = false)
    private String settlementNo; // 结算单号

    @Column(name = "inviter_id", nullable = false)
    private Long inviterId; // 邀请者ID

    @Column(name = "rebate_period", nullable = false)
    private String rebatePeriod; // 返佣周期

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart; // 周期开始时间

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd; // 周期结束时间

    @Column(name = "total_rebate_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalRebateAmount; // 总返佣金额

    @Column(name = "rebate_currency", nullable = false)
    private String rebateCurrency; // 返佣币种

    @Column(name = "record_count", nullable = false)
    private Integer recordCount; // 包含的返佣记录数

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、APPROVED（已审核）、SETTLED（已结算）、REJECTED（已拒绝）

    @Column(name = "audit_user_id")
    private Long auditUserId; // 审核人ID

    @Column(name = "audit_remark")
    private String auditRemark; // 审核备注

    @Column(name = "settled_at")
    private LocalDateTime settledAt; // 结算时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















