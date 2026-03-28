/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("币本位永续合约账户风险响应")
public class CoinFuturesAccountRiskResponse {
    @ApiModelProperty(value = "账户总资产（基础资产，如BTC）")
    private BigDecimal totalAssets;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;

    @ApiModelProperty(value = "已用保证金")
    private BigDecimal usedMargin;

    @ApiModelProperty(value = "未实现盈亏")
    private BigDecimal unrealizedPnl;

    @ApiModelProperty(value = "账户权益 = 总资产 + 未实现盈亏")
    private BigDecimal accountEquity;

    @ApiModelProperty(value = "维持保证金")
    private BigDecimal maintenanceMargin;

    @ApiModelProperty(value = "保证金率 = 账户权益 / 持仓价值")
    private BigDecimal marginRatio;

    @ApiModelProperty(value = "风险等级: LOW, MEDIUM, HIGH, CRITICAL")
    private String riskLevel;

    @ApiModelProperty(value = "高风险仓位列表")
    private List<CoinFuturesPositionResponse> highRiskPositions;

    @ApiModelProperty(value = "是否需要追加保证金")
    private Boolean needMarginCall;

    @ApiModelProperty(value = "是否接近强平")
    private Boolean nearLiquidation;
}















