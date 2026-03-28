/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("杠杆仓位响应")
public class LeveragedPositionResponse {
    @ApiModelProperty(value = "仓位ID")
    private Long id;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "方向: LONG, SHORT")
    private String side;

    @ApiModelProperty(value = "持仓数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "开仓价格")
    private BigDecimal entryPrice;

    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "杠杆倍数")
    private Integer leverage;

    @ApiModelProperty(value = "保证金")
    private BigDecimal margin;

    @ApiModelProperty(value = "未实现盈亏")
    private BigDecimal unrealizedPnl;

    @ApiModelProperty(value = "已实现盈亏")
    private BigDecimal realizedPnl;

    @ApiModelProperty(value = "强平价格")
    private BigDecimal liquidationPrice;

    @ApiModelProperty(value = "保证金率")
    private BigDecimal marginRatio;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
}















