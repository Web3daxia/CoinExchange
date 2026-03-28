/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代理商返佣记录实体
 */
@Entity
@Table(name = "agent_rebate_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AgentRebateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rebate_no", unique = true, nullable = false)
    private String rebateNo; // 返佣单号

    @Column(name = "agent_id", nullable = false)
    private Long agentId; // 代理商ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "trade_type", nullable = false)
    private String tradeType; // SPOT（现货）、FUTURES_USDT（USDT本位合约）、FUTURES_COIN（币本位合约）、COPY_TRADING（跟单）、OPTIONS（期权）

    @Column(name = "order_id")
    private Long orderId; // 关联的订单ID

    @Column(name = "fee_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal feeAmount; // 交易手续费金额

    @Column(name = "rebate_rate", precision = 10, scale = 4, nullable = false)
    private BigDecimal rebateRate; // 返佣比例

    @Column(name = "rebate_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal rebateAmount; // 返佣金额

    @Column(name = "rebate_currency", nullable = false)
    private String rebateCurrency; // 返佣币种

    @Column(name = "rebate_period", nullable = false)
    private String rebatePeriod; // 返佣周期

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart; // 周期开始时间

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd; // 周期结束时间

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待结算）、SETTLED（已结算）、CANCELLED（已取消）

    @Column(name = "settled_at")
    private LocalDateTime settledAt; // 结算时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















