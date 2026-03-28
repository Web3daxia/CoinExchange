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
 * 学院内容实体
 */
@Entity
@Table(name = "academy_contents")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AcademyContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title; // 标题

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary; // 简介

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // 内容（富文本）

    @Column(name = "icon_url")
    private String iconUrl; // 图标URL

    @Column(name = "cover_url")
    private String coverUrl; // 封面图URL

    @Column(name = "video_url")
    private String videoUrl; // 视频URL

    @Column(name = "content_type", nullable = false)
    private String contentType; // 内容类型: TUTORIAL（教程）、ANNOUNCEMENT（公告）、HELP（帮助）

    @Column(name = "category")
    private String category; // 分类

    @Column(name = "tags")
    private String tags; // 标签（逗号分隔）

    @Column(name = "language_code", nullable = false)
    private String languageCode; // 语言代码

    @Column(name = "priority")
    private Integer priority; // 优先级

    @Column(name = "view_count", nullable = false)
    private Long viewCount; // 浏览量

    @Column(name = "like_count", nullable = false)
    private Long likeCount; // 点赞数

    @Column(name = "comment_count", nullable = false)
    private Long commentCount; // 评论数

    @Column(name = "is_top")
    private Boolean isTop; // 是否置顶

    @Column(name = "status", nullable = false)
    private String status; // DRAFT（草稿）、PUBLISHED（已发布）、ARCHIVED（已归档）

    @Column(name = "publish_time")
    private LocalDateTime publishTime; // 发布时间

    @Column(name = "expire_time")
    private LocalDateTime expireTime; // 过期时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















