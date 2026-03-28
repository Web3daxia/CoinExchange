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
 * 用户矿工记录实体（用户分配到矿池的算力）
 */
@Entity
@Table(name = "mining_workers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "pool_id"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class MiningWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID（矿工）

    @Column(name = "pool_id", nullable = false)
    private Long poolId; // 矿池ID

    @Column(name = "rental_id")
    private Long rentalId; // 算力租赁记录ID

    @Column(name = "hashrate_contribution", precision = 30, scale = 8, nullable = false)
    private BigDecimal hashrateContribution; // 算力贡献

    @Column(name = "unit", nullable = false, length = 20)
    private String unit; // 算力单位

    @Column(name = "total_revenue", precision = 20, scale = 8)
    private BigDecimal totalRevenue = BigDecimal.ZERO; // 总收益

    @Column(name = "accumulated_revenue", precision = 20, scale = 8)
    private BigDecimal accumulatedRevenue = BigDecimal.ZERO; // 累计收益

    @Column(name = "last_settlement_time")
    private LocalDateTime lastSettlementTime; // 上次结算时间

    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinTime; // 加入矿池时间

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE, EXITED

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














