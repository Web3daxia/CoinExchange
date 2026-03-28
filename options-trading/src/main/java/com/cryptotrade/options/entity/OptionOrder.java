/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 期权订单实体
 */
@Entity
@Table(name = "option_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 关联的期权合约ID

    @Column(name = "order_type", nullable = false)
    private String orderType; // OPEN（开仓）、CLOSE（平仓）

    @Column(name = "side", nullable = false)
    private String side; // BUY（买入期权）、SELL（卖出期权）

    @Column(name = "option_type", nullable = false)
    private String optionType; // CALL（看涨期权）、PUT（看跌期权）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 期权数量

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 期权价格（期权费）

    @Column(name = "total_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalAmount; // 总金额（数量 * 价格）

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待成交）、FILLED（已成交）、CANCELLED（已取消）、REJECTED（已拒绝）

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity; // 已成交数量

    @Column(name = "filled_amount", precision = 20, scale = 8)
    private BigDecimal filledAmount; // 已成交金额

    @Column(name = "filled_at")
    private LocalDateTime filledAt; // 成交时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















