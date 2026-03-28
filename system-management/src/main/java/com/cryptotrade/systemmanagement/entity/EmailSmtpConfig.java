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
 * 邮件SMTP配置实体
 */
@Entity
@Table(name = "email_smtp_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EmailSmtpConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "smtp_host", nullable = false)
    private String smtpHost; // SMTP服务器地址

    @Column(name = "smtp_port", nullable = false)
    private Integer smtpPort; // SMTP端口

    @Column(name = "smtp_username", nullable = false)
    private String smtpUsername; // SMTP用户名

    @Column(name = "smtp_password", nullable = false)
    private String smtpPassword; // SMTP密码

    @Column(name = "from_email", nullable = false)
    private String fromEmail; // 发件人邮箱

    @Column(name = "from_name")
    private String fromName; // 发件人名称

    @Column(name = "use_ssl", nullable = false)
    private Boolean useSsl = true; // 是否使用SSL

    @Column(name = "use_tls", nullable = false)
    private Boolean useTls = false; // 是否使用TLS

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true; // 是否启用

    @Column(name = "default_subject")
    private String defaultSubject; // 默认邮件标题

    @Column(name = "default_content", columnDefinition = "TEXT")
    private String defaultContent; // 默认邮件内容

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














