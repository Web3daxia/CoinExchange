/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模拟交易账户实体
 */
@Entity
@Table(name = "simulated_trading_accounts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SimulatedTradingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId; // 用户ID

    @Column(name = "account_no", unique = true, nullable = false, length = 50)
    private String accountNo; // 模拟账户编号（唯一标识）

    @Column(name = "balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // 账户余额

    @Column(name = "initial_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialBalance = BigDecimal.valueOf(10000); // 初始余额（默认10000 USDT）

    @Column(name = "currency", nullable = false, length = 20)
    private String currency = "USDT"; // 币种

    @Column(name = "max_leverage", nullable = false)
    private Integer maxLeverage = 10; // 最大杠杆倍数

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition = BigDecimal.valueOf(1000); // 最大仓位限制

    @Column(name = "max_trade_amount", precision = 20, scale = 8)
    private BigDecimal maxTradeAmount; // 每笔交易最大金额限制

    @Column(name = "account_status", nullable = false, length = 20)
    private String accountStatus = "ACTIVE"; // 账户状态: ACTIVE, FROZEN

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime; // 账户创建时间

    @Column(name = "last_reset_time")
    private LocalDateTime lastResetTime; // 上次重置时间

    @Column(name = "reset_count", nullable = false)
    private Integer resetCount = 0; // 重置次数

    @Column(name = "total_profit", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalProfit = BigDecimal.ZERO; // 累计盈利

    @Column(name = "total_loss", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalLoss = BigDecimal.ZERO; // 累计亏损

    @Column(name = "total_trades", nullable = false)
    private Integer totalTrades = 0; // 总交易次数

    @Column(name = "total_win_trades", nullable = false)
    private Integer totalWinTrades = 0; // 盈利交易次数

    @Column(name = "total_lose_trades", nullable = false)
    private Integer totalLoseTrades = 0; // 亏损交易次数

    @Column(name = "max_drawdown", precision = 20, scale = 8)
    private BigDecimal maxDrawdown = BigDecimal.ZERO; // 最大回撤

    @Column(name = "win_rate", precision = 10, scale = 6)
    private BigDecimal winRate = BigDecimal.ZERO; // 胜率

    @Column(name = "last_trade_time")
    private LocalDateTime lastTradeTime; // 最后交易时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














