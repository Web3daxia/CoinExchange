/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("期权定价响应")
public class OptionPricingResponse {
    @ApiModelProperty(value = "合约ID")
    private Long contractId;

    @ApiModelProperty(value = "市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "理论价格")
    private BigDecimal theoreticalPrice;

    @ApiModelProperty(value = "标的资产价格")
    private BigDecimal underlyingPrice;

    @ApiModelProperty(value = "执行价格")
    private BigDecimal strikePrice;

    @ApiModelProperty(value = "到期时间（年）")
    private Double timeToExpiry;

    @ApiModelProperty(value = "隐含波动率")
    private BigDecimal impliedVolatility;

    @ApiModelProperty(value = "内在价值")
    private BigDecimal intrinsicValue;

    @ApiModelProperty(value = "时间价值")
    private BigDecimal timeValue;
}















