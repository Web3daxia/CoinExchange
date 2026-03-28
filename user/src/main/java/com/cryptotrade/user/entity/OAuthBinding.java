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
@Table(name = "oauth_binding")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OAuthBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "provider", nullable = false)
    private String provider; // GOOGLE, FACEBOOK, TWITTER, TELEGRAM

    @Column(name = "oauth_id", nullable = false)
    private String oauthId; // 第三方平台的用户ID

    @Column(name = "oauth_email")
    private String oauthEmail;

    @Column(name = "oauth_username")
    private String oauthUsername;

    @Column(name = "oauth_avatar")
    private String oauthAvatar;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















