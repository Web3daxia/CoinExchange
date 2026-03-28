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
 * 系统消息实体（用于多语言）
 */
@Entity
@Table(name = "system_messages")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_key", nullable = false)
    private String messageKey; // 消息键，如: error.currency.not_found

    @Column(name = "language_code", nullable = false)
    private String languageCode; // 语言代码，如: zh-CN, en-US

    @Column(name = "message_value", columnDefinition = "TEXT", nullable = false)
    private String messageValue; // 消息值（翻译内容）

    @Column(name = "module")
    private String module; // 模块名称

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

