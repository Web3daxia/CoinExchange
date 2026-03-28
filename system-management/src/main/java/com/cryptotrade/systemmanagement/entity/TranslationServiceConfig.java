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
 * 翻译服务配置实体
 */
@Entity
@Table(name = "translation_service_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TranslationServiceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName; // 服务名称

    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType; // 服务类型: GOOGLE, AZURE, BAIDU, YOUDAO, DEEPL, OTHER

    @Column(name = "service_code", unique = true, nullable = false, length = 50)
    private String serviceCode; // 服务代码（唯一标识）

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false; // 是否启用

    @Column(name = "api_url", length = 500)
    private String apiUrl; // API地址

    @Column(name = "api_key", length = 500)
    private String apiKey; // API密钥

    @Column(name = "api_secret", length = 500)
    private String apiSecret; // API密钥（部分服务需要）

    @Column(name = "app_id", length = 200)
    private String appId; // 应用ID（部分服务需要，如百度）

    @Column(name = "region", length = 100)
    private String region; // 区域（部分服务需要，如Azure）

    @Column(name = "source_language", length = 20)
    private String sourceLanguage = "en-US"; // 源语言（默认英文）

    @Column(name = "target_languages", columnDefinition = "TEXT")
    private String targetLanguages; // 目标语言列表（JSON格式）

    @Column(name = "config_params", columnDefinition = "TEXT")
    private String configParams; // 其他配置参数（JSON格式）

    @Column(name = "priority", nullable = false)
    private Integer priority = 0; // 优先级

    @Column(name = "daily_limit")
    private Integer dailyLimit; // 每日翻译字符数限制

    @Column(name = "current_daily_usage")
    private Long currentDailyUsage = 0L; // 当日已使用字符数

    @Column(name = "last_usage_reset_time")
    private LocalDateTime lastUsageResetTime; // 上次使用量重置时间

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE, ERROR

    @Column(name = "error_message", length = 500)
    private String errorMessage; // 错误信息

    @Column(name = "description", length = 500)
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














