/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建均线交叉策略请求DTO
 */
@Data
@ApiModel("创建均线交叉策略请求")
public class CreateMaCrossStrategyRequest extends CreateSpotStrategyRequest {
    @ApiModelProperty(value = "短期均线周期", required = true)
    @NotNull(message = "短期均线周期不能为空")
    private Integer shortPeriod;

    @ApiModelProperty(value = "长期均线周期", required = true)
    @NotNull(message = "长期均线周期不能为空")
    private Integer longPeriod;

    @ApiModelProperty(value = "均线类型: SMA, EMA", required = true)
    @NotNull(message = "均线类型不能为空")
    private String maType;
}













