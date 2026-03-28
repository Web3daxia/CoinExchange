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
@Table(name = "market_alerts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MarketAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "alert_type", nullable = false)
    private String alertType; // PRICE_CHANGE, VOLUME_CHANGE, LARGE_ORDER, etc.

    @Column(name = "condition_type", nullable = false)
    private String conditionType; // ABOVE, BELOW, PERCENTAGE_CHANGE

    @Column(name = "threshold_value", precision = 20, scale = 8)
    private BigDecimal thresholdValue;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "is_triggered", nullable = false)
    private Boolean isTriggered = false;

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "base_price", precision = 20, scale = 8)
    private BigDecimal basePrice; // 基准价格（用于百分比变化计算）

    @Column(name = "base_volume", precision = 20, scale = 8)
    private BigDecimal baseVolume; // 基准成交量（用于成交量变化计算）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















