/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 通知实体（站内信）
 */
@Entity
@Table(name = "notifications")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false)
    private String type; // 通知类型，如 SYSTEM（系统通知）、TRADE（交易提醒）、MARKET（市场波动）、ACTIVITY（平台活动）

    @Column(name = "title", nullable = false)
    private String title; // 标题

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 内容

    @Column(name = "is_read", nullable = false)
    private Boolean isRead; // 是否已读

    @Column(name = "is_email_sent")
    private Boolean isEmailSent; // 是否已发送邮件

    @Column(name = "is_push_sent")
    private Boolean isPushSent; // 是否已推送

    @Column(name = "link_url")
    private String linkUrl; // 跳转链接

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















