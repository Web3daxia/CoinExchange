/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 跟单订单实体
 * 记录带单员的原始订单和跟单员的复制订单
 */
@Entity
@Table(name = "copy_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CopyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trader_id", nullable = false)
    private Long traderId; // 带单员ID

    @Column(name = "trader_order_id", nullable = false)
    private Long traderOrderId; // 带单员的原始订单ID

    @Column(name = "follower_id", nullable = false)
    private Long followerId; // 跟单员ID

    @Column(name = "relation_id", nullable = false)
    private Long relationId; // 跟单关系ID

    @Column(name = "follower_order_id")
    private Long followerOrderId; // 跟单员的复制订单ID

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT、FUTURES_USDT、FUTURES_COIN

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "side", nullable = false)
    private String side; // BUY、SELL

    @Column(name = "action", nullable = false)
    private String action; // OPEN、CLOSE

    @Column(name = "trader_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal traderQuantity; // 带单员的订单数量

    @Column(name = "follower_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal followerQuantity; // 跟单员的订单数量

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 成交价格

    @Column(name = "copy_ratio", precision = 10, scale = 4, nullable = false)
    private BigDecimal copyRatio; // 跟单比例

    @Column(name = "commission", precision = 20, scale = 8)
    private BigDecimal commission; // 佣金

    @Column(name = "profit_loss", precision = 20, scale = 8)
    private BigDecimal profitLoss; // 盈亏

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待执行）、EXECUTED（已执行）、FAILED（执行失败）

    @Column(name = "executed_at")
    private LocalDateTime executedAt; // 执行时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















