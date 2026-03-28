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
 * 地区管理实体
 */
@Entity
@Table(name = "regions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", unique = true, nullable = false)
    private String countryCode; // 国家代码，ISO 3166-1 alpha-2，如: CN, US

    @Column(name = "country_name", nullable = false)
    private String countryName; // 国家名称

    @Column(name = "region_code")
    private String regionCode; // 地区代码

    @Column(name = "region_name")
    private String regionName; // 地区名称

    @Column(name = "api_access_enabled", nullable = false)
    private Boolean apiAccessEnabled = true; // API访问是否启用

    @Column(name = "frontend_access_enabled", nullable = false)
    private Boolean frontendAccessEnabled = true; // 前端访问是否启用

    @Column(name = "block_reason")
    private String blockReason; // 限制原因

    @Column(name = "block_message")
    private String blockMessage; // 限制提示信息（多语言，JSON格式）

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // ACTIVE, BLOCKED

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














