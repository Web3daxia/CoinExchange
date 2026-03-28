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
 * 币本位永续合约策略实体
 * 支持套利、对冲、跨期套利等复杂策略
 */
@Entity
@Table(name = "coin_futures_strategies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CoinFuturesStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "strategy_name")
    private String strategyName;

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // ARBITRAGE, HEDGE, INTER_TEMPORAL_ARBITRAGE

    @Column(name = "status", nullable = false)
    private String status; // CONFIGURED, RUNNING, STOPPED, COMPLETED

    // 策略参数（JSON格式存储）
    @Column(name = "strategy_params", columnDefinition = "TEXT")
    private String strategyParams;

    // 套利策略参数
    @Column(name = "pair_name_1")
    private String pairName1; // 第一个交易对

    @Column(name = "pair_name_2")
    private String pairName2; // 第二个交易对（套利用）

    @Column(name = "price_difference_threshold", precision = 10, scale = 4)
    private BigDecimal priceDifferenceThreshold; // 价差阈值

    // 对冲策略参数
    @Column(name = "hedge_pair_name")
    private String hedgePairName; // 对冲交易对

    @Column(name = "hedge_ratio", precision = 10, scale = 4)
    private BigDecimal hedgeRatio; // 对冲比例

    // 跨期套利参数
    @Column(name = "spot_pair_name")
    private String spotPairName; // 现货交易对

    @Column(name = "futures_pair_name")
    private String futuresPairName; // 期货交易对

    @Column(name = "basis_threshold", precision = 10, scale = 4)
    private BigDecimal basisThreshold; // 基差阈值

    // 风险控制参数
    @Column(name = "max_loss", precision = 20, scale = 8)
    private BigDecimal maxLoss;

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition;

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















