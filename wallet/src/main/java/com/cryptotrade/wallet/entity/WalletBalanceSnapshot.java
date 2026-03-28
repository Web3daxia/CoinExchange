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
 * 钱包余额快照实体（用于资产汇总图表）
 */
@Entity
@Table(name = "wallet_balance_snapshots")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WalletBalanceSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "snapshot_time", nullable = false)
    private LocalDateTime snapshotTime; // 快照时间

    @Column(name = "total_balance_usd", precision = 20, scale = 8)
    private BigDecimal totalBalanceUsd; // 总资产（USD）

    @Column(name = "spot_balance_usd", precision = 20, scale = 8)
    private BigDecimal spotBalanceUsd; // 现货账户资产（USD）

    @Column(name = "leverage_balance_usd", precision = 20, scale = 8)
    private BigDecimal leverageBalanceUsd; // 杠杆账户资产（USD）

    @Column(name = "futures_usdt_balance_usd", precision = 20, scale = 8)
    private BigDecimal futuresUsdtBalanceUsd; // U本位合约账户资产（USD）

    @Column(name = "futures_coin_balance_usd", precision = 20, scale = 8)
    private BigDecimal futuresCoinBalanceUsd; // 币本位合约账户资产（USD）

    @Column(name = "options_balance_usd", precision = 20, scale = 8)
    private BigDecimal optionsBalanceUsd; // 期权账户资产（USD）

    @Column(name = "copy_trading_balance_usd", precision = 20, scale = 8)
    private BigDecimal copyTradingBalanceUsd; // 跟单账户资产（USD）

    @Column(name = "unrealized_pnl_usd", precision = 20, scale = 8)
    private BigDecimal unrealizedPnlUsd; // 未实现盈亏（USD）

    @Column(name = "realized_pnl_usd", precision = 20, scale = 8)
    private BigDecimal realizedPnlUsd; // 已实现盈亏（USD）

    @Column(name = "snapshot_data", columnDefinition = "TEXT")
    private String snapshotData; // 快照详细数据（JSON格式）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















