/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gradient_rules")
@Data
@EntityListeners(AuditingEntityListener.class)
public class GradientRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "position_tier", nullable = false)
    private Integer positionTier; // 仓位档位

    @Column(name = "min_position", precision = 20, scale = 8)
    private BigDecimal minPosition; // 最小仓位

    @Column(name = "max_position", precision = 20, scale = 8)
    private BigDecimal maxPosition; // 最大仓位

    @Column(name = "max_leverage", nullable = false)
    private Integer maxLeverage; // 该档位最大杠杆

    @Column(name = "maintenance_margin_rate", precision = 10, scale = 4)
    private BigDecimal maintenanceMarginRate; // 维持保证金率

    @Column(name = "volatility_factor", precision = 10, scale = 4)
    private BigDecimal volatilityFactor; // 波动率因子

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


