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
 * 新闻多语言翻译实体
 */
@Entity
@Table(name = "news_translations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"news_id", "language_code"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "news_id", nullable = false)
    private Long newsId; // 新闻ID

    @Column(name = "language_code", nullable = false, length = 20)
    private String languageCode; // 语言代码，如: zh-CN, en-US

    @Column(name = "title", length = 500)
    private String title; // 翻译后的标题

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary; // 翻译后的摘要

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // 翻译后的内容

    @Column(name = "translation_status", nullable = false, length = 20)
    private String translationStatus = "PENDING"; // 翻译状态: PENDING, TRANSLATING, COMPLETED, FAILED

    @Column(name = "translation_time")
    private LocalDateTime translationTime; // 翻译时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














