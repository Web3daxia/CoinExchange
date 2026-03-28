/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "futures_positions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FuturesPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "contract_type")
    private String contractType; // USDT_MARGINED, COIN_MARGINED

    @Column(name = "position_side", nullable = false)
    private String positionSide; // LONG, SHORT

    @Column(name = "margin_mode", nullable = false)
    private String marginMode; // CROSS, ISOLATED, SEGMENTED, COMBINED

    @Column(name = "leverage", nullable = false)
    private Integer leverage;

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 持仓数量

    @Column(name = "entry_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal entryPrice; // 开仓价格

    @Column(name = "mark_price", precision = 20, scale = 8)
    private BigDecimal markPrice; // 标记价格

    @Column(name = "liquidation_price", precision = 20, scale = 8)
    private BigDecimal liquidationPrice; // 强平价格

    @Column(name = "margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal margin; // 保证金

    @Column(name = "unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal unrealizedPnl = BigDecimal.ZERO; // 未实现盈亏

    @Column(name = "realized_pnl", precision = 20, scale = 8)
    private BigDecimal realizedPnl = BigDecimal.ZERO; // 已实现盈亏

    @Column(name = "funding_fee", precision = 20, scale = 8)
    private BigDecimal fundingFee = BigDecimal.ZERO; // 资金费用

    @Column(name = "status", nullable = false)
    private String status; // OPEN, CLOSED, LIQUIDATED

    @Column(name = "margin_ratio", precision = 10, scale = 4)
    private BigDecimal marginRatio; // 保证金率

    @Column(name = "maintenance_margin", precision = 20, scale = 8)
    private BigDecimal maintenanceMargin; // 维持保证金

    @Column(name = "isolated_margin")
    private BigDecimal isolatedMargin; // 逐仓保证金（逐仓模式使用）

    @Column(name = "segment_id")
    private Long segmentId; // 分仓ID（分仓模式使用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















