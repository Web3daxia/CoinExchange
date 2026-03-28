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
 * 交割合约订单实体
 */
@Entity
@Table(name = "delivery_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "order_type", nullable = false)
    private String orderType; // 订单类型: OPEN（开仓）、CLOSE（平仓）、STOP_LOSS（止损）、TAKE_PROFIT（止盈）

    @Column(name = "side", nullable = false)
    private String side; // 方向: BUY（买入/做多）、SELL（卖出/做空）

    @Column(name = "price_type", nullable = false)
    private String priceType; // 价格类型: LIMIT（限价）、MARKET（市价）

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price; // 限价价格

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 数量

    @Column(name = "filled_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal filledQuantity; // 已成交数量

    @Column(name = "leverage", precision = 10, scale = 2, nullable = false)
    private BigDecimal leverage; // 杠杆倍数

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal margin; // 保证金

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待成交）、PARTIAL（部分成交）、FILLED（已完成）、CANCELLED（已取消）、REJECTED（已拒绝）

    @Column(name = "filled_at")
    private LocalDateTime filledAt; // 成交时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















