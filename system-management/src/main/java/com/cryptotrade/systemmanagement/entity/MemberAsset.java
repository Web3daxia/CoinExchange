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
 * 会员资产实体
 */
@Entity
@Table(name = "member_assets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "currency"}))
@Data
@EntityListeners(AuditingEntityListener.class)
public class MemberAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 会员ID（关联users表）

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 币种名称，如: BTC, USDT, ETH

    @Column(name = "available_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO; // 可用余额

    @Column(name = "frozen_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal frozenBalance = BigDecimal.ZERO; // 冻结余额

    @Column(name = "pending_release_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal pendingReleaseBalance = BigDecimal.ZERO; // 待释放资产

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false; // 是否锁定: false否, true是

    @Column(name = "deposit_address", length = 255)
    private String depositAddress; // 充值地址（用户独有）

    @Column(name = "deposit_address_tag", length = 100)
    private String depositAddressTag; // 充值地址标签（如Memo）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














