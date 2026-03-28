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
 * 用户算力租赁记录实体
 */
@Entity
@Table(name = "hashrate_rentals")
@Data
@EntityListeners(AuditingEntityListener.class)
public class HashrateRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "pool_id", nullable = false)
    private Long poolId; // 矿池ID

    @Column(name = "hashrate_type_id", nullable = false)
    private Long hashrateTypeId; // 算力类型ID

    @Column(name = "hashrate_amount", precision = 30, scale = 8, nullable = false)
    private BigDecimal hashrateAmount; // 租赁的算力数量

    @Column(name = "unit", nullable = false, length = 20)
    private String unit; // 算力单位

    @Column(name = "rental_period", nullable = false)
    private Integer rentalPeriod; // 租赁周期（天数）

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 租赁开始时间

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // 租赁结束时间

    @Column(name = "total_cost", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalCost; // 总租赁费用

    @Column(name = "paid_amount", precision = 20, scale = 8)
    private BigDecimal paidAmount = BigDecimal.ZERO; // 已支付金额

    @Column(name = "currency", nullable = false, length = 20)
    private String currency; // 支付币种

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 租赁状态: ACTIVE, EXPIRED, CANCELLED

    @Column(name = "rental_order_no", unique = true, nullable = false, length = 50)
    private String rentalOrderNo; // 租赁订单号

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














