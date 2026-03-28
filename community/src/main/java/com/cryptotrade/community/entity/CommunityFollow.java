/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 社区关注实体
 */
@Entity
@Table(name = "community_follows")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CommunityFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private Long followerId; // 关注者ID

    @Column(name = "following_id", nullable = false)
    private Long followingId; // 被关注者ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















