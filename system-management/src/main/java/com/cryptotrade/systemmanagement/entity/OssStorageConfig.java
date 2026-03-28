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
 * OSS存储配置实体
 */
@Entity
@Table(name = "oss_storage_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OssStorageConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", unique = true, nullable = false)
    private String provider; // 服务提供商: ALIYUN, TENCENT, HUAWEI, AWS

    @Column(name = "provider_name", nullable = false)
    private String providerName; // 提供商名称: 阿里云, 腾讯云, 华为云, AWS

    @Column(name = "access_key_id", nullable = false)
    private String accessKeyId; // AccessKey ID

    @Column(name = "access_key_secret", nullable = false)
    private String accessKeySecret; // AccessKey Secret

    @Column(name = "endpoint", nullable = false)
    private String endpoint; // OSS服务端点

    @Column(name = "bucket_name", nullable = false)
    private String bucketName; // 存储桶名称

    @Column(name = "region")
    private String region; // 区域

    @Column(name = "cdn_domain")
    private String cdnDomain; // CDN域名（可选）

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false; // 是否启用

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // 是否默认（默认使用阿里云）

    @Column(name = "config_params", columnDefinition = "TEXT")
    private String configParams; // 其他配置参数（JSON格式）

    @Column(name = "description")
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














