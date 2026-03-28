/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 现货订单管理实体
 */
@Entity
@Table(name = "spot_order_management")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SpotOrderManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId; // 订单ID（关联spot_orders表）

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "member_uid")
    private String memberUid; // 会员UID

    @Column(name = "member_name")
    private String memberName; // 会员名称

    @Column(name = "email")
    private String email; // 会员邮箱

    @Column(name = "phone")
    private String phone; // 手机号码

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "base_currency")
    private String baseCurrency; // 交易币种

    @Column(name = "quote_currency")
    private String quoteCurrency; // 结算币种

    @Column(name = "order_no", nullable = false)
    private String orderNo; // 委托单号

    @Column(name = "order_type", nullable = false)
    private String orderType; // 挂单类型: LIMIT, MARKET, STOP_LOSS, TAKE_PROFIT等

    @Column(name = "side", nullable = false)
    private String side; // 订单方向: BUY, SELL

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 委托数量

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price; // 挂单价格

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity = BigDecimal.ZERO; // 成交量

    @Column(name = "filled_amount", precision = 20, scale = 8)
    private BigDecimal filledAmount = BigDecimal.ZERO; // 成交金额

    @Column(name = "fee", precision = 20, scale = 8)
    private BigDecimal fee = BigDecimal.ZERO; // 手续费

    @Column(name = "status", nullable = false)
    private String status; // 订单状态: PENDING, FILLED, CANCELLED, TIMEOUT

    @Column(name = "order_source", nullable = false)
    private String orderSource = "USER"; // 订单来源: USER, ROBOT

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 挂单时间

    @Column(name = "filled_at")
    private LocalDateTime filledAt; // 成交时间/完成时间

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt; // 取消时间

    @CreatedDate
    @Column(name = "created_at_manage")
    private LocalDateTime createdAtManage;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














