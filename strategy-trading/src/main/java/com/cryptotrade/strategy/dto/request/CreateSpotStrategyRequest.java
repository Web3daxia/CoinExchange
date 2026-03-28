/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建现货策略请求DTO（通用基础类）
 */
@Data
@ApiModel("创建现货策略请求")
public class CreateSpotStrategyRequest {
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "交易对名称", required = true)
    @NotNull(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "初始资金", required = true)
    @NotNull(message = "初始资金不能为空")
    private BigDecimal initialCapital;

    @ApiModelProperty(value = "投资比例（0-1，默认0.5）")
    private BigDecimal investmentRatio;

    @ApiModelProperty(value = "止损百分比")
    private BigDecimal stopLossPercentage;

    @ApiModelProperty(value = "止盈百分比")
    private BigDecimal takeProfitPercentage;

    @ApiModelProperty(value = "最大持仓比例（0-1）")
    private BigDecimal maxPosition;

    @ApiModelProperty(value = "订单类型: MARKET, LIMIT")
    private String orderType;
}













