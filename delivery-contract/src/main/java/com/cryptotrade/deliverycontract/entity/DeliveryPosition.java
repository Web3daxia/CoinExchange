/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交割合约持仓实体
 */
@Entity
@Table(name = "delivery_positions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "side", nullable = false)
    private String side; // 方向: LONG（多头）、SHORT（空头）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 持仓数量

    @Column(name = "avg_open_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal avgOpenPrice; // 平均开仓价格

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 当前价格

    @Column(name = "leverage", precision = 10, scale = 2, nullable = false)
    private BigDecimal leverage; // 杠杆倍数

    @Column(name = "margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal margin; // 保证金

    @Column(name = "maintenance_margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal maintenanceMargin; // 维持保证金

    @Column(name = "unrealized_pnl", precision = 20, scale = 8, nullable = false)
    private BigDecimal unrealizedPnl; // 未实现盈亏

    @Column(name = "realized_pnl", precision = 20, scale = 8, nullable = false)
    private BigDecimal realizedPnl; // 已实现盈亏

    @Column(name = "liquidation_price", precision = 20, scale = 8)
    private BigDecimal liquidationPrice; // 强平价格

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、CLOSED（已平仓）、LIQUIDATED（已强平）

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt; // 开仓时间

    @Column(name = "closed_at")
    private LocalDateTime closedAt; // 平仓时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















