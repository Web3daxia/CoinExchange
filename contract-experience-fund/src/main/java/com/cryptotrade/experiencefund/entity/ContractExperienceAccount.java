/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合约体验金账户实体
 */
@Entity
@Table(name = "contract_experience_accounts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ContractExperienceAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId; // 用户ID

    @Column(name = "account_no", unique = true, nullable = false, length = 50)
    private String accountNo; // 体验金账户编号（唯一标识）

    @Column(name = "balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // 体验金余额

    @Column(name = "initial_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialAmount; // 初始体验金金额

    @Column(name = "currency", nullable = false, length = 20)
    private String currency = "USDT"; // 币种

    @Column(name = "max_leverage", nullable = false)
    private Integer maxLeverage = 10; // 最大杠杆倍数

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition; // 最大仓位限制

    @Column(name = "daily_trade_limit")
    private Integer dailyTradeLimit; // 每日最大交易次数

    @Column(name = "daily_trade_count", nullable = false)
    private Integer dailyTradeCount = 0; // 当日交易次数

    @Column(name = "last_trade_date")
    private LocalDate lastTradeDate; // 最后交易日期

    @Column(name = "account_status", nullable = false, length = 20)
    private String accountStatus = "ACTIVE"; // 账户状态: ACTIVE, FROZEN, EXPIRED, CLOSED

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime; // 账户创建时间

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime; // 过期时间

    @Column(name = "last_reset_time")
    private LocalDateTime lastResetTime; // 上次重置时间

    @Column(name = "total_profit", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalProfit = BigDecimal.ZERO; // 累计盈利

    @Column(name = "total_loss", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalLoss = BigDecimal.ZERO; // 累计亏损

    @Column(name = "total_trades", nullable = false)
    private Integer totalTrades = 0; // 总交易次数

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














