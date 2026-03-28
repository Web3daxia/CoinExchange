/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家实体
 */
@Entity
@Table(name = "merchants")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName; // 商家名称

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar; // 头像URL

    @Column(name = "signature", length = 500)
    private String signature; // 签名

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio; // 个人简介

    @Column(name = "country")
    private String country; // 国家

    @Column(name = "region")
    private String region; // 地区

    @Column(name = "level", nullable = false)
    private String level; // CROWN（皇冠商家）、BLUE_V（蓝V商家）、NORMAL（普通商家）

    @Column(name = "is_shield_merchant")
    private Boolean isShieldMerchant; // 是否神盾商家

    @Column(name = "margin_frozen", precision = 20, scale = 8, nullable = false)
    private BigDecimal marginFrozen; // 冻结的保证金

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）、CANCELLED（已取消）

    @Column(name = "is_active")
    private Boolean isActive; // 是否可交易（是否接单）

    @Column(name = "completed_orders")
    private Integer completedOrders; // 完成订单量

    @Column(name = "completion_rate", precision = 10, scale = 4)
    private BigDecimal completionRate; // 完成订单率

    @Column(name = "avg_release_time", precision = 10, scale = 2)
    private BigDecimal avgReleaseTime; // 平均放币时间（分钟）

    @Column(name = "total_rating")
    private BigDecimal totalRating; // 总评分

    @Column(name = "rating_count")
    private Integer ratingCount; // 评分次数

    @Column(name = "avg_rating", precision = 10, scale = 2)
    private BigDecimal avgRating; // 平均评分

    @Column(name = "followers_count")
    private Integer followersCount; // 关注人数

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















