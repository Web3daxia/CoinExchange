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
 * U本位永续合约仓位管理实体（持仓管理/合约资产管理）
 */
@Entity
@Table(name = "usdt_futures_position_management")
@Data
@EntityListeners(AuditingEntityListener.class)
public class UsdtFuturesPositionManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_id", unique = true, nullable = false)
    private Long positionId; // 仓位ID（关联futures_positions表）

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "member_uid")
    private String memberUid; // 会员UID

    @Column(name = "member_name")
    private String memberName; // 用户名称

    @Column(name = "email")
    private String email; // 会员邮箱

    @Column(name = "phone")
    private String phone; // 手机号码

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "contract_name")
    private String contractName; // 合约名称

    @Column(name = "pair_name")
    private String pairName; // 合约交易对

    @Column(name = "account_type", nullable = false)
    private String accountType = "FUTURES_USDT"; // 合约账户类型

    @Column(name = "available_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO; // 可用余额

    @Column(name = "frozen_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal frozenBalance = BigDecimal.ZERO; // 冻结余额

    @Column(name = "margin_mode")
    private String marginMode; // 仓位模式: CROSS, ISOLATED, SEGMENTED, COMBINED

    // 多仓相关
    @Column(name = "long_position", precision = 20, scale = 8)
    private BigDecimal longPosition = BigDecimal.ZERO; // 多仓仓位

    @Column(name = "long_frozen", precision = 20, scale = 8)
    private BigDecimal longFrozen = BigDecimal.ZERO; // 冻结多仓

    @Column(name = "long_avg_price", precision = 20, scale = 8)
    private BigDecimal longAvgPrice; // 多仓均买价

    @Column(name = "long_unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal longUnrealizedPnl = BigDecimal.ZERO; // 多仓当前盈亏（金额）

    @Column(name = "long_unrealized_pnl_percent", precision = 10, scale = 4)
    private BigDecimal longUnrealizedPnlPercent = BigDecimal.ZERO; // 多仓当前盈亏（百分比）

    @Column(name = "long_leverage")
    private Integer longLeverage; // 多仓杠杆

    @Column(name = "long_margin", precision = 20, scale = 8)
    private BigDecimal longMargin = BigDecimal.ZERO; // 多仓保证金

    // 空仓相关
    @Column(name = "short_position", precision = 20, scale = 8)
    private BigDecimal shortPosition = BigDecimal.ZERO; // 空仓仓位

    @Column(name = "short_frozen", precision = 20, scale = 8)
    private BigDecimal shortFrozen = BigDecimal.ZERO; // 冻结空仓

    @Column(name = "short_avg_price", precision = 20, scale = 8)
    private BigDecimal shortAvgPrice; // 空仓卖均价

    @Column(name = "short_unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal shortUnrealizedPnl = BigDecimal.ZERO; // 空仓盈亏（金额）

    @Column(name = "short_unrealized_pnl_percent", precision = 10, scale = 4)
    private BigDecimal shortUnrealizedPnlPercent = BigDecimal.ZERO; // 空仓盈亏（百分比）

    @Column(name = "short_leverage")
    private Integer shortLeverage; // 空仓杠杆

    @Column(name = "short_margin", precision = 20, scale = 8)
    private BigDecimal shortMargin = BigDecimal.ZERO; // 空仓保证金

    @Column(name = "status")
    private String status = "ACTIVE"; // 状态: ACTIVE, CLOSED, LIQUIDATED

    @CreatedDate
    @Column(name = "created_at_manage")
    private LocalDateTime createdAtManage;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














