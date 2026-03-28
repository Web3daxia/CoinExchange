/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Web3钱包实体
 */
@Entity
@Table(name = "web3_wallets")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Web3Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "chain_type", nullable = false)
    private String chainType; // 链类型: ETHEREUM, BSC, POLYGON, ARBITRUM, OPTIMISM

    @Column(name = "wallet_address", nullable = false, unique = true)
    private String walletAddress; // 钱包地址

    @Column(name = "private_key_encrypted", columnDefinition = "TEXT")
    private String privateKeyEncrypted; // 加密的私钥

    @Column(name = "mnemonic_encrypted", columnDefinition = "TEXT")
    private String mnemonicEncrypted; // 加密的助记词

    @Column(name = "wallet_name")
    private String walletName; // 钱包名称

    @Column(name = "is_default")
    private Boolean isDefault; // 是否默认钱包

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（激活）、DISABLED（禁用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















