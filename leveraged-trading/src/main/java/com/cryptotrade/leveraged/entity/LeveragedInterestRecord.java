/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 杠杆利息记录实体
 */
@Entity
@Table(name = "leveraged_interest_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedInterestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 关联的杠杆账户ID

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "borrowed_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal borrowedAmount; // 借款金额

    @Column(name = "interest_rate", precision = 10, scale = 8, nullable = false)
    private BigDecimal interestRate; // 利率

    @Column(name = "interest_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal interestAmount; // 利息金额

    @Column(name = "settlement_cycle", nullable = false)
    private String settlementCycle; // 结算周期: HOURLY, DAILY, WEEKLY

    @Column(name = "settlement_time", nullable = false)
    private LocalDateTime settlementTime; // 结算时间

    @Column(name = "status", nullable = false)
    private String status; // SETTLED（已结算）, PENDING（待结算）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}













