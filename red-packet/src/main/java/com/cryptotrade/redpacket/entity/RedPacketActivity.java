/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 红包活动实体
 */
@Entity
@Table(name = "red_packet_activities")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RedPacketActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_name", nullable = false, length = 200)
    private String activityName; // 活动名称

    @Column(name = "activity_code", unique = true, nullable = false, length = 50)
    private String activityCode; // 活动代码（唯一标识）

    @Column(name = "packet_type", nullable = false, length = 50)
    private String packetType; // 红包类型: CASH, COUPON, POINTS, DISCOUNT

    @Column(name = "amount_type", nullable = false, length = 50)
    private String amountType; // 金额类型: FIXED, RANDOM

    @Column(name = "fixed_amount", precision = 20, scale = 8)
    private BigDecimal fixedAmount; // 固定金额

    @Column(name = "min_amount", precision = 20, scale = 8)
    private BigDecimal minAmount; // 最小金额（随机金额）

    @Column(name = "max_amount", precision = 20, scale = 8)
    private BigDecimal maxAmount; // 最大金额（随机金额）

    @Column(name = "total_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalAmount; // 红包总金额

    @Column(name = "total_count", nullable = false)
    private Integer totalCount; // 红包总数量

    @Column(name = "issued_count", nullable = false)
    private Integer issuedCount = 0; // 已发放数量

    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0; // 已使用数量

    @Column(name = "distribution_scope", nullable = false, length = 50)
    private String distributionScope; // 发放范围: SPECIFIC, ALL, VIP

    @Column(name = "receive_condition", length = 100)
    private String receiveCondition; // 领取条件: TRADE, CHECKIN, INVITE, NONE

    @Column(name = "condition_value", precision = 20, scale = 8)
    private BigDecimal conditionValue; // 条件值

    @Column(name = "valid_days", nullable = false)
    private Integer validDays = 7; // 有效期（天数）

    @Column(name = "use_scope", length = 500)
    private String useScope; // 使用范围限制（JSON格式）

    @Column(name = "use_time_limit", length = 500)
    private String useTimeLimit; // 使用时间限制（JSON格式）

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 活动状态: ACTIVE, SUSPENDED, ENDED

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 活动开始时间

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 活动结束时间

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 活动描述

    @Column(name = "auto_issue", nullable = false)
    private Boolean autoIssue = false; // 是否自动发放

    @Column(name = "issue_cycle", length = 50)
    private String issueCycle; // 发放周期: DAILY, WEEKLY, MONTHLY

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














