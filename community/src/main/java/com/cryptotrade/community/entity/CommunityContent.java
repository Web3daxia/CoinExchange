/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 社区内容实体
 */
@Entity
@Table(name = "community_contents")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CommunityContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 发布者ID

    @Column(name = "content_type", nullable = false)
    private String contentType; // 内容类型: ARTICLE（文章）、IMAGE（图片）、VIDEO（视频）

    @Column(name = "title")
    private String title; // 标题

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content; // 内容（富文本）

    @Column(name = "image_urls")
    private String imageUrls; // 图片URLs（JSON数组）

    @Column(name = "video_url")
    private String videoUrl; // 视频URL

    @Column(name = "category")
    private String category; // 分类

    @Column(name = "tags")
    private String tags; // 标签（逗号分隔）

    @Column(name = "like_count", nullable = false)
    private Long likeCount; // 点赞数

    @Column(name = "comment_count", nullable = false)
    private Long commentCount; // 评论数

    @Column(name = "share_count", nullable = false)
    private Long shareCount; // 分享数

    @Column(name = "reward_count", nullable = false)
    private Long rewardCount; // 赞赏数

    @Column(name = "view_count", nullable = false)
    private Long viewCount; // 浏览量

    @Column(name = "is_top")
    private Boolean isTop; // 是否置顶

    @Column(name = "status", nullable = false)
    private String status; // DRAFT（草稿）、PUBLISHED（已发布）、DELETED（已删除）、HIDDEN（已隐藏）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















