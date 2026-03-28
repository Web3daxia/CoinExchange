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
@Table(name = "advanced_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AdvancedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_type", nullable = false)
    private String orderType; // ADVANCED_LIMIT, TIME_WEIGHTED, RECURRING, TRAILING, ICEBERG

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "side", nullable = false)
    private String side; // BUY, SELL

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price;

    @Column(name = "quantity", precision = 20, scale = 8)
    private BigDecimal quantity;

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity = BigDecimal.ZERO; // 已成交数量（用于分时委托等）

    @Column(name = "status", nullable = false)
    private String status; // PENDING, ACTIVE, COMPLETED, CANCELLED, EXPIRED, FAILED

    // 高级限价单参数
    @Column(name = "time_in_force")
    private String timeInForce; // GTC, IOC, FOK

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    // 分时委托参数
    @Column(name = "time_interval")
    private Integer timeInterval; // 时间间隔（秒）

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // 循环委托参数
    @Column(name = "recurring_period")
    private String recurringPeriod; // DAILY, WEEKLY, MONTHLY

    @Column(name = "recurring_amount", precision = 20, scale = 8)
    private BigDecimal recurringAmount;

    @Column(name = "next_execution_time")
    private LocalDateTime nextExecutionTime;

    // 追踪委托参数
    @Column(name = "trailing_distance", precision = 10, scale = 4)
    private BigDecimal trailingDistance; // 追踪距离（百分比）

    @Column(name = "trailing_price", precision = 20, scale = 8)
    private BigDecimal trailingPrice; // 当前追踪价格

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice;

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice;

    // 冰山策略参数
    @Column(name = "iceberg_total_quantity", precision = 20, scale = 8)
    private BigDecimal icebergTotalQuantity; // 总数量

    @Column(name = "iceberg_display_quantity", precision = 20, scale = 8)
    private BigDecimal icebergDisplayQuantity; // 每次显示数量

    @Column(name = "iceberg_filled_quantity", precision = 20, scale = 8)
    private BigDecimal icebergFilledQuantity = BigDecimal.ZERO; // 已成交数量

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















