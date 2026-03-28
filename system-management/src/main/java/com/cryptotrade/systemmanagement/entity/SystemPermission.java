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
 * 系统权限实体
 */
@Entity
@Table(name = "system_permissions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_code", unique = true, nullable = false)
    private String permissionCode; // 权限代码，如: user:view, currency:edit

    @Column(name = "permission_name", nullable = false)
    private String permissionName; // 权限名称

    @Column(name = "module")
    private String module; // 模块名称：USER, CURRENCY, REGION, SYSTEM等

    @Column(name = "action")
    private String action; // 操作：VIEW, CREATE, EDIT, DELETE, MANAGE

    @Column(name = "resource")
    private String resource; // 资源：user, currency, region等

    @Column(name = "description")
    private String description; // 权限描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














