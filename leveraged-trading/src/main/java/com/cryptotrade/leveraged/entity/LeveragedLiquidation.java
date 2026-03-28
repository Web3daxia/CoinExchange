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
 * 杠杆清算记录实体
 */
@Entity
@Table(name = "leveraged_liquidations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedLiquidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 关联的杠杆账户ID

    @Column(name = "position_id", nullable = false)
    private Long positionId; // 关联的仓位ID

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "liquidation_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal liquidationPrice; // 强平价格

    @Column(name = "liquidation_quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal liquidationQuantity; // 强平数量

    @Column(name = "liquidation_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal liquidationAmount; // 强平金额

    @Column(name = "liquidation_fee", precision = 20, scale = 8)
    private BigDecimal liquidationFee; // 强平手续费

    @Column(name = "margin_remaining", precision = 20, scale = 8)
    private BigDecimal marginRemaining; // 剩余保证金

    @Column(name = "reason", length = 500)
    private String reason; // 强平原因

    @Column(name = "liquidation_time", nullable = false)
    private LocalDateTime liquidationTime; // 强平时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















