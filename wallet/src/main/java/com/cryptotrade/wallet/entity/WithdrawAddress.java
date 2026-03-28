/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 提现地址簿实体（用户常用地址）
 */
@Entity
@Table(name = "withdraw_addresses")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WithdrawAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "chain", nullable = false)
    private String chain; // 链类型

    @Column(name = "address", nullable = false)
    private String address; // 提现地址

    @Column(name = "address_label")
    private String addressLabel; // 地址标签（用户自定义名称）

    @Column(name = "is_verified")
    private Boolean isVerified; // 是否已验证

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、DISABLED（禁用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















