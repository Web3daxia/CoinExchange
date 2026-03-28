/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 邀请奖励实体（一次性奖励）
 */
@Entity
@Table(name = "invite_rewards")
@Data
@EntityListeners(AuditingEntityListener.class)
public class InviteReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inviter_id", nullable = false)
    private Long inviterId; // 邀请者ID

    @Column(name = "invitee_id", nullable = false)
    private Long inviteeId; // 被邀请者ID

    @Column(name = "relation_id", nullable = false)
    private Long relationId; // 邀请关系ID

    @Column(name = "reward_type", nullable = false)
    private String rewardType; // REGISTER（注册奖励）、KYC（KYC奖励）、FIRST_TRADE（首笔交易奖励）

    @Column(name = "reward_currency", nullable = false)
    private String rewardCurrency; // 奖励币种，如 USDT

    @Column(name = "reward_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rewardAmount; // 奖励金额

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待发放）、COMPLETED（已发放）、FAILED（发放失败）

    @Column(name = "distributed_at")
    private LocalDateTime distributedAt; // 发放时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















