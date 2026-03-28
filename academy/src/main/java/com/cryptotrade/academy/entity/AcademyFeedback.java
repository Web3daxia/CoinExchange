/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 学院反馈/评分实体
 */
@Entity
@Table(name = "academy_feedbacks")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AcademyFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId; // 内容ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "rating")
    private Integer rating; // 评分（1-5）

    @Column(name = "feedback_text", columnDefinition = "TEXT")
    private String feedbackText; // 反馈内容

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















