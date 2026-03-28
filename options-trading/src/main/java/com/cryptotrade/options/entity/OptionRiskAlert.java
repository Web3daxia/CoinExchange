/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 期权风险警报实体
 */
@Entity
@Table(name = "option_risk_alerts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionRiskAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "position_id")
    private Long positionId; // 关联的期权持仓ID（如果为空，则适用于所有持仓）

    @Column(name = "alert_type", nullable = false)
    private String alertType; // PROFIT（盈利警报）、LOSS（亏损警报）、EXPIRY（到期警报）

    @Column(name = "threshold", precision = 20, scale = 8, nullable = false)
    private BigDecimal threshold; // 阈值（盈利或亏损金额）

    @Column(name = "threshold_percentage", precision = 10, scale = 4)
    private BigDecimal thresholdPercentage; // 阈值百分比

    @Column(name = "is_triggered")
    private Boolean isTriggered; // 是否已触发

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt; // 触发时间

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、TRIGGERED（已触发）、CANCELLED（已取消）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















