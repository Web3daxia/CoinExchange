/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 社区赞赏实体
 */
@Entity
@Table(name = "community_rewards")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CommunityReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id", nullable = false)
    private Long contentId; // 内容ID

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId; // 赞赏者ID

    @Column(name = "to_user_id", nullable = false)
    private Long toUserId; // 被赞赏者ID

    @Column(name = "reward_currency", nullable = false)
    private String rewardCurrency; // 奖励币种

    @Column(name = "reward_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rewardAmount; // 奖励金额

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















