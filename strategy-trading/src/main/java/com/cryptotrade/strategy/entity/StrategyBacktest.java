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
 * 策略回测实体
 */
@Entity
@Table(name = "strategy_backtests")
@Data
@EntityListeners(AuditingEntityListener.class)
public class StrategyBacktest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backtest_id", unique = true, nullable = false)
    private String backtestId; // 回测任务ID

    @Column(name = "strategy_id", nullable = false)
    private Long strategyId; // 策略ID

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT, FUTURES_USDT, FUTURES_COIN

    @Column(name = "status", nullable = false)
    private String status; // PENDING, RUNNING, COMPLETED, FAILED

    @Column(name = "progress", precision = 5, scale = 2)
    private BigDecimal progress; // 进度百分比

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 回测开始时间

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 回测结束时间

    @Column(name = "initial_capital", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialCapital;

    @Column(name = "final_capital", precision = 20, scale = 8)
    private BigDecimal finalCapital;

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit;

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss;

    @Column(name = "net_profit", precision = 20, scale = 8)
    private BigDecimal netProfit;

    @Column(name = "profit_rate", precision = 10, scale = 4)
    private BigDecimal profitRate;

    @Column(name = "annualized_return_rate", precision = 10, scale = 4)
    private BigDecimal annualizedReturnRate;

    @Column(name = "total_trades")
    private Integer totalTrades;

    @Column(name = "winning_trades")
    private Integer winningTrades;

    @Column(name = "losing_trades")
    private Integer losingTrades;

    @Column(name = "win_rate", precision = 10, scale = 4)
    private BigDecimal winRate;

    @Column(name = "average_profit", precision = 20, scale = 8)
    private BigDecimal averageProfit;

    @Column(name = "average_loss", precision = 20, scale = 8)
    private BigDecimal averageLoss;

    @Column(name = "profit_loss_ratio", precision = 10, scale = 4)
    private BigDecimal profitLossRatio;

    @Column(name = "max_drawdown", precision = 20, scale = 8)
    private BigDecimal maxDrawdown;

    @Column(name = "max_drawdown_rate", precision = 10, scale = 4)
    private BigDecimal maxDrawdownRate;

    @Column(name = "sharpe_ratio", precision = 10, scale = 4)
    private BigDecimal sharpeRatio;

    @Column(name = "total_fees", precision = 20, scale = 8)
    private BigDecimal totalFees;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // 预估时长（秒）

    @Column(name = "started_at")
    private LocalDateTime startedAt; // 实际开始时间

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}













