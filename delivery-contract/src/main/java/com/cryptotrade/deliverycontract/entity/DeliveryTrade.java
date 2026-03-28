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
 * 交割合约成交记录实体
 */
@Entity
@Table(name = "delivery_trades")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_no", unique = true, nullable = false)
    private String tradeNo; // 成交单号

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "buy_order_id", nullable = false)
    private Long buyOrderId; // 买方订单ID

    @Column(name = "sell_order_id", nullable = false)
    private Long sellOrderId; // 卖方订单ID

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId; // 买方用户ID

    @Column(name = "seller_id", nullable = false)
    private Long sellerId; // 卖方用户ID

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 成交价格

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 成交数量

    @Column(name = "fee", precision = 20, scale = 8, nullable = false)
    private BigDecimal fee; // 手续费

    @Column(name = "fee_currency", nullable = false)
    private String feeCurrency; // 手续费币种

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















