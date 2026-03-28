/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Map;

@Data
@ApiModel("跟随带单员请求")
public class FollowTraderRequest {
    @ApiModelProperty(value = "带单员ID", required = true)
    @NotNull(message = "带单员ID不能为空")
    private Long traderId;

    @ApiModelProperty(value = "市场类型: SPOT, FUTURES_USDT, FUTURES_COIN", required = true)
    @NotBlank(message = "市场类型不能为空")
    private String marketType;

    @ApiModelProperty(value = "跟单类型: PUBLIC（公域跟单）、PRIVATE（私域跟单）", required = true)
    @NotBlank(message = "跟单类型不能为空")
    private String copyType;

    @ApiModelProperty(value = "分配金额", required = true)
    @NotNull(message = "分配金额不能为空")
    @Positive(message = "分配金额必须大于0")
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "跟单比例（0.1-1.0，1.0表示100%复制）", required = true)
    @NotNull(message = "跟单比例不能为空")
    private BigDecimal copyRatio;

    @ApiModelProperty(value = "邀请码（私域跟单必填）")
    private String inviteCode;

    @ApiModelProperty(value = "跟单设置（Map格式：leverage, marginMode, stopLossPrice, takeProfitPrice等）")
    private Map<String, Object> settings;
}















