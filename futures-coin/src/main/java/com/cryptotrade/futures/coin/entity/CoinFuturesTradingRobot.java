/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 币本位永续合约交易机器人实体
 */
@Entity
@Table(name = "coin_futures_trading_robots")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CoinFuturesTradingRobot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "robot_name")
    private String robotName;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // GRID, TREND_FOLLOWING, REVERSE

    @Column(name = "status", nullable = false)
    private String status; // STOPPED, RUNNING, PAUSED

    // 策略参数（JSON格式存储）
    @Column(name = "strategy_params", columnDefinition = "TEXT")
    private String strategyParams;

    // 风险控制参数
    @Column(name = "max_loss", precision = 20, scale = 8)
    private BigDecimal maxLoss;

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition;

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice;

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice;

    // 交易量参数
    @Column(name = "order_amount", precision = 20, scale = 8)
    private BigDecimal orderAmount;

    @Column(name = "order_quantity", precision = 20, scale = 8)
    private BigDecimal orderQuantity;

    // 杠杆和保证金模式
    @Column(name = "leverage")
    private Integer leverage;

    @Column(name = "margin_mode")
    private String marginMode; // CROSS, ISOLATED

    @Column(name = "position_side")
    private String positionSide; // LONG, SHORT

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss = BigDecimal.ZERO;

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















