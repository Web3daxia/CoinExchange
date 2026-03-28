/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务实体
 */
@Entity
@Table(name = "tasks")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name", nullable = false)
    private String taskName; // 任务名称

    @Column(name = "task_type", nullable = false)
    private String taskType; // 任务类型: INVITE（邀请）、REGISTER_KYC（注册认证）、DEPOSIT（入金）、TRADE（交易）、COMPREHENSIVE（综合）

    @Column(name = "task_description", columnDefinition = "TEXT")
    private String taskDescription; // 任务描述

    @Column(name = "task_conditions", columnDefinition = "TEXT")
    private String taskConditions; // 任务条件（JSON格式）

    @Column(name = "reward_currency", nullable = false)
    private String rewardCurrency; // 奖励币种

    @Column(name = "reward_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rewardAmount; // 奖励金额

    @Column(name = "max_reward_count")
    private Integer maxRewardCount; // 最大奖励次数，NULL表示无限制

    @Column(name = "daily_limit")
    private Integer dailyLimit; // 每日限制

    @Column(name = "weekly_limit")
    private Integer weeklyLimit; // 每周限制

    @Column(name = "monthly_limit")
    private Integer monthlyLimit; // 每月限制

    @Column(name = "start_time")
    private LocalDateTime startTime; // 开始时间

    @Column(name = "end_time")
    private LocalDateTime endTime; // 结束时间

    @Column(name = "completion_deadline")
    private Integer completionDeadline; // 完成期限（天数）

    @Column(name = "reward_method", nullable = false)
    private String rewardMethod; // 奖励发放方式: INSTANT（即时）、PERIODIC（按周期）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















