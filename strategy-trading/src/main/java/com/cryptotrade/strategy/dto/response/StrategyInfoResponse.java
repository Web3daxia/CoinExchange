/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("策略信息响应")
public class StrategyInfoResponse {
    @ApiModelProperty(value = "支持的策略类型列表")
    private Set<String> supportedStrategyTypes;

    @ApiModelProperty(value = "策略数量")
    private Integer strategyCount;
}















