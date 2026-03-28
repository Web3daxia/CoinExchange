/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("币本位永续合约策略响应")
public class CoinFuturesStrategyResponse {
    @ApiModelProperty(value = "策略ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "策略类型")
    private String strategyType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "总盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "总亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "最后执行时间")
    private LocalDateTime lastExecutionTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
}















