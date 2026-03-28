/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 交割合约审计日志实体
 */
@Entity
@Table(name = "delivery_audit_logs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DeliveryAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 用户ID

    @Column(name = "contract_id")
    private Long contractId; // 合约ID

    @Column(name = "order_id")
    private Long orderId; // 订单ID

    @Column(name = "position_id")
    private Long positionId; // 持仓ID

    @Column(name = "action_type", nullable = false)
    private String actionType; // 操作类型: ORDER_CREATE（订单创建）、ORDER_FILLED（订单成交）、POSITION_OPEN（开仓）、POSITION_CLOSE（平仓）、LIQUIDATION（强平）

    @Column(name = "action_detail", columnDefinition = "TEXT")
    private String actionDetail; // 操作详情（JSON格式）

    @Column(name = "ip_address")
    private String ipAddress; // IP地址

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















