/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 杠杆交易策略实体
 */
@Entity
@Table(name = "leveraged_strategies")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 关联的杠杆账户ID

    @Column(name = "strategy_name")
    private String strategyName; // 策略名称

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // GRID（网格交易）、TREND_FOLLOWING（趋势跟踪）、REVERSE（反转策略）

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "strategy_params", columnDefinition = "TEXT")
    private String strategyParams; // 策略参数（JSON格式）

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit; // 总盈利

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss; // 总亏损

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、STOPPED（已停止）、PAUSED（已暂停）

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime; // 上次执行时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















