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
 * 交易策略实体
 */
@Entity
@Table(name = "trading_strategies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TradingStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "strategy_name", nullable = false)
    private String strategyName; // 策略名称

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT, FUTURES_USDT, FUTURES_COIN

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // MA_CROSS, RSI_OVERBOUGHT_OVERSOLD, etc.

    @Column(name = "status", nullable = false)
    private String status; // STOPPED, RUNNING, PAUSED

    @Column(name = "initial_capital", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialCapital; // 初始资金

    @Column(name = "current_capital", precision = 20, scale = 8)
    private BigDecimal currentCapital; // 当前资金

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit = BigDecimal.ZERO; // 总盈利

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss = BigDecimal.ZERO; // 总亏损

    @Column(name = "total_trades")
    private Integer totalTrades = 0; // 总交易次数

    @Column(name = "winning_trades")
    private Integer winningTrades = 0; // 盈利交易次数

    @Column(name = "losing_trades")
    private Integer losingTrades = 0; // 亏损交易次数

    @Column(name = "strategy_params", columnDefinition = "TEXT")
    private String strategyParams; // 策略参数（JSON格式）

    @Column(name = "start_time")
    private LocalDateTime startTime; // 开始时间

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime; // 最后执行时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}













