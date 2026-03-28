/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("市场异动提醒请求")
public class MarketAlertRequest {
    @ApiModelProperty(value = "交易对名称", required = true)
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "提醒类型: PRICE_CHANGE, VOLUME_CHANGE, LARGE_ORDER", required = true)
    @NotBlank(message = "提醒类型不能为空")
    private String alertType;

    @ApiModelProperty(value = "条件类型: ABOVE, BELOW, PERCENTAGE_CHANGE", required = true)
    @NotBlank(message = "条件类型不能为空")
    private String conditionType;

    @ApiModelProperty(value = "阈值", required = true)
    @NotNull(message = "阈值不能为空")
    private BigDecimal thresholdValue;
}















