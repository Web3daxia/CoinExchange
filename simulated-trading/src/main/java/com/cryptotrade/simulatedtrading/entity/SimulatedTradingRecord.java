/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模拟交易记录实体
 */
@Entity
@Table(name = "simulated_trading_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SimulatedTradingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 模拟账户ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "trade_no", unique = true, nullable = false, length = 50)
    private String tradeNo; // 交易编号

    @Column(name = "trade_type", nullable = false, length = 50)
    private String tradeType; // 交易类型: SPOT, FUTURES_USDT, FUTURES_COIN, DELIVERY, LEVERAGE, OPTIONS

    @Column(name = "contract_type", length = 50)
    private String contractType; // 合约类型（用于合约交易）

    @Column(name = "pair_name", nullable = false, length = 50)
    private String pairName; // 交易对

    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType; // 订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT等

    @Column(name = "position_side", length = 50)
    private String positionSide; // 仓位方向: LONG, SHORT（用于合约交易）

    @Column(name = "side", nullable = false, length = 20)
    private String side; // 买卖方向: BUY, SELL

    @Column(name = "leverage")
    private Integer leverage = 1; // 杠杆倍数（默认1倍）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 交易数量

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price; // 交易价格（限价单）

    @Column(name = "executed_price", precision = 20, scale = 8)
    private BigDecimal executedPrice; // 成交价格

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 交易金额

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee = BigDecimal.ZERO; // 手续费

    @Column(name = "margin", precision = 20, scale = 8)
    private BigDecimal margin; // 保证金（用于杠杆和合约交易）

    @Column(name = "entry_price", precision = 20, scale = 8)
    private BigDecimal entryPrice; // 开仓价格（用于合约交易）

    @Column(name = "exit_price", precision = 20, scale = 8)
    private BigDecimal exitPrice; // 平仓价格（用于合约交易）

    @Column(name = "profit_loss", precision = 20, scale = 8)
    private BigDecimal profitLoss = BigDecimal.ZERO; // 盈亏金额

    @Column(name = "profit_loss_rate", precision = 10, scale = 6)
    private BigDecimal profitLossRate; // 盈亏比例

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "open_time", nullable = false)
    private LocalDateTime openTime; // 开仓时间/下单时间

    @Column(name = "close_time")
    private LocalDateTime closeTime; // 平仓时间/成交时间

    @Column(name = "trade_status", nullable = false, length = 20)
    private String tradeStatus = "OPEN"; // 交易状态: OPEN, FILLED, CLOSED, CANCELLED, STOPPED, PROFITED

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














