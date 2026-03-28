/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录实体
 */
@Entity
@Table(name = "withdraw_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WithdrawRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "withdraw_no", unique = true, nullable = false)
    private String withdrawNo; // 提现单号

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "chain", nullable = false)
    private String chain; // 链类型

    @Column(name = "withdraw_address", nullable = false)
    private String withdrawAddress; // 提现地址

    @Column(name = "address_id")
    private Long addressId; // 地址簿ID（如果从地址簿选择）

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 提现金额

    @Column(name = "fee", precision = 20, scale = 8, nullable = false)
    private BigDecimal fee; // 手续费

    @Column(name = "actual_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal actualAmount; // 实际到账金额

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、PROCESSING（处理中）、COMPLETED（已完成）、FAILED（失败）、CANCELLED（已取消）

    @Column(name = "transaction_hash")
    private String transactionHash; // 交易哈希

    @Column(name = "email_verification_code")
    private String emailVerificationCode; // 邮箱验证码

    @Column(name = "phone_verification_code")
    private String phoneVerificationCode; // 手机验证码

    @Column(name = "google_verification_code")
    private String googleVerificationCode; // 谷歌验证码

    @Column(name = "is_verified")
    private Boolean isVerified; // 是否已验证

    @Column(name = "reject_reason", length = 500)
    private String rejectReason; // 拒绝原因

    @Column(name = "processed_by")
    private Long processedBy; // 处理人ID（后台审核）

    @Column(name = "processed_at")
    private LocalDateTime processedAt; // 处理时间

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















