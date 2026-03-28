/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 机器人交易记录实体
 */
@Entity
@Table(name = "robot_trade_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RobotTradeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "robot_id", nullable = false)
    private Long robotId; // 机器人ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT, FUTURES_USDT, FUTURES_COIN, LEVERAGED

    @Column(name = "order_id")
    private Long orderId; // 关联的订单ID

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "action", nullable = false)
    private String action; // OPEN, CLOSE

    @Column(name = "side", nullable = false)
    private String side; // BUY, SELL

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 交易数量

    @Column(name = "price", precision = 20, scale = 8, nullable = false)
    private BigDecimal price; // 交易价格

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 交易金额

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee; // 手续费

    @Column(name = "profit_loss", precision = 20, scale = 8)
    private BigDecimal profitLoss; // 盈亏（平仓时计算）

    @Column(name = "strategy_type", nullable = false)
    private String strategyType; // 策略类型: GRID, TREND_FOLLOWING, etc.

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
