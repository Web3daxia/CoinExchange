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
 * 期权持仓实体
 */
@Entity
@Table(name = "option_positions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 关联的期权合约ID

    @Column(name = "option_type", nullable = false)
    private String optionType; // CALL（看涨期权）、PUT（看跌期权）

    @Column(name = "side", nullable = false)
    private String side; // LONG（持有期权）、SHORT（卖出期权）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 持仓数量

    @Column(name = "average_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal averagePrice; // 平均开仓价格（期权费）

    @Column(name = "current_price", precision = 20, scale = 8)
    private BigDecimal currentPrice; // 当前期权价格

    @Column(name = "underlying_price", precision = 20, scale = 8)
    private BigDecimal underlyingPrice; // 标的资产当前价格

    @Column(name = "strike_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal strikePrice; // 执行价格

    @Column(name = "unrealized_pnl", precision = 20, scale = 8)
    private BigDecimal unrealizedPnl; // 未实现盈亏

    @Column(name = "realized_pnl", precision = 20, scale = 8)
    private BigDecimal realizedPnl; // 已实现盈亏

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate; // 到期日期

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、EXERCISED（已行使）、EXPIRED（已到期）、CLOSED（已平仓）

    @Column(name = "margin", precision = 20, scale = 8)
    private BigDecimal margin; // 保证金

    @Column(name = "is_in_the_money")
    private Boolean isInTheMoney; // 是否实值（in-the-money）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















