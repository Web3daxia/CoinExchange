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
 * 杠杆账户实体
 */
@Entity
@Table(name = "leveraged_accounts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称，如 BTC/USDT

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数，如 2, 5, 10, 20

    @Column(name = "max_leverage", nullable = false)
    private Integer maxLeverage; // 最大杠杆倍数限制

    @Column(name = "available_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal availableBalance; // 可用余额

    @Column(name = "borrowed_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal borrowedAmount; // 借入资金

    @Column(name = "margin", precision = 20, scale = 8, nullable = false)
    private BigDecimal margin; // 保证金

    @Column(name = "initial_margin", precision = 20, scale = 8)
    private BigDecimal initialMargin; // 初始保证金

    @Column(name = "maintenance_margin", precision = 20, scale = 8)
    private BigDecimal maintenanceMargin; // 维持保证金

    @Column(name = "margin_ratio", precision = 10, scale = 4)
    private BigDecimal marginRatio; // 保证金率

    @Column(name = "interest_rate", precision = 10, scale = 8)
    private BigDecimal interestRate; // 借贷利率

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、FROZEN（冻结）、CLOSED（关闭）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















