/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel("调整杠杆请求")
public class AdjustLeverageRequest {
    @ApiModelProperty(value = "仓位ID", required = true)
    @NotNull(message = "仓位ID不能为空")
    private Long positionId;

    @ApiModelProperty(value = "新杠杆倍数", required = true, example = "20")
    @NotNull(message = "杠杆倍数不能为空")
    @Positive(message = "杠杆倍数必须大于0")
    private Integer newLeverage;
}















