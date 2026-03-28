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
 * 系统管理员实体
 */
@Entity
@Table(name = "system_admins")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId; // 关联角色表

    @Column(name = "avatar", length = 500)
    private String avatar; // 头像URL

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username; // 管理员用户名

    @Column(name = "password", nullable = false, length = 255)
    private String password; // 密码（加密存储）

    @Column(name = "phone", length = 20)
    private String phone; // 手机号码

    @Column(name = "email", length = 100)
    private String email; // 邮箱号码

    @Column(name = "security_code", length = 50)
    private String securityCode; // 安全码（用于二次验证）

    @Column(name = "enable_google_auth", nullable = false)
    private Boolean enableGoogleAuth = false; // 是否启用谷歌验证码

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, SUSPENDED

    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp; // 最后登录IP

    @Column(name = "last_login_device", length = 200)
    private String lastLoginDevice; // 最后登录设备

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 添加时间

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime; // 最后登录时间

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间
}



