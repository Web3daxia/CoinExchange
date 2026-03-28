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
 * 杠杆订单实体
 */
@Entity
@Table(name = "leveraged_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 关联的杠杆账户ID

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "order_type", nullable = false)
    private String orderType; // MARKET（市价单）、LIMIT（限价单）、STOP_LOSS（止损单）、TAKE_PROFIT（止盈单）、STOP_LIMIT（止损限价单）、CONDITIONAL（条件单）

    @Column(name = "side", nullable = false)
    private String side; // BUY（买入）、SELL（卖出）

    @Column(name = "action", nullable = false)
    private String action; // OPEN（开仓）、CLOSE（平仓）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 数量

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price; // 价格（限价单）

    @Column(name = "stop_price", precision = 20, scale = 8)
    private BigDecimal stopPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "trigger_price", precision = 20, scale = 8)
    private BigDecimal triggerPrice; // 触发价格（条件单）

    @Column(name = "condition_type", length = 50)
    private String conditionType; // 条件类型（条件单）

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity; // 已成交数量

    @Column(name = "filled_amount", precision = 20, scale = 8)
    private BigDecimal filledAmount; // 已成交金额

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待成交）、FILLED（已成交）、PARTIALLY_FILLED（部分成交）、CANCELLED（已取消）、REJECTED（已拒绝）、TRIGGERED（已触发）

    @Column(name = "filled_at")
    private LocalDateTime filledAt; // 成交时间

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt; // 触发时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















