/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@ApiModel("执行策略请求")
public class ExecuteStrategyRequest {
    @ApiModelProperty(value = "交易对名称", required = true, example = "BTC/USDT")
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "市场类型: SPOT, FUTURES_USDT, FUTURES_COIN, LEVERAGED", required = true)
    @NotBlank(message = "市场类型不能为空")
    private String marketType;

    @ApiModelProperty(value = "策略类型", required = true)
    @NotBlank(message = "策略类型不能为空")
    private String strategyType;

    @ApiModelProperty(value = "策略参数（Map格式）")
    private Map<String, Object> strategyParams;
}















