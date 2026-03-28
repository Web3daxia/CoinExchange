/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略响应DTO
 */
@Data
@ApiModel("策略响应")
public class StrategyResponse {
    @ApiModelProperty(value = "策略ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "市场类型")
    private String marketType;

    @ApiModelProperty(value = "策略类型")
    private String strategyType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "初始资金")
    private BigDecimal initialCapital;

    @ApiModelProperty(value = "当前资金")
    private BigDecimal currentCapital;

    @ApiModelProperty(value = "总盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "总亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "净盈利")
    private BigDecimal netProfit;

    @ApiModelProperty(value = "收益率")
    private BigDecimal profitRate;

    @ApiModelProperty(value = "总交易次数")
    private Integer totalTrades;

    @ApiModelProperty(value = "盈利交易次数")
    private Integer winningTrades;

    @ApiModelProperty(value = "亏损交易次数")
    private Integer losingTrades;

    @ApiModelProperty(value = "胜率")
    private BigDecimal winRate;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "最后执行时间")
    private LocalDateTime lastExecutionTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;
}













