/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 闪兑订单实体
 */
@Entity
@Table(name = "flash_exchange_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FlashExchangeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo; // 订单号

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "from_currency", nullable = false)
    private String fromCurrency; // 源币种

    @Column(name = "to_currency", nullable = false)
    private String toCurrency; // 目标币种

    @Column(name = "from_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal fromAmount; // 源币种数量

    @Column(name = "to_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal toAmount; // 目标币种数量

    @Column(name = "exchange_rate", precision = 20, scale = 8, nullable = false)
    private BigDecimal exchangeRate; // 兑换汇率

    @Column(name = "expected_rate", precision = 20, scale = 8)
    private BigDecimal expectedRate; // 预期汇率

    @Column(name = "slippage", precision = 10, scale = 4)
    private BigDecimal slippage; // 滑点

    @Column(name = "max_slippage", precision = 10, scale = 4)
    private BigDecimal maxSlippage; // 最大允许滑点

    @Column(name = "fee", precision = 20, scale = 8, nullable = false)
    private BigDecimal fee; // 手续费

    @Column(name = "fee_rate", precision = 10, scale = 4)
    private BigDecimal feeRate; // 手续费率

    @Column(name = "status", nullable = false)
    private String status; // PENDING（处理中）、COMPLETED（已完成）、FAILED（失败）、CANCELLED（已取消）

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @Column(name = "transaction_hash")
    private String transactionHash; // 交易哈希（智能合约）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















