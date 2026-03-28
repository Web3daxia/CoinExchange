/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 转账请求DTO
 */
@Data
@ApiModel("转账请求")
public class TransferRequest {
    @ApiModelProperty(value = "币种，如: USDT, BTC", required = true)
    @NotNull(message = "币种不能为空")
    private String currency;

    @ApiModelProperty(value = "转账金额", required = true)
    @NotNull(message = "转账金额不能为空")
    private BigDecimal amount;
}













