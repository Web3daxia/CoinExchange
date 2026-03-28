/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 验证码频率限制记录实体
 */
@Entity
@Table(name = "verification_limit_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VerificationLimitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "limit_key", nullable = false)
    private String limitKey; // 限制键（IP、设备ID、手机号、邮箱）

    @Column(name = "limit_type", nullable = false)
    private String limitType; // 限制类型: IP, DEVICE, PHONE, EMAIL

    @Column(name = "verification_type", nullable = false)
    private String verificationType; // 验证类型: SMS, EMAIL, CAPTCHA

    @Column(name = "action_type", nullable = false)
    private String actionType; // 操作类型: SEND_VERIFICATION, REGISTER

    @Column(name = "count", nullable = false)
    private Integer count = 1; // 次数

    @Column(name = "period_start_time", nullable = false)
    private LocalDateTime periodStartTime; // 周期开始时间

    @Column(name = "period_end_time", nullable = false)
    private LocalDateTime periodEndTime; // 周期结束时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

