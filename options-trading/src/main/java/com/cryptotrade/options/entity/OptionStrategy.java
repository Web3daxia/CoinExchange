/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 期权策略实体
 */
@Entity
@Table(name = "option_strategies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "strategy_name")
    private String strategyName; // 策略名称

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // STRADDLE（跨式）、BUTTERFLY（蝶式）、VERTICAL（价差）、CALENDAR（日历价差）

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "strategy_params", columnDefinition = "TEXT")
    private String strategyParams; // 策略参数（JSON格式）

    @Column(name = "total_cost", precision = 20, scale = 8)
    private BigDecimal totalCost; // 总成本

    @Column(name = "current_value", precision = 20, scale = 8)
    private BigDecimal currentValue; // 当前价值

    @Column(name = "unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal unrealizedPnl; // 未实现盈亏

    @Column(name = "realized_pnl", precision = 20, scale = 8)
    private BigDecimal realizedPnl; // 已实现盈亏

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、CLOSED（已关闭）、EXPIRED（已到期）

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate; // 策略到期日期

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















