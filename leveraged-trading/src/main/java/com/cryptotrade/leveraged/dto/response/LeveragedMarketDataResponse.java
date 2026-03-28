/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("杠杆市场数据响应")
public class LeveragedMarketDataResponse {
    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "24小时价格变化")
    private BigDecimal priceChange24h;

    @ApiModelProperty(value = "24小时交易量")
    private BigDecimal volume24h;

    @ApiModelProperty(value = "24小时最高价")
    private BigDecimal high24h;

    @ApiModelProperty(value = "24小时最低价")
    private BigDecimal low24h;

    @ApiModelProperty(value = "可用杠杆倍数")
    private java.util.List<Integer> availableLeverage;
}















