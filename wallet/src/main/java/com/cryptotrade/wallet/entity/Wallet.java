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

@Entity
@Table(name = "wallets")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_type", nullable = false)
    private String accountType; // SPOT, FUTURES_USDT, FUTURES_COIN, OPTIONS, COPY_TRADING

    @Column(name = "currency", nullable = false)
    private String currency; // USDT, BTC, ETH等

    @Column(name = "balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "frozen_balance", precision = 20, scale = 8)
    private BigDecimal frozenBalance = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















