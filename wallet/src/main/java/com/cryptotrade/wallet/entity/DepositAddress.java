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
 * 充值地址实体（后台管理）
 */
@Entity
@Table(name = "deposit_addresses")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DepositAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency", nullable = false)
    private String currency; // 币种，如 USDT, BTC, ETH

    @Column(name = "chain", nullable = false)
    private String chain; // 链类型，如 TRC20, ERC20, BEP20, BTC, ETH

    @Column(name = "address", nullable = false, unique = true)
    private String address; // 充值地址

    @Column(name = "qr_code_url", columnDefinition = "TEXT")
    private String qrCodeUrl; // 二维码URL

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、DISABLED（禁用）

    @Column(name = "is_third_party")
    private Boolean isThirdParty; // 是否第三方钱包（如优盾钱包）

    @Column(name = "third_party_config", columnDefinition = "TEXT")
    private String thirdPartyConfig; // 第三方配置（JSON格式）

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















