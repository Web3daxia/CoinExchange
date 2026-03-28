/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 行情源配置实体
 */
@Entity
@Table(name = "market_data_source_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MarketDataSourceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_source", nullable = false)
    private String dataSource; // 行情源: BINANCE, HUOBI, OKX, GATE

    @Column(name = "data_source_name", nullable = false)
    private String dataSourceName; // 行情源名称: 币安, 火币, OKX, Gate

    @Column(name = "trading_area", nullable = false)
    private String tradingArea; // 交易区域: SPOT, FUTURES_USDT, FUTURES_COIN, OPTIONS, LEVERAGED, DELIVERY

    @Column(name = "api_endpoint")
    private String apiEndpoint; // API端点URL

    @Column(name = "api_key")
    private String apiKey; // API Key（如果需要）

    @Column(name = "api_secret")
    private String apiSecret; // API Secret（如果需要）

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false; // 是否启用

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false; // 是否默认（默认使用币安）

    @Column(name = "priority")
    private Integer priority = 0; // 优先级（数字越大优先级越高）

    @Column(name = "config_params", columnDefinition = "TEXT")
    private String configParams; // 其他配置参数（JSON格式）

    @Column(name = "description")
    private String description; // 描述

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














