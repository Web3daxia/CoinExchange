/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 模拟交易规则配置实体（后台管理）
 */
@Entity
@Table(name = "simulated_trading_rules")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SimulatedTradingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false, length = 200)
    private String ruleName; // 规则名称

    @Column(name = "rule_key", unique = true, nullable = false, length = 100)
    private String ruleKey; // 规则键（唯一标识）

    @Column(name = "rule_value", nullable = false, length = 500)
    private String ruleValue; // 规则值（JSON格式或具体值）

    @Column(name = "rule_type", nullable = false, length = 50)
    private String ruleType; // 规则类型: INITIAL_BALANCE, MAX_LEVERAGE, MAX_POSITION, MAX_TRADE_AMOUNT

    @Column(name = "description", length = 500)
    private String description; // 规则描述

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // 状态: ACTIVE, INACTIVE

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














