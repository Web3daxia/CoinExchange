/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("补充保证金请求")
public class TopUpMarginRequest {
    @ApiModelProperty(value = "仓位ID", required = true)
    @NotNull(message = "仓位ID不能为空")
    private Long positionId;

    @ApiModelProperty(value = "补充金额", required = true)
    @NotNull(message = "补充金额不能为空")
    @Positive(message = "补充金额必须大于0")
    private BigDecimal amount;
}















