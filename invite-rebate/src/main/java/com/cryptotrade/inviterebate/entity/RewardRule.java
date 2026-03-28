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
 * 奖励规则实体（一次性奖励配置）
 */
@Entity
@Table(name = "reward_rules")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RewardRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_type", nullable = false)
    private String rewardType; // REGISTER（注册奖励）、KYC（KYC奖励）、FIRST_TRADE（首笔交易奖励）

    @Column(name = "reward_name", nullable = false)
    private String rewardName; // 奖励名称

    @Column(name = "reward_currency", nullable = false)
    private String rewardCurrency; // 奖励币种

    @Column(name = "reward_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rewardAmount; // 奖励金额

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）

    @Column(name = "max_reward_count")
    private Integer maxRewardCount; // 最大奖励次数，NULL表示无限制

    @Column(name = "remark")
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















