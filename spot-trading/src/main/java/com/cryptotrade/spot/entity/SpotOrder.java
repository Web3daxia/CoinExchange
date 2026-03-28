/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spot_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SpotOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "order_type", nullable = false)
    private String orderType; // MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT

    @Column(name = "side", nullable = false)
    private String side; // BUY, SELL

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price;

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity;

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity = BigDecimal.ZERO;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, PARTIAL_FILLED, FILLED, CANCELLED

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee;

    @Column(name = "stop_price", precision = 20, scale = 8)
    private BigDecimal stopPrice; // 止损价格

    @Column(name = "avg_price", precision = 20, scale = 8)
    private BigDecimal avgPrice; // 平均成交价格

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















