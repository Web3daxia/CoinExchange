/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 任务进度实体
 */
@Entity
@Table(name = "task_progress")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TaskProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "task_id", nullable = false)
    private Long taskId; // 任务ID

    @Column(name = "progress_data", columnDefinition = "TEXT")
    private String progressData; // 进度数据（JSON格式）

    @Column(name = "completion_rate", precision = 5, scale = 2)
    private java.math.BigDecimal completionRate; // 完成率（0-100）

    @Column(name = "status", nullable = false)
    private String status; // IN_PROGRESS（进行中）、COMPLETED（已完成）、REWARDED（已奖励）、EXPIRED（已过期）

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @Column(name = "deadline")
    private LocalDateTime deadline; // 截止时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















