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
 * 验证码服务配置实体
 */
@Entity
@Table(name = "captcha_service_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CaptchaServiceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_provider", unique = true, nullable = false)
    private String serviceProvider; // 服务提供商: ALIYUN, TENCENT, AWS

    @Column(name = "provider_name", nullable = false)
    private String providerName; // 提供商名称: 阿里云, 腾讯云, AWS

    @Column(name = "captcha_type", nullable = false)
    private String captchaType; // 验证码类型: SLIDER（滑块）, TEXT（验证码）, PICTURE（图片验证码）

    @Column(name = "access_key_id")
    private String accessKeyId; // AccessKey ID

    @Column(name = "access_key_secret")
    private String accessKeySecret; // AccessKey Secret

    @Column(name = "app_id")
    private String appId; // 应用ID（部分服务商需要）

    @Column(name = "app_secret")
    private String appSecret; // 应用Secret（部分服务商需要）

    @Column(name = "endpoint")
    private String endpoint; // 服务端点URL

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false; // 是否启用

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // 是否默认

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














