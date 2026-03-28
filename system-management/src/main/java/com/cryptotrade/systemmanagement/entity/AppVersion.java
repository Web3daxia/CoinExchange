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
 * APP版本管理实体
 */
@Entity
@Table(name = "app_versions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"platform", "version"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class AppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "platform", nullable = false, length = 50)
    private String platform; // 平台: ANDROID, IOS, WEB等

    @Column(name = "version", nullable = false, length = 50)
    private String version; // 版本号，如: 1.2.1

    @Column(name = "min_version", length = 50)
    private String minVersion; // 最低可以运行的版本，如: 1.2.0

    @Column(name = "download_url", length = 500)
    private String downloadUrl; // 下载地址

    @Column(name = "release_notes", columnDefinition = "TEXT")
    private String releaseNotes; // 更新说明

    @Column(name = "file_size")
    private Long fileSize; // 文件大小（字节）

    @Column(name = "file_hash", length = 128)
    private String fileHash; // 文件哈希值（MD5或SHA256）

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排列顺序

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE, FORCE_UPDATE

    @Column(name = "is_force_update", nullable = false)
    private Boolean isForceUpdate = false; // 是否强制更新

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














