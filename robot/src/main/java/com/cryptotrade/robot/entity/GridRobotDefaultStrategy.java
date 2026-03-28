/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网格机器人默认策略实体
 */
@Entity
@Table(name = "grid_robot_default_strategies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class GridRobotDefaultStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_name", nullable = false)
    private String strategyName; // 策略名称

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT, FUTURES_USDT, FUTURES_COIN

    @Column(name = "grid_count")
    private Integer gridCount; // 默认网格数量

    @Column(name = "default_upper_price", precision = 20, scale = 8)
    private BigDecimal defaultUpperPrice; // 默认上边界价格

    @Column(name = "default_lower_price", precision = 20, scale = 8)
    private BigDecimal defaultLowerPrice; // 默认下边界价格

    @Column(name = "default_stop_loss_percentage", precision = 10, scale = 4)
    private BigDecimal defaultStopLossPercentage; // 默认止损百分比

    @Column(name = "default_take_profit_percentage", precision = 10, scale = 4)
    private BigDecimal defaultTakeProfitPercentage; // 默认止盈百分比

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 策略描述

    @Column(name = "expected_return_rate", precision = 10, scale = 4)
    private BigDecimal expectedReturnRate; // 预期收益率

    @Column(name = "risk_level", nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH

    @Column(name = "recommended_pairs", columnDefinition = "TEXT")
    private String recommendedPairs; // 推荐交易对（JSON数组）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}













