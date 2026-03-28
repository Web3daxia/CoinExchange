/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合约体验金发放活动实体
 */
@Entity
@Table(name = "contract_experience_activities")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ContractExperienceActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_name", nullable = false, length = 200)
    private String activityName; // 活动名称

    @Column(name = "activity_code", unique = true, nullable = false, length = 50)
    private String activityCode; // 活动代码（唯一标识）

    @Column(name = "experience_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal experienceAmount; // 体验金金额

    @Column(name = "currency", nullable = false, length = 20)
    private String currency = "USDT"; // 币种

    @Column(name = "valid_days", nullable = false)
    private Integer validDays = 7; // 有效期（天数）

    @Column(name = "max_leverage", nullable = false)
    private Integer maxLeverage = 10; // 最大杠杆倍数

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition; // 最大仓位限制

    @Column(name = "daily_trade_limit")
    private Integer dailyTradeLimit; // 每日最大交易次数

    @Column(name = "target_users", nullable = false, length = 50)
    private String targetUsers; // 目标用户: NEW, ALL, VIP, SPECIFIC

    @Column(name = "receive_condition", length = 100)
    private String receiveCondition; // 领取条件: REGISTER, KYC, TRADE, NONE

    @Column(name = "total_count")
    private Integer totalCount; // 发放总数量（NULL表示无限制）

    @Column(name = "issued_count", nullable = false)
    private Integer issuedCount = 0; // 已发放数量

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 活动状态: ACTIVE, SUSPENDED, ENDED

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 活动开始时间

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 活动结束时间

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 活动描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














