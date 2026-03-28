/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.response;

import com.cryptotrade.systemmanagement.entity.UsdtFuturesOrderManagement;
import com.cryptotrade.systemmanagement.util.OrderDisplayUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 永续合约委托显示响应DTO（包含格式化后的显示字段）
 * 适用于U本位和币本位合约委托
 */
@Data
public class FuturesOrderDisplayResponse {
    private Long id;
    private Long orderId;
    private String orderNo; // 委托单号
    private Long userId;
    private String memberName;
    private String contractName;
    private String orderTypeDisplay; // 委托类型显示：买入开多，买入开空，卖出开多，卖出开空等
    private String orderType; // 委托类型原始值
    private String orderStatusDisplay; // 委托状态显示
    private String orderStatus; // 委托状态原始值
    private Boolean isLiquidation;
    private BigDecimal quantity; // 委托张数（数量）
    private BigDecimal triggerPrice;
    private BigDecimal price; // 委托价格
    private BigDecimal filledPrice; // 成交价
    private BigDecimal filledQuantity; // 成交张数（数量）
    private BigDecimal openFee; // 冻结开仓手续费
    private BigDecimal marginFrozen; // 冻结/扣除保证金
    private LocalDateTime createdAt; // 挂单时间
    private LocalDateTime triggeredAt; // 触发时间
    private LocalDateTime filledAt; // 成交时间
    private LocalDateTime cancelledAt; // 撤销时间

    /**
     * 从U本位实体转换
     */
    public static FuturesOrderDisplayResponse from(UsdtFuturesOrderManagement entity) {
        FuturesOrderDisplayResponse response = new FuturesOrderDisplayResponse();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setOrderNo(entity.getOrderNo());
        response.setUserId(entity.getUserId());
        response.setMemberName(entity.getMemberName());
        response.setContractName(entity.getContractName());
        // 格式化委托类型显示（买入开多、买入开空等）
        response.setOrderTypeDisplay(OrderDisplayUtil.formatOrderDirection(entity.getOrderDirection(), entity.getSide()));
        response.setOrderType(entity.getOrderType());
        response.setOrderStatusDisplay(OrderDisplayUtil.formatOrderStatus(entity.getStatus()));
        response.setOrderStatus(entity.getStatus());
        response.setIsLiquidation(entity.getIsLiquidation());
        response.setQuantity(entity.getQuantity());
        response.setTriggerPrice(entity.getTriggerPrice());
        response.setPrice(entity.getPrice());
        response.setFilledPrice(entity.getFilledPrice());
        response.setFilledQuantity(entity.getFilledQuantity());
        response.setOpenFee(entity.getOpenFee());
        response.setMarginFrozen(entity.getMarginFrozen());
        response.setCreatedAt(entity.getCreatedAt());
        response.setTriggeredAt(entity.getTriggeredAt());
        response.setFilledAt(entity.getFilledAt());
        response.setCancelledAt(entity.getCancelledAt());
        return response;
    }
}

