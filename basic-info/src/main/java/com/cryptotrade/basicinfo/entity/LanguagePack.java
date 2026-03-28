/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 语言包实体
 */
@Entity
@Table(name = "language_packs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LanguagePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language_code", nullable = false, unique = true)
    private String languageCode; // 语言代码，如 en, zh-CN, fr, es, de, ja, ko, ar

    @Column(name = "language_name", nullable = false)
    private String languageName; // 语言名称，如 English, 中文, Français

    @Column(name = "native_name")
    private String nativeName; // 本地名称，如 English, 中文, Français

    @Column(name = "country_code")
    private String countryCode; // 国家代码，如 US, CN, FR

    @Column(name = "is_default")
    private Boolean isDefault; // 是否默认语言

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）

    @Column(name = "translation_data", columnDefinition = "LONGTEXT")
    private String translationData; // 翻译数据（JSON格式）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















