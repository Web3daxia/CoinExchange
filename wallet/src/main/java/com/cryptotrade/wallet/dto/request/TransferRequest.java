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
@ApiModel("资产划转请求")
public class TransferRequest {
    @ApiModelProperty(value = "源账户类型: SPOT, LEVERAGE, FUTURES_USDT, FUTURES_COIN, OPTIONS, COPY_TRADING", required = true)
    @NotBlank(message = "源账户类型不能为空")
    private String fromAccountType;

    @ApiModelProperty(value = "目标账户类型: SPOT, LEVERAGE, FUTURES_USDT, FUTURES_COIN, OPTIONS, COPY_TRADING", required = true)
    @NotBlank(message = "目标账户类型不能为空")
    private String toAccountType;

    @ApiModelProperty(value = "币种", required = true)
    @NotBlank(message = "币种不能为空")
    private String currency;

    @ApiModelProperty(value = "划转金额", required = true)
    @NotNull(message = "划转金额不能为空")
    @Positive(message = "划转金额必须大于0")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注")
    private String remark;
}















