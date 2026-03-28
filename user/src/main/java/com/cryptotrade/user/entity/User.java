/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String countryCode;

    private String password;

    @Column(name = "private_key_hash")
    private String privateKeyHash;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String gender;

    private LocalDateTime birthday;

    @Column(name = "account_status")
    private String accountStatus; // ACTIVE, FROZEN, DISABLED

    @Column(name = "kyc_level")
    private Integer kycLevel; // 0:未认证, 1:基础认证, 2:高级认证

    @Column(name = "kyc_status")
    private String kycStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "two_fa_enabled")
    private Boolean twoFaEnabled = false;

    @Column(name = "two_fa_secret")
    private String twoFaSecret;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















