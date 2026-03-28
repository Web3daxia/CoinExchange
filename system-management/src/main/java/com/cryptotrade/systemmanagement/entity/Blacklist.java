/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 黑名单实体
 */
@Entity
@Table(name = "blacklists")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blacklist_type", nullable = false)
    private String blacklistType; // 黑名单类型: IP, USER, DEVICE

    @Column(name = "blacklist_value", nullable = false)
    private String blacklistValue; // 黑名单值（IP地址、用户ID、设备ID）

    @Column(name = "reason")
    private String reason; // 加入黑名单原因

    @Column(name = "blocked_by")
    private Long blockedBy; // 操作人ID（管理员ID）

    @Column(name = "expire_time")
    private LocalDateTime expireTime; // 过期时间（NULL表示永久）

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE, EXPIRED

    @Column(name = "description")
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














