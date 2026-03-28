/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质押借币利率调整历史实体
 */
@Entity
@Table(name = "pledge_loan_rate_history")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PledgeLoanRateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_type", nullable = false, length = 20)
    private String configType; // 配置类型: PLEDGE, LOAN

    @Column(name = "currency_code", nullable = false, length = 20)
    private String currencyCode; // 币种代码

    @Column(name = "rate_type", nullable = false, length = 20)
    private String rateType; // 利率类型: INTEREST_RATE, LOAN_RATIO, RISK_RATE

    @Column(name = "old_value", precision = 20, scale = 8)
    private BigDecimal oldValue; // 旧值

    @Column(name = "new_value", precision = 20, scale = 8, nullable = false)
    private BigDecimal newValue; // 新值

    @Column(name = "operator_id", nullable = false)
    private Long operatorId; // 操作人ID

    @Column(name = "operator_name", length = 200)
    private String operatorName; // 操作人名称

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














