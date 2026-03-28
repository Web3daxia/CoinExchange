/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 模拟交易活动实体（后台管理）
 */
@Entity
@Table(name = "simulated_trading_activities")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SimulatedTradingActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_name", nullable = false, length = 200)
    private String activityName; // 活动名称

    @Column(name = "activity_code", unique = true, nullable = false, length = 50)
    private String activityCode; // 活动代码（唯一标识）

    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType; // 活动类型: COMPETITION, REWARD

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 活动开始时间

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 活动结束时间

    @Column(name = "reward_rules", columnDefinition = "TEXT")
    private String rewardRules; // 奖励规则（JSON格式）

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 活动状态: ACTIVE, SUSPENDED, ENDED

    @Column(name = "participant_count", nullable = false)
    private Integer participantCount = 0; // 参与人数

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 活动描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














