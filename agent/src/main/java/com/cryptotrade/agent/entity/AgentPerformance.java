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
 * 代理商业绩统计实体
 */
@Entity
@Table(name = "agent_performances")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false)
    private Long agentId; // 代理商ID

    @Column(name = "stat_date", nullable = false)
    private LocalDateTime statDate; // 统计日期

    @Column(name = "stat_period", nullable = false)
    private String statPeriod; // 统计周期，如 DAILY, WEEKLY, MONTHLY

    @Column(name = "invite_user_count", nullable = false)
    private Integer inviteUserCount; // 邀请用户数

    @Column(name = "total_trade_volume", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalTradeVolume; // 总交易量

    @Column(name = "spot_trade_volume", precision = 20, scale = 8)
    private BigDecimal spotTradeVolume; // 现货交易量

    @Column(name = "futures_trade_volume", precision = 20, scale = 8)
    private BigDecimal futuresTradeVolume; // 合约交易量

    @Column(name = "options_trade_volume", precision = 20, scale = 8)
    private BigDecimal optionsTradeVolume; // 期权交易量

    @Column(name = "total_rebate_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal totalRebateAmount; // 总返佣金额

    @Column(name = "settled_rebate_amount", precision = 20, scale = 8)
    private BigDecimal settledRebateAmount; // 已结算返佣金额

    @Column(name = "pending_rebate_amount", precision = 20, scale = 8)
    private BigDecimal pendingRebateAmount; // 待结算返佣金额

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















