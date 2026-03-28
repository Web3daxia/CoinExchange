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
 * 创建回测任务请求DTO
 */
@Data
@ApiModel("创建回测任务请求")
public class CreateBacktestRequest {
    @ApiModelProperty(value = "机器人ID")
    private Long robotId;

    @ApiModelProperty(value = "交易对名称", required = true)
    @NotNull(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "市场类型: SPOT, FUTURES_USDT, FUTURES_COIN", required = true)
    @NotNull(message = "市场类型不能为空")
    private String marketType;

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

    @ApiModelProperty(value = "回测开始时间（格式: yyyy-MM-dd HH:mm:ss）", required = true)
    @NotNull(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty(value = "回测结束时间（格式: yyyy-MM-dd HH:mm:ss）", required = true)
    @NotNull(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "投资比例")
    private BigDecimal investmentRatio;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "杠杆倍数（合约市场）")
    private Integer leverage;

    @ApiModelProperty(value = "保证金模式（合约市场）")
    private String marginMode;
}













