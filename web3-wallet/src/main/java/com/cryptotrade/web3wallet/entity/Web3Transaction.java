/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Web3交易记录实体
 */
@Entity
@Table(name = "web3_transactions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Web3Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId; // 钱包ID

    @Column(name = "tx_hash", nullable = false, unique = true)
    private String txHash; // 交易哈希

    @Column(name = "from_address", nullable = false)
    private String fromAddress; // 发送地址

    @Column(name = "to_address", nullable = false)
    private String toAddress; // 接收地址

    @Column(name = "token_contract_address")
    private String tokenContractAddress; // 代币合约地址（NULL表示原生币）

    @Column(name = "amount", precision = 36, scale = 18, nullable = false)
    private BigDecimal amount; // 金额

    @Column(name = "gas_price", precision = 36, scale = 18)
    private BigDecimal gasPrice; // Gas价格

    @Column(name = "gas_used")
    private Long gasUsed; // Gas使用量

    @Column(name = "tx_fee", precision = 36, scale = 18)
    private BigDecimal txFee; // 交易手续费

    @Column(name = "block_number")
    private Long blockNumber; // 区块号

    @Column(name = "block_hash")
    private String blockHash; // 区块哈希

    @Column(name = "tx_status", nullable = false)
    private String txStatus; // PENDING（待确认）、SUCCESS（成功）、FAILED（失败）

    @Column(name = "tx_type", nullable = false)
    private String txType; // SEND（发送）、RECEIVE（接收）、CONTRACT（合约调用）

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt; // 确认时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















