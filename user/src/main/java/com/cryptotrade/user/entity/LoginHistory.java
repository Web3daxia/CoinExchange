/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "login_ip")
    private String loginIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "device_type")
    private String deviceType; // WEB, MOBILE, DESKTOP

    @Column(name = "device_fingerprint")
    private String deviceFingerprint;

    @Column(name = "login_location")
    private String loginLocation;

    @Column(name = "login_status")
    private String loginStatus; // SUCCESS, FAILED

    @Column(name = "failure_reason")
    private String failureReason;

    @CreatedDate
    @Column(name = "login_at")
    private LocalDateTime loginAt;
}















