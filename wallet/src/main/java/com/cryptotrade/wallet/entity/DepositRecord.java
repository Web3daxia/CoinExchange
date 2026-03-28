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
 * 充值记录实体
 */
@Entity
@Table(name = "deposit_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DepositRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deposit_no", unique = true, nullable = false)
    private String depositNo; // 充值单号

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "chain", nullable = false)
    private String chain; // 链类型

    @Column(name = "deposit_address", nullable = false)
    private String depositAddress; // 充值地址

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 充值金额

    @Column(name = "confirmations")
    private Integer confirmations; // 确认数

    @Column(name = "required_confirmations")
    private Integer requiredConfirmations; // 所需确认数

    @Column(name = "transaction_hash")
    private String transactionHash; // 交易哈希

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待确认）、CONFIRMING（确认中）、COMPLETED（已完成）、FAILED（失败）

    @Column(name = "is_third_party")
    private Boolean isThirdParty; // 是否第三方充值

    @Column(name = "third_party_order_id")
    private String thirdPartyOrderId; // 第三方订单ID

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















