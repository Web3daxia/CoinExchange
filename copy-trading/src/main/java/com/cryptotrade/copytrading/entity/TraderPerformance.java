/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 带单员表现数据实体
 * 按周期存储带单员的业绩数据
 */
@Entity
@Table(name = "trader_performances")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TraderPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trader_id", nullable = false)
    private Long traderId;

    @Column(name = "period_type", nullable = false)
    private String periodType; // DAILY（日）、WEEKLY（周）、MONTHLY（月）、YEARLY（年）

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart; // 周期开始时间

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd; // 周期结束时间

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit; // 总盈利

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss; // 总亏损

    @Column(name = "net_profit", precision = 20, scale = 8)
    private BigDecimal netProfit; // 净利润

    @Column(name = "return_rate", precision = 10, scale = 4)
    private BigDecimal returnRate; // 收益率

    @Column(name = "win_rate", precision = 10, scale = 4)
    private BigDecimal winRate; // 胜率

    @Column(name = "total_trades")
    private Integer totalTrades; // 总交易笔数

    @Column(name = "winning_trades")
    private Integer winningTrades; // 盈利笔数

    @Column(name = "losing_trades")
    private Integer losingTrades; // 亏损笔数

    @Column(name = "avg_profit", precision = 20, scale = 8)
    private BigDecimal avgProfit; // 平均盈利

    @Column(name = "avg_loss", precision = 20, scale = 8)
    private BigDecimal avgLoss; // 平均亏损

    @Column(name = "profit_loss_ratio", precision = 10, scale = 4)
    private BigDecimal profitLossRatio; // 盈亏比

    @Column(name = "sharpe_ratio", precision = 10, scale = 4)
    private BigDecimal sharpeRatio; // 夏普比率

    @Column(name = "max_drawdown", precision = 10, scale = 4)
    private BigDecimal maxDrawdown; // 最大回撤

    @Column(name = "total_aum", precision = 20, scale = 8)
    private BigDecimal totalAum; // 总资产管理规模

    @Column(name = "total_followers")
    private Integer totalFollowers; // 总跟单人数

    @Column(name = "daily_avg_trades", precision = 10, scale = 2)
    private BigDecimal dailyAvgTrades; // 日均交易频次

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















