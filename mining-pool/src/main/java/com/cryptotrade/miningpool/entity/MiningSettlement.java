/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挖矿收益结算记录实体
 */
@Entity
@Table(name = "mining_settlements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MiningSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "worker_id", nullable = false)
    private Long workerId; // 矿工记录ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "pool_id", nullable = false)
    private Long poolId; // 矿池ID

    @Column(name = "settlement_period_start", nullable = false)
    private LocalDateTime settlementPeriodStart; // 结算周期开始时间

    @Column(name = "settlement_period_end", nullable = false)
    private LocalDateTime settlementPeriodEnd; // 结算周期结束时间

    @Column(name = "hashrate_contribution", precision = 30, scale = 8, nullable = false)
    private BigDecimal hashrateContribution; // 算力贡献

    @Column(name = "pool_total_hashrate", precision = 30, scale = 8, nullable = false)
    private BigDecimal poolTotalHashrate; // 矿池总算力

    @Column(name = "pool_revenue", precision = 20, scale = 8, nullable = false)
    private BigDecimal poolRevenue; // 矿池收益（该周期）

    @Column(name = "user_revenue", precision = 20, scale = 8, nullable = false)
    private BigDecimal userRevenue; // 用户应得收益

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 收益币种

    @Column(name = "distribution_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal distributionRate; // 分配比例（用户算力/矿池总算力）

    @Column(name = "settlement_type", nullable = false, length = 50)
    private String settlementType; // 结算类型: BLOCK_REWARD, FEE, BOTH

    @Column(name = "settlement_status", nullable = false, length = 20)
    private String settlementStatus = "PENDING"; // 结算状态: PENDING, SETTLED, FAILED

    @Column(name = "settlement_time")
    private LocalDateTime settlementTime; // 结算时间

    @Column(name = "settlement_order_no", length = 50)
    private String settlementOrderNo; // 结算订单号

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














