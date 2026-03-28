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

@Entity
@Table(name = "trading_robots")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TradingRobot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "robot_name")
    private String robotName;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）、LEVERAGED（杠杆）

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // GRID, TREND_FOLLOWING, REVERSE, etc.

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

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss = BigDecimal.ZERO;

    @Column(name = "total_trades")
    private Integer totalTrades = 0; // 总交易次数

    // 网格机器人专用字段
    @Column(name = "grid_count")
    private Integer gridCount; // 网格数量

    @Column(name = "upper_price", precision = 20, scale = 8)
    private BigDecimal upperPrice; // 卖出区间上限价格

    @Column(name = "lower_price", precision = 20, scale = 8)
    private BigDecimal lowerPrice; // 买入区间下限价格

    @Column(name = "start_price", precision = 20, scale = 8)
    private BigDecimal startPrice; // 起始价格

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 当前价格

    @Column(name = "initial_capital", precision = 20, scale = 8)
    private BigDecimal initialCapital; // 初始资金

    @Column(name = "current_capital", precision = 20, scale = 8)
    private BigDecimal currentCapital; // 当前资金

    @Column(name = "available_capital", precision = 20, scale = 8)
    private BigDecimal availableCapital; // 可用资金

    @Column(name = "used_capital", precision = 20, scale = 8)
    private BigDecimal usedCapital; // 已用资金

    @Column(name = "investment_ratio", precision = 10, scale = 4)
    private BigDecimal investmentRatio; // 投资比例

    @Column(name = "current_position", precision = 20, scale = 8)
    private BigDecimal currentPosition; // 当前持仓

    @Column(name = "active_orders")
    private Integer activeOrders = 0; // 活跃订单数

    @Column(name = "completed_trades")
    private Integer completedTrades = 0; // 已完成交易数

    @Column(name = "winning_trades")
    private Integer winningTrades = 0; // 盈利交易数

    @Column(name = "losing_trades")
    private Integer losingTrades = 0; // 亏损交易数

    @Column(name = "win_rate", precision = 10, scale = 4)
    private BigDecimal winRate; // 胜率

    @Column(name = "average_buy_price", precision = 20, scale = 8)
    private BigDecimal averageBuyPrice; // 平均买入价格

    @Column(name = "average_sell_price", precision = 20, scale = 8)
    private BigDecimal averageSellPrice; // 平均卖出价格

    @Column(name = "profit_rate", precision = 10, scale = 4)
    private BigDecimal profitRate; // 收益率

    @Column(name = "net_profit", precision = 20, scale = 8)
    private BigDecimal netProfit = BigDecimal.ZERO; // 净盈利

    @Column(name = "stop_loss_percentage", precision = 10, scale = 4)
    private BigDecimal stopLossPercentage; // 止损百分比

    @Column(name = "take_profit_percentage", precision = 10, scale = 4)
    private BigDecimal takeProfitPercentage; // 止盈百分比

    // 合约市场专用字段
    @Column(name = "leverage")
    private Integer leverage; // 杠杆倍数

    @Column(name = "margin_mode")
    private String marginMode; // 保证金模式: ISOLATED, CROSS

    @Column(name = "start_time")
    private LocalDateTime startTime; // 开始时间

    @Column(name = "paused_at")
    private LocalDateTime pausedAt; // 暂停时间

    @Column(name = "resumed_at")
    private LocalDateTime resumedAt; // 恢复时间

    @Column(name = "stopped_at")
    private LocalDateTime stoppedAt; // 停止时间

    @Column(name = "settlement_status")
    private String settlementStatus; // 结算状态: PENDING, COMPLETED

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

