/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交割合约行情数据实体
 */
@Entity
@Table(name = "delivery_market_data")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryMarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 价格

    @Column(name = "volume_24h", precision = 20, scale = 8, nullable = false)
    private BigDecimal volume24h; // 24小时成交量

    @Column(name = "high_24h", precision = 20, scale = 8, nullable = false)
    private BigDecimal high24h; // 24小时最高价

    @Column(name = "low_24h", precision = 20, scale = 8, nullable = false)
    private BigDecimal low24h; // 24小时最低价

    @Column(name = "change_24h", precision = 10, scale = 4, nullable = false)
    private BigDecimal change24h; // 24小时涨跌幅

    @Column(name = "open_interest", precision = 20, scale = 8, nullable = false)
    private BigDecimal openInterest; // 持仓量

    @Column(name = "funding_rate", precision = 10, scale = 6)
    private BigDecimal fundingRate; // 资金费率

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















