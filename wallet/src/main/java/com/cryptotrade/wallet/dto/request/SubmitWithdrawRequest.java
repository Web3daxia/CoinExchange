/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("提交提现请求")
public class SubmitWithdrawRequest {
    @ApiModelProperty(value = "币种", required = true, example = "USDT")
    @NotBlank(message = "币种不能为空")
    private String currency;

    @ApiModelProperty(value = "链类型", required = true, example = "TRC20")
    @NotBlank(message = "链类型不能为空")
    private String chain;

    @ApiModelProperty(value = "提现地址", required = true)
    @NotBlank(message = "提现地址不能为空")
    private String address;

    @ApiModelProperty(value = "提现金额", required = true)
    @NotNull(message = "提现金额不能为空")
    @Positive(message = "提现金额必须大于0")
    private BigDecimal amount;

    @ApiModelProperty(value = "地址簿ID（如果从地址簿选择）")
    private Long addressId;
}















