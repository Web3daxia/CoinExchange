/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网格机器人响应DTO
 */
@Data
@ApiModel("网格机器人响应")
public class GridRobotResponse {
    @ApiModelProperty(value = "机器人ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "机器人名称")
    private String robotName;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "市场类型")
    private String marketType;

    @ApiModelProperty(value = "策略类型")
    private String strategyType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "网格数量")
    private Integer gridCount;

    @ApiModelProperty(value = "上边界价格")
    private BigDecimal upperPrice;

    @ApiModelProperty(value = "下边界价格")
    private BigDecimal lowerPrice;

    @ApiModelProperty(value = "起始价格")
    private BigDecimal startPrice;

    @ApiModelProperty(value = "初始资金")
    private BigDecimal initialCapital;

    @ApiModelProperty(value = "当前资金")
    private BigDecimal currentCapital;

    @ApiModelProperty(value = "投资比例")
    private BigDecimal investmentRatio;

    @ApiModelProperty(value = "杠杆倍数")
    private Integer leverage;

    @ApiModelProperty(value = "保证金模式")
    private String marginMode;

    @ApiModelProperty(value = "总盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "总亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "净盈利")
    private BigDecimal netProfit;

    @ApiModelProperty(value = "总交易次数")
    private Integer totalTrades;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;
}













