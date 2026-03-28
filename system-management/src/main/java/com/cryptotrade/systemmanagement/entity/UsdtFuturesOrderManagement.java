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
 * U本位永续合约委托管理实体
 */
@Entity
@Table(name = "usdt_futures_order_management")
@Data
@EntityListeners(AuditingEntityListener.class)
public class UsdtFuturesOrderManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId; // 订单ID（关联futures_orders表）

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

    @Column(name = "agent_id")
    private Long agentId; // 所属的代理商ID

    @Column(name = "contract_id", nullable = false)
    private Long contractId; // 合约ID

    @Column(name = "contract_name")
    private String contractName; // 合约名称

    @Column(name = "pair_name")
    private String pairName; // 合约交易对

    @Column(name = "order_no", nullable = false)
    private String orderNo; // 委托单号

    @Column(name = "order_type", nullable = false)
    private String orderType; // 委托类型: LIMIT, MARKET, STOP_LOSS等

    @Column(name = "order_category", nullable = false)
    private String orderCategory; // 委托分类: OPEN, CLOSE

    @Column(name = "order_direction", nullable = false)
    private String orderDirection; // 委托方向: OPEN_LONG, OPEN_SHORT, CLOSE_LONG, CLOSE_SHORT

    @Column(name = "side", nullable = false)
    private String side; // 买卖方向: BUY, SELL

    @Column(name = "position_side")
    private String positionSide; // 仓位方向: LONG, SHORT

    @Column(name = "quantity", precision = 20, scale = 8, nullable = false)
    private BigDecimal quantity; // 委托张数（数量）

    @Column(name = "trigger_price", precision = 20, scale = 8)
    private BigDecimal triggerPrice; // 触发价格

    @Column(name = "price", precision = 20, scale = 8)
    private BigDecimal price; // 委托价格

    @Column(name = "filled_price", precision = 20, scale = 8)
    private BigDecimal filledPrice; // 成交价

    @Column(name = "filled_quantity", precision = 20, scale = 8)
    private BigDecimal filledQuantity = BigDecimal.ZERO; // 成交张数（数量）

    @Column(name = "open_fee", precision = 20, scale = 8)
    private BigDecimal openFee = BigDecimal.ZERO; // 冻结开仓手续费

    @Column(name = "margin_frozen", precision = 20, scale = 8)
    private BigDecimal marginFrozen = BigDecimal.ZERO; // 冻结/扣除保证金

    @Column(name = "status", nullable = false)
    private String status; // 委托状态: PENDING, CANCELLED, FILLED, FAILED

    @Column(name = "is_liquidation", nullable = false)
    private Boolean isLiquidation = false; // 是否爆仓单

    @Column(name = "is_planned_order", nullable = false)
    private Boolean isPlannedOrder = false; // 是否计划委托

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 挂单时间

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt; // 触发时间

    @Column(name = "filled_at")
    private LocalDateTime filledAt; // 成交时间

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt; // 撤销时间

    @CreatedDate
    @Column(name = "created_at_manage")
    private LocalDateTime createdAtManage;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














