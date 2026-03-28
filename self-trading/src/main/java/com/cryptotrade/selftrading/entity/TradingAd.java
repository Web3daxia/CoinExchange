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
 * 交易广告实体
 */
@Entity
@Table(name = "trading_ads")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TradingAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId; // 商家ID

    @Column(name = "ad_type", nullable = false)
    private String adType; // SELL（出售广告）、BUY（买入广告）

    @Column(name = "crypto_currency", nullable = false)
    private String cryptoCurrency; // 加密货币类型，如 USDT, BTC, ETH

    @Column(name = "fiat_currency", nullable = false)
    private String fiatCurrency; // 支付法币，如 USD, CNY, EUR

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 广告单价（法币）

    @Column(name = "min_amount", precision = 20, scale = 8)
    private BigDecimal minAmount; // 最小交易金额

    @Column(name = "max_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal maxAmount; // 最大交易金额（限额）

    @Column(name = "available_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal availableAmount; // 可用金额

    @Column(name = "payment_methods", columnDefinition = "TEXT", nullable = false)
    private String paymentMethods; // 支持的支付方式（JSON数组）

    @Column(name = "require_kyc")
    private Boolean requireKyc; // 是否需要KYC验证

    @Column(name = "require_asset_proof")
    private Boolean requireAssetProof; // 是否需要资产证明

    @Column(name = "require_transaction_history")
    private Boolean requireTransactionHistory; // 是否需要交易流水

    @Column(name = "auto_reply_enabled")
    private Boolean autoReplyEnabled; // 是否自动回复

    @Column(name = "auto_reply_content", columnDefinition = "TEXT")
    private String autoReplyContent; // 自动回复内容

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、PAUSED（已暂停）、CLOSED（已关闭）

    @Column(name = "order_count")
    private Integer orderCount; // 订单数量

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















