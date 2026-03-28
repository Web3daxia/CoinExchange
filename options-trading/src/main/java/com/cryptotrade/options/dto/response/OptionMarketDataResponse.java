/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("期权市场数据响应")
public class OptionMarketDataResponse {
    @ApiModelProperty(value = "期权合约ID")
    private Long contractId;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "期权类型: CALL（看涨）、PUT（看跌）")
    private String optionType;

    @ApiModelProperty(value = "执行价格")
    private BigDecimal strikePrice;

    @ApiModelProperty(value = "到期日期")
    private LocalDateTime expiryDate;

    @ApiModelProperty(value = "当前期权价格（期权费）")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "标的资产当前价格")
    private BigDecimal underlyingPrice;

    @ApiModelProperty(value = "理论价格")
    private BigDecimal theoreticalPrice;

    @ApiModelProperty(value = "隐含波动率")
    private BigDecimal impliedVolatility;

    @ApiModelProperty(value = "24小时交易量")
    private BigDecimal volume24h;

    @ApiModelProperty(value = "持仓量")
    private BigDecimal openInterest;
}















