/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交割合约持仓响应DTO
 */
@Data
@ApiModel("交割合约持仓响应")
public class DeliveryPositionResponse {
    @ApiModelProperty(value = "持仓ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "合约ID")
    private Long contractId;

    @ApiModelProperty(value = "方向: LONG（多头）、SHORT（空头）")
    private String side;

    @ApiModelProperty(value = "持仓数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "平均开仓价格")
    private BigDecimal avgOpenPrice;

    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "杠杆倍数")
    private BigDecimal leverage;

    @ApiModelProperty(value = "保证金")
    private BigDecimal margin;

    @ApiModelProperty(value = "维持保证金")
    private BigDecimal maintenanceMargin;

    @ApiModelProperty(value = "未实现盈亏")
    private BigDecimal unrealizedPnl;

    @ApiModelProperty(value = "已实现盈亏")
    private BigDecimal realizedPnl;

    @ApiModelProperty(value = "强平价格")
    private BigDecimal liquidationPrice;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "开仓时间")
    private LocalDateTime openedAt;

    @ApiModelProperty(value = "平仓时间")
    private LocalDateTime closedAt;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;
}













