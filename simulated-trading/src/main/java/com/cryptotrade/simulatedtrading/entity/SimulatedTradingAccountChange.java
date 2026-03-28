/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模拟交易账户变更记录实体
 */
@Entity
@Table(name = "simulated_trading_account_changes")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SimulatedTradingAccountChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId; // 模拟账户ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "change_type", nullable = false, length = 50)
    private String changeType; // 变更类型: INIT, RESET, TRADE_PROFIT, TRADE_LOSS, DEPOSIT, WITHDRAW

    @Column(name = "change_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal changeAmount; // 变更金额（正数为增加，负数为减少）

    @Column(name = "before_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal beforeBalance; // 变更前余额

    @Column(name = "after_balance", precision = 20, scale = 8, nullable = false)
    private BigDecimal afterBalance; // 变更后余额

    @Column(name = "trade_id")
    private Long tradeId; // 关联交易记录ID

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














