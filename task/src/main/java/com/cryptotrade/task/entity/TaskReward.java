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
 * 任务奖励记录实体
 */
@Entity
@Table(name = "task_rewards")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TaskReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_no", unique = true, nullable = false)
    private String rewardNo; // 奖励单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "task_id", nullable = false)
    private Long taskId; // 任务ID

    @Column(name = "progress_id", nullable = false)
    private Long progressId; // 任务进度ID

    @Column(name = "reward_currency", nullable = false)
    private String rewardCurrency; // 奖励币种

    @Column(name = "reward_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rewardAmount; // 奖励金额

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待发放）、CLAIMED（已领取）、DISTRIBUTED（已发放）、FAILED（发放失败）

    @Column(name = "claimed_at")
    private LocalDateTime claimedAt; // 领取时间

    @Column(name = "distributed_at")
    private LocalDateTime distributedAt; // 发放时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















