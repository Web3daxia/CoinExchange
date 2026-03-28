/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新网格机器人参数请求DTO
 */
@Data
@ApiModel("更新网格机器人参数请求")
public class UpdateGridRobotRequest {
    @ApiModelProperty(value = "机器人名称")
    private String robotName;

    @ApiModelProperty(value = "网格数量")
    private Integer gridCount;

    @ApiModelProperty(value = "卖出区间上限价格")
    private BigDecimal upperPrice;

    @ApiModelProperty(value = "买入区间下限价格")
    private BigDecimal lowerPrice;

    @ApiModelProperty(value = "起始价格")
    private BigDecimal startPrice;

    @ApiModelProperty(value = "初始资金")
    private BigDecimal initialCapital;

    @ApiModelProperty(value = "投资比例")
    private BigDecimal investmentRatio;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;
}













