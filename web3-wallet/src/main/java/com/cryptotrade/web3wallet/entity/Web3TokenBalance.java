/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Web3代币余额实体
 */
@Entity
@Table(name = "web3_token_balances")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Web3TokenBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId; // 钱包ID

    @Column(name = "token_contract_address")
    private String tokenContractAddress; // 代币合约地址（NULL表示原生币）

    @Column(name = "token_symbol", nullable = false)
    private String tokenSymbol; // 代币符号

    @Column(name = "token_name")
    private String tokenName; // 代币名称

    @Column(name = "token_decimals")
    private Integer tokenDecimals; // 代币精度

    @Column(name = "balance", precision = 36, scale = 18, nullable = false)
    private BigDecimal balance; // 余额

    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime; // 最后同步时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















