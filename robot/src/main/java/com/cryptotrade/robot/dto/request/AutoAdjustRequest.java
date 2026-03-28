/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 智能调整网格参数请求DTO
 */
@Data
@ApiModel("智能调整网格参数请求")
public class AutoAdjustRequest {
    @ApiModelProperty(value = "调整类型: AUTO（自动）, MANUAL（手动）", required = true)
    @NotNull(message = "调整类型不能为空")
    private String adjustType;

    @ApiModelProperty(value = "波动率计算周期（小时，默认24）")
    private Integer volatilityPeriod = 24;

    @ApiModelProperty(value = "目标网格数量（手动调整时必填）")
    private Integer targetGridCount;

    @ApiModelProperty(value = "是否调整价格区间（默认true）")
    private Boolean adjustPriceRange = true;
}













