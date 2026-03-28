/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("执行快捷买币请求")
public class ExecuteQuickBuyRequest {
    @ApiModelProperty(value = "加密货币类型", required = true, example = "USDT")
    @NotBlank(message = "加密货币类型不能为空")
    private String cryptoCurrency;

    @ApiModelProperty(value = "法币类型", required = true, example = "USD")
    @NotBlank(message = "法币类型不能为空")
    private String fiatCurrency;

    @ApiModelProperty(value = "支付方式", required = true, example = "PayPal")
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @ApiModelProperty(value = "购买的加密货币数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal cryptoAmount;
}















