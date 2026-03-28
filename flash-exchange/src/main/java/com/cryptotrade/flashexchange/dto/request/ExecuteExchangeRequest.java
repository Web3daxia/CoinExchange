/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("执行闪兑交易请求")
public class ExecuteExchangeRequest {
    @ApiModelProperty(value = "源币种", required = true, example = "BTC")
    @NotBlank(message = "源币种不能为空")
    private String fromCurrency;

    @ApiModelProperty(value = "目标币种", required = true, example = "USDT")
    @NotBlank(message = "目标币种不能为空")
    private String toCurrency;

    @ApiModelProperty(value = "源币种数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal fromAmount;

    @ApiModelProperty(value = "最大允许滑点（百分比，如0.5表示0.5%）")
    private BigDecimal maxSlippage;
}















