/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 返佣配置实体（用户级别）
 */
@Entity
@Table(name = "rebate_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RebateConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // 用户ID

    @Column(name = "spot_rebate_rate", precision = 10, scale = 4)
    private BigDecimal spotRebateRate; // 现货交易返佣比例

    @Column(name = "futures_usdt_rebate_rate", precision = 10, scale = 4)
    private BigDecimal futuresUsdtRebateRate; // USDT本位合约返佣比例

    @Column(name = "futures_coin_rebate_rate", precision = 10, scale = 4)
    private BigDecimal futuresCoinRebateRate; // 币本位合约返佣比例

    @Column(name = "copy_trading_rebate_rate", precision = 10, scale = 4)
    private BigDecimal copyTradingRebateRate; // 跟单返佣比例

    @Column(name = "options_rebate_rate", precision = 10, scale = 4)
    private BigDecimal optionsRebateRate; // 期权交易返佣比例

    @Column(name = "daily_rebate_limit", precision = 20, scale = 8)
    private BigDecimal dailyRebateLimit; // 每日返佣上限

    @Column(name = "rebate_period", nullable = false)
    private String rebatePeriod; // 返佣周期，如 DAILY（日）、WEEKLY（周）、MONTHLY（月）

    @Column(name = "rebate_method", nullable = false)
    private String rebateMethod; // 返佣方式，如 USDT（USDT）、TOKEN（平台代币）

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















