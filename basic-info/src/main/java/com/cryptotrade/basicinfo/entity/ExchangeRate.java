/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 汇率实体
 */
@Entity
@Table(name = "exchange_rates")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_currency", nullable = false)
    private String fromCurrency; // 源货币，如 BTC, ETH, USDT

    @Column(name = "to_currency", nullable = false)
    private String toCurrency; // 目标货币，如 USD, EUR, CNY

    @Column(name = "rate", precision = 20, scale = 8, nullable = false)
    private BigDecimal rate; // 汇率

    @Column(name = "source", nullable = false)
    private String source; // 数据源，如 BINANCE, COINBASE, MANUAL

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（有效）、INACTIVE（无效）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















