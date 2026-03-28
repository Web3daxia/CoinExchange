/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网格机器人结算记录实体
 */
@Entity
@Table(name = "grid_robot_settlements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class GridRobotSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_id", unique = true, nullable = false)
    private String settlementId; // 结算ID

    @Column(name = "robot_id", nullable = false)
    private Long robotId; // 机器人ID

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "initial_capital", precision = 20, scale = 8, nullable = false)
    private BigDecimal initialCapital;

    @Column(name = "final_capital", precision = 20, scale = 8, nullable = false)
    private BigDecimal finalCapital;

    @Column(name = "total_profit", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalProfit;

    @Column(name = "total_loss", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalLoss;

    @Column(name = "net_profit", precision = 20, scale = 8, nullable = false)
    private BigDecimal netProfit;

    @Column(name = "total_fees", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalFees;

    @Column(name = "settlement_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal settlementAmount; // 结算金额

    @Column(name = "status", nullable = false)
    private String status; // PENDING, COMPLETED, FAILED

    @CreatedDate
    @Column(name = "settlement_time")
    private LocalDateTime settlementTime;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}













