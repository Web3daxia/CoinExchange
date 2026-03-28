/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 合约体验金交易记录实体
 */
@Entity
@Table(name = "contract_experience_trades")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ContractExperienceTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 体验金账户ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "trade_no", unique = true, nullable = false, length = 50)
    private String tradeNo; // 交易编号

    @Column(name = "contract_type", nullable = false, length = 50)
    private String contractType; // 合约类型: FUTURES_USDT, FUTURES_COIN, PERPETUAL

    @Column(name = "pair_name", nullable = false, length = 50)
    private String pairName; // 交易对

    @Column(name = "trade_type", nullable = false, length = 50)
    private String tradeType; // 交易类型: OPEN_LONG, OPEN_SHORT, CLOSE_LONG, CLOSE_SHORT

    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType; // 订单类型: MARKET, LIMIT

    @Column(name = "leverage", nullable = false)
    private Integer leverage; // 杠杆倍数

    @Column(name = "position_size", precision = 20, scale = 8, nullable = false)
    private BigDecimal positionSize; // 仓位大小

    @Column(name = "entry_price", precision = 20, scale = 8)
    private BigDecimal entryPrice; // 开仓价格

    @Column(name = "exit_price", precision = 20, scale = 8)
    private BigDecimal exitPrice; // 平仓价格

    @Column(name = "open_fee", precision = 20, scale = 8)
    private BigDecimal openFee = BigDecimal.ZERO; // 开仓手续费

    @Column(name = "close_fee", precision = 20, scale = 8)
    private BigDecimal closeFee = BigDecimal.ZERO; // 平仓手续费

    @Column(name = "profit_loss", precision = 20, scale = 8)
    private BigDecimal profitLoss = BigDecimal.ZERO; // 盈亏金额

    @Column(name = "profit_loss_rate", precision = 10, scale = 6)
    private BigDecimal profitLossRate; // 盈亏比例

    @Column(name = "open_time", nullable = false)
    private LocalDateTime openTime; // 开仓时间

    @Column(name = "close_time")
    private LocalDateTime closeTime; // 平仓时间

    @Column(name = "trade_status", nullable = false, length = 20)
    private String tradeStatus = "OPEN"; // 交易状态: OPEN, CLOSED, LIQUIDATED

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














