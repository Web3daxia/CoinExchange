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
@Table(name = "futures_advanced_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FuturesAdvancedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_type", nullable = false)
    private String orderType; // ADVANCED_LIMIT, TRAILING, TRAILING_LIMIT, ICEBERG, SEGMENTED, TIME_WEIGHTED

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "side", nullable = false)
    private String side; // BUY, SELL

    @Column(name = "position_side", nullable = false)
    private String positionSide; // LONG, SHORT

    @Column(name = "margin_mode", nullable = false)
    private String marginMode;

    @Column(name = "leverage", nullable = false)
    private Integer leverage;

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price;

    @Column(name = "quantity", precision = 20, scale = 8)
    private BigDecimal quantity;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, ACTIVE, COMPLETED, CANCELLED

    // 高级限价单参数
    @Column(name = "time_in_force")
    private String timeInForce; // GTC, IOC, FOK

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "price_range_min", precision = 20, scale = 8)
    private BigDecimal priceRangeMin;

    @Column(name = "price_range_max", precision = 20, scale = 8)
    private BigDecimal priceRangeMax;

    // 追踪委托参数
    @Column(name = "trailing_distance", precision = 10, scale = 4)
    private BigDecimal trailingDistance;

    @Column(name = "trailing_price", precision = 20, scale = 8)
    private BigDecimal trailingPrice;

    // 追逐限价单参数
    @Column(name = "trailing_limit_price", precision = 20, scale = 8)
    private BigDecimal trailingLimitPrice;

    // 冰山策略参数
    @Column(name = "iceberg_total_quantity", precision = 20, scale = 8)
    private BigDecimal icebergTotalQuantity;

    @Column(name = "iceberg_display_quantity", precision = 20, scale = 8)
    private BigDecimal icebergDisplayQuantity;

    @Column(name = "iceberg_filled_quantity", precision = 20, scale = 8)
    private BigDecimal icebergFilledQuantity = BigDecimal.ZERO;

    // 分段委托参数
    @Column(name = "segmented_total_quantity", precision = 20, scale = 8)
    private BigDecimal segmentedTotalQuantity;

    @Column(name = "segmented_count")
    private Integer segmentedCount; // 分段数量

    @Column(name = "segmented_filled_count")
    private Integer segmentedFilledCount = 0;

    // 分时委托参数
    @Column(name = "time_interval")
    private Integer timeInterval; // 时间间隔（秒）

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


