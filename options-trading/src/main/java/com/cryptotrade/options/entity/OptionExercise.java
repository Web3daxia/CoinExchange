/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 期权行使记录实体
 */
@Entity
@Table(name = "option_exercises")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OptionExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "position_id", nullable = false)
    private Long positionId; // 关联的期权持仓ID

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 关联的期权合约ID

    @Column(name = "option_type", nullable = false)
    private String optionType; // CALL（看涨期权）、PUT（看跌期权）

    @Column(name = "exercise_type", nullable = false)
    private String exerciseType; // MANUAL（手动行使）、AUTO（自动行使）

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 行使数量

    @Column(name = "strike_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal strikePrice; // 执行价格

    @Column(name = "underlying_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal underlyingPrice; // 行使时的标的资产价格

    @Column(name = "exercise_price", precision = 20, scale = 8, nullable = false)
    private BigDecimal exercisePrice; // 行使价格（执行价格）

    @Column(name = "profit_loss", precision = 20, scale = 8)
    private BigDecimal profitLoss; // 盈亏

    @Column(name = "exercise_fee", precision = 20, scale = 8)
    private BigDecimal exerciseFee; // 行使手续费

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待处理）、EXECUTED（已执行）、FAILED（失败）

    @Column(name = "executed_at")
    private LocalDateTime executedAt; // 执行时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















