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
 * 用户新闻收藏实体
 */
@Entity
@Table(name = "news_favorites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "news_id"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class NewsFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "news_id", nullable = false)
    private Long newsId; // 新闻ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














