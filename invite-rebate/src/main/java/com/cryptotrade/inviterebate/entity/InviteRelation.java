/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 邀请关系实体
 */
@Entity
@Table(name = "invite_relations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class InviteRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inviter_id", nullable = false)
    private Long inviterId; // 邀请者ID

    @Column(name = "invitee_id", nullable = false, unique = true)
    private Long inviteeId; // 被邀请者ID

    @Column(name = "invite_code", nullable = false)
    private String inviteCode; // 邀请码

    @Column(name = "invite_link")
    private String inviteLink; // 邀请链接

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（有效）、INACTIVE（无效）

    @Column(name = "is_registered")
    private Boolean isRegistered; // 是否已注册

    @Column(name = "is_kyc_verified")
    private Boolean isKycVerified; // 是否已完成KYC

    @Column(name = "has_first_trade")
    private Boolean hasFirstTrade; // 是否已完成首笔交易

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















