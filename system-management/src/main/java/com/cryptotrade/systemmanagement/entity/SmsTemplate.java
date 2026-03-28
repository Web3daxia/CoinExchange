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
 * 短信模板实体（多语言）
 */
@Entity
@Table(name = "sms_templates")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SmsTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_code", nullable = false)
    private String templateCode; // 模板代码，如: VERIFICATION_CODE, LOGIN_CODE

    @Column(name = "language_code", nullable = false)
    private String languageCode; // 语言代码，如: zh-CN, en-US

    @Column(name = "content", nullable = false, length = 500)
    private String content; // 短信内容（支持变量替换）

    @Column(name = "template_type")
    private String templateType; // 模板类型: VERIFICATION, NOTIFICATION

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














