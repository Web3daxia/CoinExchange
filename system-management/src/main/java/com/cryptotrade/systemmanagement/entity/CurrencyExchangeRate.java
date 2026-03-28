/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 币种汇率实体
 */
@Entity
@Table(name = "currency_exchange_rates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"currency_id", "base_currency", "target_currency"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class CurrencyExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_id", nullable = false)
    private Long currencyId; // 币种ID

    @Column(name = "base_currency", nullable = false, length = 20)
    private String baseCurrency; // 基准货币: CNY, USD, EUR等

    @Column(name = "target_currency", nullable = false, length = 20)
    private String targetCurrency; // 目标货币（币种代码）: BTC, ETH等

    @Column(name = "exchange_rate", precision = 30, scale = 8, nullable = false)
    private BigDecimal exchangeRate; // 汇率（1个目标货币 = exchange_rate个基准货币）

    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 关联关系（可选，用于查询）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private Currency currency;
}














