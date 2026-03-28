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
 * 质押借币风险监控记录实体
 */
@Entity
@Table(name = "pledge_loan_risk_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanRiskRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 订单ID

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "risk_level", nullable = false, length = 20)
    private String riskLevel; // 风险等级: LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "health_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal healthRate; // 健康度

    @Column(name = "pledge_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal pledgeValue; // 质押价值（USDT）

    @Column(name = "loan_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal loanValue; // 借款价值（USDT）

    @Column(name = "risk_message", nullable = false, length = 500)
    private String riskMessage; // 风险提示信息

    @Column(name = "is_notified", nullable = false)
    private Boolean isNotified = false; // 是否已通知用户

    @Column(name = "notification_time")
    private LocalDateTime notificationTime; // 通知时间

    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed = false; // 是否已处理

    @Column(name = "process_time")
    private LocalDateTime processTime; // 处理时间

    @Column(name = "processor_id")
    private Long processorId; // 处理人ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














