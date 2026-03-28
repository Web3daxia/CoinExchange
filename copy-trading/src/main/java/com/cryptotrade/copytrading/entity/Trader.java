/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 带单员实体
 */
@Entity
@Table(name = "traders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Trader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "trader_type", nullable = false)
    private String traderType; // SPOT（现货交易员）、FUTURES（合约交易员）、BOTH（两者都是）

    @Column(name = "level", nullable = false)
    private String level; // BEGINNER（初级）、INTERMEDIATE（中级）、ADVANCED（高级）、TOP（顶级）

    @Column(name = "margin_frozen", precision = 20, scale = 8, nullable = false)
    private BigDecimal marginFrozen; // 冻结的保证金

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）、CANCELLED（已取消）

    @Column(name = "public_enabled")
    private Boolean publicEnabled; // 是否开启公域跟单

    @Column(name = "private_enabled")
    private Boolean privateEnabled; // 是否开启私域跟单

    @Column(name = "invite_code", unique = true)
    private String inviteCode; // 邀请码（私域跟单）

    @Column(name = "subscription_fee", precision = 20, scale = 8)
    private BigDecimal subscriptionFee; // 订阅费（私域跟单）

    @Column(name = "commission_rate", precision = 10, scale = 4)
    private BigDecimal commissionRate; // 佣金比例

    @Column(name = "profit_share_rate", precision = 10, scale = 4)
    private BigDecimal profitShareRate; // 分润比例

    @Column(name = "total_followers")
    private Integer totalFollowers; // 总跟单人数

    @Column(name = "total_aum", precision = 20, scale = 8)
    private BigDecimal totalAum; // 总资产管理规模（AUM）

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit; // 总盈利

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss; // 总亏损

    @Column(name = "win_rate", precision = 10, scale = 4)
    private BigDecimal winRate; // 胜率

    @Column(name = "sharpe_ratio", precision = 10, scale = 4)
    private BigDecimal sharpeRatio; // 夏普比率

    @Column(name = "max_drawdown", precision = 10, scale = 4)
    private BigDecimal maxDrawdown; // 最大回撤

    @Column(name = "last_liquidation_time")
    private LocalDateTime lastLiquidationTime; // 最近强制平仓时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















