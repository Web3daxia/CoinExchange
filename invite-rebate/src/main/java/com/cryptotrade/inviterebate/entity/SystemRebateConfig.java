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
 * 系统返佣配置实体（后台管理）
 */
@Entity
@Table(name = "system_rebate_configs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SystemRebateConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_name", nullable = false)
    private String configName; // 配置名称

    @Column(name = "user_level")
    private String userLevel; // 用户等级，如 VIP1, VIP2, NORMAL，NULL表示全局默认

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

    @Column(name = "max_daily_rebate", precision = 20, scale = 8)
    private BigDecimal maxDailyRebate; // 每日返佣上限

    @Column(name = "default_rebate_period", nullable = false)
    private String defaultRebatePeriod; // 默认返佣周期

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（启用）、INACTIVE（禁用）

    @Column(name = "remark")
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















