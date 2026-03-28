/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 钱包资产划转记录实体
 */
@Entity
@Table(name = "wallet_transfers")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WalletTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_no", unique = true, nullable = false)
    private String transferNo; // 划转单号

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "from_account_type", nullable = false)
    private String fromAccountType; // 源账户类型

    @Column(name = "to_account_type", nullable = false)
    private String toAccountType; // 目标账户类型

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 划转金额

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（处理中）、COMPLETED（已完成）、FAILED（失败）

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















