/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 杠杆风险警报表
 */
@Entity
@Table(name = "leveraged_risk_alerts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedRiskAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id")
    private Long accountId; // 关联的杠杆账户ID（如果为空，则适用于所有账户）

    @Column(name = "position_id")
    private Long positionId; // 关联的仓位ID（如果为空，则适用于所有仓位）

    @Column(name = "alert_type", nullable = false)
    private String alertType; // MARGIN_LOW（保证金不足）、LIQUIDATION_RISK（强平风险）、LEVERAGE_HIGH（杠杆过高）

    @Column(name = "threshold", precision = 20, scale = 8)
    private BigDecimal threshold; // 阈值

    @Column(name = "threshold_percentage", precision = 10, scale = 4)
    private BigDecimal thresholdPercentage; // 阈值百分比

    @Column(name = "is_triggered")
    private Boolean isTriggered; // 是否已触发

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt; // 触发时间

    @Column(name = "notification_method")
    private String notificationMethod; // SMS（短信）、EMAIL（邮件）、IN_APP（站内信）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、TRIGGERED（已触发）、CANCELLED（已取消）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















