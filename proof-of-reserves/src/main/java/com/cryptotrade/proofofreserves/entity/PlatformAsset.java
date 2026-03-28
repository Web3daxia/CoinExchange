/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 平台资产实体
 */
@Entity
@Table(name = "platform_assets")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PlatformAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_type", nullable = false)
    private String assetType; // 资产类型: SPOT（现货）、CONTRACT（合约）、COLD_WALLET（冷钱包）、FIAT（法币）

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "balance", precision = 36, scale = 18, nullable = false)
    private BigDecimal balance; // 余额

    @Column(name = "wallet_address")
    private String walletAddress; // 钱包地址

    @Column(name = "account_type")
    private String accountType; // 账户类型

    @Column(name = "sync_time", nullable = false)
    private LocalDateTime syncTime; // 同步时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















