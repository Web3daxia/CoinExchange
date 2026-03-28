/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建合约网格机器人请求DTO（USDT本位和币本位通用）
 */
@Data
@ApiModel("创建合约网格机器人请求")
public class CreateFuturesGridRobotRequest {
    @ApiModelProperty(value = "机器人名称")
    private String robotName;

    @ApiModelProperty(value = "交易对名称", required = true)
    @NotNull(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "网格数量", required = true)
    @NotNull(message = "网格数量不能为空")
    private Integer gridCount;

    @ApiModelProperty(value = "卖出区间上限价格", required = true)
    @NotNull(message = "上边界价格不能为空")
    private BigDecimal upperPrice;

    @ApiModelProperty(value = "买入区间下限价格", required = true)
    @NotNull(message = "下边界价格不能为空")
    private BigDecimal lowerPrice;

    @ApiModelProperty(value = "起始价格", required = true)
    @NotNull(message = "起始价格不能为空")
    private BigDecimal startPrice;

    @ApiModelProperty(value = "初始资金", required = true)
    @NotNull(message = "初始资金不能为空")
    private BigDecimal initialCapital;

    @ApiModelProperty(value = "投资比例（0-1，默认0.5）")
    private BigDecimal investmentRatio;

    @ApiModelProperty(value = "杠杆倍数（合约市场必填）", required = true)
    @NotNull(message = "杠杆倍数不能为空")
    private Integer leverage;

    @ApiModelProperty(value = "保证金模式: ISOLATED（逐仓）, CROSS（全仓）", required = true)
    @NotNull(message = "保证金模式不能为空")
    private String marginMode;

    @ApiModelProperty(value = "止损价格（绝对值）")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格（绝对值）")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "止损百分比（相对起始价格）")
    private BigDecimal stopLossPercentage;

    @ApiModelProperty(value = "止盈百分比（相对起始价格）")
    private BigDecimal takeProfitPercentage;
}













