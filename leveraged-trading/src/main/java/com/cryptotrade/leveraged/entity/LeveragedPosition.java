/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 杠杆仓位实体
 */
@Entity
@Table(name = "leveraged_positions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 关联的杠杆账户ID

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "side", nullable = false)
    private String side; // LONG（做多）、SHORT（做空）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 持仓数量

    @Column(name = "entry_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal entryPrice; // 开仓价格

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 当前价格

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数

    @Column(name = "margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal margin; // 保证金

    @Column(name = "initial_margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialMargin; // 初始保证金

    @Column(name = "maintenance_margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal maintenanceMargin; // 维持保证金

    @Column(name = "borrowed_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal borrowedAmount; // 借入资金

    @Column(name = "unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal unrealizedPnl; // 未实现盈亏

    @Column(name = "realized_pnl", precision = 20, scale = 8)
    private BigDecimal realizedPnl; // 已实现盈亏

    @Column(name = "liquidation_price", precision = 20, scale = 8)
    private BigDecimal liquidationPrice; // 强平价格

    @Column(name = "margin_ratio", precision = 10, scale = 4)
    private BigDecimal marginRatio; // 保证金率

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、CLOSED（已平仓）、LIQUIDATED（已强平）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















