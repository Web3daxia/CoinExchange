/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户新闻订阅实体
 */
@Entity
@Table(name = "news_subscriptions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "category_id")
    private Long categoryId; // 订阅的分类ID（NULL表示订阅全部）

    @Column(name = "tag_id")
    private Long tagId; // 订阅的标签ID

    @Column(name = "push_frequency", nullable = false, length = 50)
    private String pushFrequency = "DAILY"; // 推送频率: DAILY, WEEKLY, MONTHLY, REAL_TIME

    @Column(name = "push_enabled", nullable = false)
    private Boolean pushEnabled = true; // 是否启用推送

    @Column(name = "push_method", length = 200)
    private String pushMethod; // 推送方式（JSON格式）

    @Column(name = "notification_priority", length = 50)
    private String notificationPriority = "NORMAL"; // 通知优先级: LOW, NORMAL, HIGH

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














