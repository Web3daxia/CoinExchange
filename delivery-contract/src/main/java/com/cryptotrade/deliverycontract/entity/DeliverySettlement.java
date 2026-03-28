/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交割合约结算记录实体
 */
@Entity
@Table(name = "delivery_settlements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliverySettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_no", unique = true, nullable = false)
    private String settlementNo; // 结算单号

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "position_id", nullable = false)
    private Long positionId; // 持仓ID

    @Column(name = "settlement_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal settlementPrice; // 结算价格

    @Column(name = "settlement_pnl", precision = 20, scale = 8, nullable = false)
    private BigDecimal settlementPnl; // 结算盈亏

    @Column(name = "settlement_type", nullable = false)
    private String settlementType; // 结算类型: DELIVERY（交割）、LIQUIDATION（强平）、MANUAL（手动）

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待结算）、SETTLED（已结算）、FAILED（结算失败）

    @Column(name = "settled_at")
    private LocalDateTime settledAt; // 结算时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















