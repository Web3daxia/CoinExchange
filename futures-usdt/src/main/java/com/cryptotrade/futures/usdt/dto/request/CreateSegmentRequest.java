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
import java.math.BigDecimal;

@Data
@ApiModel("创建分仓请求")
public class CreateSegmentRequest {
    @ApiModelProperty(value = "分仓名称", required = true)
    @NotBlank(message = "分仓名称不能为空")
    private String segmentName;

    @ApiModelProperty(value = "分仓保证金", required = true)
    @NotNull(message = "分仓保证金不能为空")
    @Positive(message = "分仓保证金必须大于0")
    private BigDecimal margin;

    @ApiModelProperty(value = "杠杆倍数", required = true)
    @NotNull(message = "杠杆倍数不能为空")
    @Positive(message = "杠杆倍数必须大于0")
    private Integer leverage;
}















