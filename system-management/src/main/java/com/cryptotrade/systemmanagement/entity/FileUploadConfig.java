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
 * 文件上传配置实体
 */
@Entity
@Table(name = "file_upload_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FileUploadConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upload_type", nullable = false)
    private String uploadType; // 上传类型: FRONTEND, BACKEND

    @Column(name = "file_category", nullable = false)
    private String fileCategory; // 文件分类: AVATAR, CHAT, COMMUNITY, NEWS, DOCUMENT

    @Column(name = "allowed_extensions", nullable = false, length = 500)
    private String allowedExtensions; // 允许的文件扩展名，逗号分隔，如: jpg,jpeg,png,gif

    @Column(name = "max_file_size", nullable = false)
    private Long maxFileSize; // 最大文件大小（字节）

    @Column(name = "max_file_count")
    private Integer maxFileCount; // 最大文件数量

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














