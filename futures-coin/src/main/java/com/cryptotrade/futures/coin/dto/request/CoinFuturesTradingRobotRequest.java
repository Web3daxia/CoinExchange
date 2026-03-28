/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Map;

@Data
@ApiModel("币本位永续合约交易机器人配置请求")
public class CoinFuturesTradingRobotRequest {
    @ApiModelProperty(value = "机器人名称")
    private String robotName;

    @ApiModelProperty(value = "交易对名称", required = true, example = "BTC/BTC")
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "策略类型: GRID, TREND_FOLLOWING, REVERSE", required = true)
    @NotBlank(message = "策略类型不能为空")
    private String strategyType;

    @ApiModelProperty(value = "策略参数（Map格式）")
    private Map<String, Object> strategyParams;

    @ApiModelProperty(value = "最大亏损")
    private BigDecimal maxLoss;

    @ApiModelProperty(value = "最大持仓")
    private BigDecimal maxPosition;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单数量")
    private BigDecimal orderQuantity;

    @ApiModelProperty(value = "杠杆倍数", example = "10")
    private Integer leverage;

    @ApiModelProperty(value = "保证金模式: CROSS, ISOLATED", example = "CROSS")
    private String marginMode;

    @ApiModelProperty(value = "仓位方向: LONG, SHORT", example = "LONG")
    private String positionSide;
}















