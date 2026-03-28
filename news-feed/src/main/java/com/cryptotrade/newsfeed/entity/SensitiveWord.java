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
 * 敏感词实体
 */
@Entity
@Table(name = "sensitive_words")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SensitiveWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", unique = true, nullable = false, length = 200)
    private String word; // 敏感词

    @Column(name = "word_type", length = 50)
    private String wordType; // 敏感词类型: POLITICAL, VIOLENCE, FAKE, OTHER

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














