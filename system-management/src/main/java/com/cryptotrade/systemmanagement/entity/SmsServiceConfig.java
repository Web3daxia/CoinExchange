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
 * 短信服务配置实体
 */
@Entity
@Table(name = "sms_service_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SmsServiceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_provider", unique = true, nullable = false)
    private String serviceProvider; // 服务提供商: ALIYUN, TENCENT, SMSBAO, MODU

    @Column(name = "provider_name", nullable = false)
    private String providerName; // 提供商名称: 阿里云, 腾讯云, 短信宝, 摩杜云

    @Column(name = "access_key_id")
    private String accessKeyId; // AccessKey ID（阿里云、腾讯云使用）

    @Column(name = "access_key_secret")
    private String accessKeySecret; // AccessKey Secret（阿里云、腾讯云使用）

    @Column(name = "username")
    private String username; // 用户名（短信宝、摩杜云使用）

    @Column(name = "password")
    private String password; // 密码（短信宝、摩杜云使用）

    @Column(name = "api_key")
    private String apiKey; // API Key（部分服务商使用）

    @Column(name = "endpoint")
    private String endpoint; // 服务端点URL

    @Column(name = "sign_name")
    private String signName; // 短信签名

    @Column(name = "template_code")
    private String templateCode; // 模板代码

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false; // 是否启用

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // 是否默认（默认使用短信宝）

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














