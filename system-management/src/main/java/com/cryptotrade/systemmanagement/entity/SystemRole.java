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
 * 系统角色实体
 */
@Entity
@Table(name = "system_roles")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code", unique = true, nullable = false)
    private String roleCode; // ROOT, ADMIN, OPERATOR, VIEWER等

    @Column(name = "role_name", nullable = false)
    private String roleName; // 角色名称

    @Column(name = "description")
    private String description; // 角色描述

    @Column(name = "level", nullable = false)
    private Integer level; // 角色级别，数字越小权限越高，ROOT=0, ADMIN=1

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














