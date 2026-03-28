/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "liquidation_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LiquidationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "pair_name", nullable = false)
    private String pairName;

    @Column(name = "liquidation_price", precision = 20, scale = 8)
    private BigDecimal liquidationPrice;

    @Column(name = "liquidation_quantity", precision = 20, scale = 8)
    private BigDecimal liquidationQuantity;

    @Column(name = "liquidation_type")
    private String liquidationType; // AUTO, MANUAL, FORCED

    @Column(name = "loss_amount", precision = 20, scale = 8)
    private BigDecimal lossAmount;

    @Column(name = "margin_used", precision = 20, scale = 8)
    private BigDecimal marginUsed;

    @Column(name = "reason", length = 500)
    private String reason;

    @CreatedDate
    @Column(name = "liquidation_at")
    private LocalDateTime liquidationAt;
}


