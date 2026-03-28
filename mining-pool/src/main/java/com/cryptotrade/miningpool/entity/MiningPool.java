/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 矿池实体
 */
@Entity
@Table(name = "mining_pools")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MiningPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pool_name", nullable = false, length = 200)
    private String poolName; // 矿池名称

    @Column(name = "pool_code", unique = true, nullable = false, length = 50)
    private String poolCode; // 矿池代码（唯一标识）

    @Column(name = "mining_currency", nullable = false, length = 20)
    private String miningCurrency; // 挖矿币种（如: BTC, ETH）

    @Column(name = "algorithm", nullable = false, length = 50)
    private String algorithm; // 挖矿算法（如: SHA-256, Ethash）

    @Column(name = "total_hashrate", precision = 30, scale = 8)
    private BigDecimal totalHashrate = BigDecimal.ZERO; // 矿池总算力

    @Column(name = "active_hashrate", precision = 30, scale = 8)
    private BigDecimal activeHashrate = BigDecimal.ZERO; // 活跃算力

    @Column(name = "pool_revenue", precision = 20, scale = 8)
    private BigDecimal poolRevenue = BigDecimal.ZERO; // 矿池总收益

    @Column(name = "distribution_method", nullable = false, length = 50)
    private String distributionMethod = "PPS"; // 收益分配方式: PPS, PPLNS, PROP

    @Column(name = "hashrate_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal hashratePrice; // 算力价格（每单位算力的价格）

    @Column(name = "min_hashrate", precision = 30, scale = 8)
    private BigDecimal minHashrate; // 最低算力门槛

    @Column(name = "max_participants")
    private Integer maxParticipants; // 最大参与用户数

    @Column(name = "current_participants")
    private Integer currentParticipants = 0; // 当前参与用户数

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 矿池状态: ACTIVE, SUSPENDED, CLOSED

    @Column(name = "risk_level", length = 50)
    private String riskLevel = "MEDIUM"; // 风险等级: LOW, MEDIUM, HIGH

    @Column(name = "settlement_cycle", nullable = false, length = 50)
    private String settlementCycle = "DAILY"; // 收益结算周期: DAILY, WEEKLY, MONTHLY

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 矿池描述

    @Column(name = "risk_warning", columnDefinition = "TEXT")
    private String riskWarning; // 风险提示

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 排序顺序

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














