/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@ApiModel("更新跟单设置请求")
public class UpdateCopyTradingSettingsRequest {
    @ApiModelProperty(value = "分配金额")
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "跟单比例")
    private BigDecimal copyRatio;

    @ApiModelProperty(value = "其他设置（Map格式）")
    private Map<String, Object> settings;
}















