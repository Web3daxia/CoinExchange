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
 * 社区评论实体
 */
@Entity
@Table(name = "community_comments")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CommunityComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId; // 内容ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "parent_id")
    private Long parentId; // 父评论ID

    @Column(name = "comment_text", columnDefinition = "TEXT", nullable = false)
    private String commentText; // 评论内容

    @Column(name = "like_count", nullable = false)
    private Long likeCount; // 点赞数

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（有效）、DELETED（已删除）、HIDDEN（已隐藏）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















