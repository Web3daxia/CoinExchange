/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自选交易订单实体
 */
@Entity
@Table(name = "self_trading_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SelfTradingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false)
    private String orderNo; // 订单号

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId; // 商家ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "ad_id", nullable = false)
    private Long adId; // 广告ID

    @Column(name = "order_type", nullable = false)
    private String orderType; // BUY（买入）、SELL（卖出）

    @Column(name = "crypto_currency", nullable = false)
    private String cryptoCurrency; // 加密货币类型

    @Column(name = "fiat_currency", nullable = false)
    private String fiatCurrency; // 法币类型

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // 支付方式

    @Column(name = "crypto_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal cryptoAmount; // 加密货币数量

    @Column(name = "fiat_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal fiatAmount; // 法币金额

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 成交价格

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待付款）、PAID（已付款）、RELEASED（已放币）、COMPLETED（已完成）、CANCELLED（已取消）、DISPUTED（申诉中）

    @Column(name = "payment_proof", columnDefinition = "TEXT")
    private String paymentProof; // 支付凭证（图片URL）

    @Column(name = "processing_timeout")
    private Integer processingTimeout; // 处理时限（分钟）

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 支付时间

    @Column(name = "released_at")
    private LocalDateTime releasedAt; // 放币时间

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt; // 取消时间

    @Column(name = "cancelled_reason", length = 500)
    private String cancelledReason; // 取消原因

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















