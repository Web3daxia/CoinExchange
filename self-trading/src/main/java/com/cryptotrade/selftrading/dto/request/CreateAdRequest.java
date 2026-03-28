/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("创建广告请求")
public class CreateAdRequest {
    @ApiModelProperty(value = "广告类型: SELL（出售）、BUY（买入）", required = true)
    @NotBlank(message = "广告类型不能为空")
    private String adType;

    @ApiModelProperty(value = "加密货币类型", required = true)
    @NotBlank(message = "加密货币类型不能为空")
    private String cryptoCurrency;

    @ApiModelProperty(value = "法币类型", required = true)
    @NotBlank(message = "法币类型不能为空")
    private String fiatCurrency;

    @ApiModelProperty(value = "价格（法币）", required = true)
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;

    @ApiModelProperty(value = "最小金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大金额", required = true)
    @NotNull(message = "最大金额不能为空")
    @Positive(message = "最大金额必须大于0")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "支持的支付方式", required = true)
    @NotNull(message = "支付方式不能为空")
    private List<String> paymentMethods;

    @ApiModelProperty(value = "其他设置")
    private Map<String, Object> settings;
}















