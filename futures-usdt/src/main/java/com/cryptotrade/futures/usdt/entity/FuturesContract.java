/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "futures_contracts")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FuturesContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", unique = true, nullable = false)
    private String pairName; // 如 BTC/USDT

    @Column(name = "base_currency")
    private String baseCurrency; // BTC

    @Column(name = "quote_currency")
    private String quoteCurrency; // USDT

    @Column(name = "contract_type")
    private String contractType; // USDT_MARGINED, COIN_MARGINED（USDT本位、币本位）
    
    @Column(name = "settlement_currency")
    private String settlementCurrency; // 结算货币（USDT或基础货币）

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "index_price", precision = 20, scale = 8)
    private BigDecimal indexPrice; // 指数价格

    @Column(name = "mark_price", precision = 20, scale = 8)
    private BigDecimal markPrice; // 标记价格

    @Column(name = "funding_rate", precision = 10, scale = 8)
    private BigDecimal fundingRate; // 资金费率

    @Column(name = "next_funding_time")
    private LocalDateTime nextFundingTime; // 下次资金费率结算时间

    @Column(name = "price_change_24h", precision = 10, scale = 4)
    private BigDecimal priceChange24h;

    @Column(name = "volume_24h", precision = 20, scale = 8)
    private BigDecimal volume24h;

    @Column(name = "amount_24h", precision = 20, scale = 8)
    private BigDecimal amount24h; // 24小时成交额

    @Column(name = "high_24h", precision = 20, scale = 8)
    private BigDecimal high24h;

    @Column(name = "low_24h", precision = 20, scale = 8)
    private BigDecimal low24h;

    @Column(name = "max_leverage")
    private Integer maxLeverage; // 最大杠杆倍数

    @Column(name = "min_leverage")
    private Integer minLeverage; // 最小杠杆倍数

    @Column(name = "contract_size", precision = 20, scale = 8)
    private BigDecimal contractSize; // 合约面值

    @Column(name = "status")
    private String status; // ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















