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
 * 验证码频率限制配置实体
 */
@Entity
@Table(name = "verification_limit_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VerificationLimitConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "limit_type", nullable = false)
    private String limitType; // 限制类型: IP, DEVICE, PHONE, EMAIL

    @Column(name = "verification_type", nullable = false)
    private String verificationType; // 验证类型: SMS, EMAIL, CAPTCHA

    @Column(name = "time_period", nullable = false)
    private String timePeriod; // 时间周期: MINUTE, HOUR, DAY

    @Column(name = "time_value", nullable = false)
    private Integer timeValue; // 时间值（如：5分钟，1小时，1天）

    @Column(name = "max_verification_count", nullable = false)
    private Integer maxVerificationCount; // 最大验证码次数

    @Column(name = "max_register_count")
    private Integer maxRegisterCount; // 最大注册账号数

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // 是否启用

    @Column(name = "description")
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














