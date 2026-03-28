/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel("设置保证金模式请求")
public class SetMarginModeRequest {
    @ApiModelProperty(value = "保证金模式: CROSS, ISOLATED, SEGMENTED, COMBINED", required = true)
    @NotBlank(message = "保证金模式不能为空")
    private String marginMode;

    @ApiModelProperty(value = "默认杠杆倍数", example = "10")
    @NotNull(message = "默认杠杆倍数不能为空")
    @Positive(message = "杠杆倍数必须大于0")
    private Integer defaultLeverage;
}


