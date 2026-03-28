/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户新闻互动实体
 */
@Entity
@Table(name = "news_interactions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "news_id", nullable = false)
    private Long newsId; // 新闻ID

    @Column(name = "interaction_type", nullable = false, length = 50)
    private String interactionType; // 互动类型: LIKE, SHARE, VIEW

    @Column(name = "share_platform", length = 50)
    private String sharePlatform; // 分享平台: WECHAT, TELEGRAM, TWITTER, FACEBOOK

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














