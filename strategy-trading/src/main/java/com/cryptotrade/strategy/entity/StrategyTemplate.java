/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略模板实体
 */
@Entity
@Table(name = "strategy_templates")
@Data
@EntityListeners(AuditingEntityListener.class)
public class StrategyTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false)
    private String templateName; // 模板名称

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT, FUTURES_USDT, FUTURES_COIN

    @Column(name = "strategy_category", nullable = false)
    private String strategyCategory; // TREND, REVERSAL, BREAKOUT, OSCILLATION, GRID, RISK

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // MA_CROSS, RSI_OVERBOUGHT_OVERSOLD, etc.

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 描述

    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription; // 详细描述

    @Column(name = "default_params", columnDefinition = "TEXT")
    private String defaultParams; // 默认参数（JSON格式）

    @Column(name = "param_description", columnDefinition = "TEXT")
    private String paramDescription; // 参数描述（JSON格式）

    @Column(name = "risk_level", nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH

    @Column(name = "expected_return_rate", precision = 10, scale = 4)
    private BigDecimal expectedReturnRate; // 预期收益率

    @Column(name = "max_drawdown", precision = 10, scale = 4)
    private BigDecimal maxDrawdown; // 最大回撤

    @Column(name = "backtest_performance", columnDefinition = "TEXT")
    private String backtestPerformance; // 回测表现（JSON格式）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}













