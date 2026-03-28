/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 跟单关系实体
 */
@Entity
@Table(name = "copy_trading_relations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CopyTradingRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trader_id", nullable = false)
    private Long traderId; // 带单员ID

    @Column(name = "follower_id", nullable = false)
    private Long followerId; // 跟单员ID

    @Column(name = "market_type", nullable = false)
    private String marketType; // SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）

    @Column(name = "copy_type", nullable = false)
    private String copyType; // PUBLIC（公域跟单）、PRIVATE（私域跟单）

    @Column(name = "allocation_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal allocationAmount; // 分配的资金金额

    @Column(name = "allocation_percentage", precision = 10, scale = 4)
    private BigDecimal allocationPercentage; // 分配的资金比例

    @Column(name = "leverage")
    private Integer leverage; // 杠杆倍数（合约跟单）

    @Column(name = "margin_mode")
    private String marginMode; // 保证金模式（合约跟单）：ISOLATED（逐仓）、CROSS（全仓）

    @Column(name = "stop_loss_price", precision = 20, scale = 8)
    private BigDecimal stopLossPrice; // 止损价格

    @Column(name = "take_profit_price", precision = 20, scale = 8)
    private BigDecimal takeProfitPrice; // 止盈价格

    @Column(name = "stop_loss_percentage", precision = 10, scale = 4)
    private BigDecimal stopLossPercentage; // 止损百分比

    @Column(name = "take_profit_percentage", precision = 10, scale = 4)
    private BigDecimal takeProfitPercentage; // 止盈百分比

    @Column(name = "copy_ratio", precision = 10, scale = 4, nullable = false)
    private BigDecimal copyRatio; // 跟单比例（1.0表示100%复制）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（活跃）、PAUSED（已暂停）、STOPPED（已停止）

    @Column(name = "total_profit", precision = 20, scale = 8)
    private BigDecimal totalProfit; // 总盈利

    @Column(name = "total_loss", precision = 20, scale = 8)
    private BigDecimal totalLoss; // 总亏损

    @Column(name = "total_commission", precision = 20, scale = 8)
    private BigDecimal totalCommission; // 总佣金

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















