/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员管理实体
 */
@Entity
@Table(name = "member_management")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MemberManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId; // 会员ID（关联users表）

    @Column(name = "member_name")
    private String memberName; // 会员名称

    @Column(name = "member_uid", unique = true)
    private String memberUid; // 会员UID（唯一标识）

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl; // 头像URL

    @Column(name = "country")
    private String country; // 国家

    @Column(name = "email")
    private String email; // 会员邮箱

    @Column(name = "phone")
    private String phone; // 手机号码

    @Column(name = "agent_id")
    private Long agentId; // 代理商ID（关联agents表）

    @Column(name = "registration_time")
    private LocalDateTime registrationTime; // 注册时间

    @Column(name = "registration_method")
    private String registrationMethod; // 注册方式: EMAIL, PHONE, OAUTH_GOOGLE, OAUTH_FACEBOOK等

    @Column(name = "trading_status", nullable = false)
    private String tradingStatus = "NORMAL"; // 交易状态: NORMAL（正常）, FROZEN（冻结）

    @Column(name = "registration_device")
    private String registrationDevice; // 注册设备

    @Column(name = "user_status", nullable = false)
    private String userStatus = "NORMAL"; // 用户状态: NORMAL（正常）, DISABLED（禁用）

    @Column(name = "withdraw_status", nullable = false)
    private String withdrawStatus = "ALLOWED"; // 提现状态: ALLOWED（允许）, REJECTED（拒绝）

    @Column(name = "oauth_bound", nullable = false)
    private Boolean oauthBound = false; // 是否绑定第三方登录

    @Column(name = "google_auth_enabled", nullable = false)
    private Boolean googleAuthEnabled = false; // 是否启用谷歌验证码

    @Column(name = "withdraw_auto_approve", nullable = false)
    private String withdrawAutoApprove = "REJECTED"; // 提现自动审核: ALLOWED（允许）, REJECTED（拒绝）

    @Column(name = "withdraw_min_amount", precision = 20, scale = 8)
    private BigDecimal withdrawMinAmount; // 提现保底金额（最低提现限制）

    @Column(name = "inviter_id")
    private Long inviterId; // 邀请者ID（关联users表）

    @Column(name = "registration_ip")
    private String registrationIp; // 注册IP

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime; // 最近登录时间

    @Column(name = "last_login_ip")
    private String lastLoginIp; // 最近登录IP

    @Column(name = "agent_organization")
    private String agentOrganization; // 所属的代理商的机构

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














