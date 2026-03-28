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
 * 商家申请实体
 */
@Entity
@Table(name = "merchant_applications")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MerchantApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
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

    @Column(name = "asset_proof", columnDefinition = "TEXT")
    private String assetProof; // 资产证明图片（JSON数组）

    @Column(name = "total_assets", precision = 20, scale = 8)
    private BigDecimal totalAssets; // 总资产

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）

    @Column(name = "reject_reason", length = 500)
    private String rejectReason; // 拒绝原因

    @Column(name = "reviewed_by")
    private Long reviewedBy; // 审核人ID

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt; // 审核时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















