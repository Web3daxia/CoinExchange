/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("带单员响应")
public class TraderResponse {
    @ApiModelProperty(value = "带单员ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "交易员类型")
    private String traderType;

    @ApiModelProperty(value = "等级")
    private String level;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否开启公域跟单")
    private Boolean publicEnabled;

    @ApiModelProperty(value = "是否开启私域跟单")
    private Boolean privateEnabled;

    @ApiModelProperty(value = "订阅费")
    private BigDecimal subscriptionFee;

    @ApiModelProperty(value = "分润比例")
    private BigDecimal profitShareRate;

    @ApiModelProperty(value = "总跟单人数")
    private Integer totalFollowers;

    @ApiModelProperty(value = "总资产管理规模")
    private BigDecimal totalAum;

    @ApiModelProperty(value = "总盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "总亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "胜率")
    private BigDecimal winRate;

    @ApiModelProperty(value = "夏普比率")
    private BigDecimal sharpeRatio;

    @ApiModelProperty(value = "最大回撤")
    private BigDecimal maxDrawdown;

    @ApiModelProperty(value = "最近强制平仓时间")
    private LocalDateTime lastLiquidationTime;
}















