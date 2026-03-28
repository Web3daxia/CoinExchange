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
 * 新闻实体
 */
@Entity
@Table(name = "news", indexes = {
    @Index(name = "idx_source_article", columnList = "source_id,source_article_id", unique = true)
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    private Long sourceId; // 新闻源ID

    @Column(name = "source_article_id", length = 200)
    private String sourceArticleId; // 新闻源的文章ID（用于去重）

    @Column(name = "title", nullable = false, length = 500)
    private String title; // 新闻标题

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary; // 新闻摘要

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // 新闻内容（完整内容）

    @Column(name = "author", length = 200)
    private String author; // 作者

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl; // 封面图片URL

    @Column(name = "original_url", length = 500)
    private String originalUrl; // 原文链接

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime; // 发布时间

    @Column(name = "category_id")
    private Long categoryId; // 新闻分类ID

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING"; // 状态: PENDING, APPROVED, REJECTED, PUBLISHED

    @Column(name = "is_hot", nullable = false)
    private Boolean isHot = false; // 是否热门

    @Column(name = "is_recommended", nullable = false)
    private Boolean isRecommended = false; // 是否推荐

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0; // 阅读量

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 点赞数

    @Column(name = "share_count", nullable = false)
    private Integer shareCount = 0; // 分享数

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0; // 评论数

    @Column(name = "reviewer_id")
    private Long reviewerId; // 审核人ID

    @Column(name = "review_time")
    private LocalDateTime reviewTime; // 审核时间

    @Column(name = "review_remark", length = 500)
    private String reviewRemark; // 审核备注

    @Column(name = "sensitive_words", columnDefinition = "TEXT")
    private String sensitiveWords; // 检测到的敏感词（JSON格式）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














