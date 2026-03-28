/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 快捷买币订单实体
 */
@Entity
@Table(name = "quick_buy_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class QuickBuyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "crypto_currency", nullable = false)
    private String cryptoCurrency; // 加密货币类型，如 USDT, BTC, ETH

    @Column(name = "fiat_currency", nullable = false)
    private String fiatCurrency; // 法币类型，如 USD, CNY, EUR

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // 支付方式，如 PayPal, WeChat, BankTransfer

    @Column(name = "crypto_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal cryptoAmount; // 购买的加密货币数量

    @Column(name = "fiat_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal fiatAmount; // 支付的法币金额

    @Column(name = "exchange_rate", precision = 20, scale = 8, nullable = false)
    private BigDecimal exchangeRate; // 汇率

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待支付）、PAID（已支付）、COMPLETED（已完成）、CANCELLED（已取消）

    @Column(name = "payment_proof", columnDefinition = "TEXT")
    private String paymentProof; // 支付凭证（图片URL）

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 支付时间

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 完成时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















