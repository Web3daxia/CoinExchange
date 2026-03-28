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
@Table(name = "futures_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FuturesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "contract_type")
    private String contractType; // USDT_MARGINED, COIN_MARGINED

    @Column(name = "order_type", nullable = false)
    private String orderType; // MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT, STOP_LIMIT, CONDITIONAL

    @Column(name = "side", nullable = false)
    private String side; // BUY, SELL

    @Column(name = "position_side", nullable = false)
    private String positionSide; // LONG, SHORT

    @Column(name = "margin_mode", nullable = false)
    private String marginMode; // CROSS, ISOLATED, SEGMENTED, COMBINED

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price;

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity;

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity = BigDecimal.ZERO;

    @Column(name = "avg_price", precision = 20, scale = 8)
    private BigDecimal avgPrice; // 平均成交价格

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PARTIAL_FILLED, FILLED, CANCELLED, REJECTED

    @Column(name = "stop_price", precision = 20, scale = 8)
    private BigDecimal stopPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "limit_price", precision = 20, scale = 8)
    private BigDecimal limitPrice; // 限价（用于止损限价单）

    @Column(name = "condition_price", precision = 20, scale = 8)
    private BigDecimal conditionPrice; // 条件价格（用于条件单）

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee;

    @Column(name = "margin", precision = 20, scale = 8)
    private BigDecimal margin; // 保证金

    @Column(name = "position_id")
    private Long positionId; // 关联的仓位ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















