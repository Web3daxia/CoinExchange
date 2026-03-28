/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@ApiModel("带单员表现数据响应")
public class TraderPerformanceResponse {
    @ApiModelProperty(value = "带单员ID")
    private Long traderId;

    @ApiModelProperty(value = "周期类型")
    private String periodType;

    @ApiModelProperty(value = "周期开始时间")
    private LocalDateTime periodStart;

    @ApiModelProperty(value = "周期结束时间")
    private LocalDateTime periodEnd;

    @ApiModelProperty(value = "总盈利")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "总亏损")
    private BigDecimal totalLoss;

    @ApiModelProperty(value = "净利润")
    private BigDecimal netProfit;

    @ApiModelProperty(value = "收益率")
    private BigDecimal returnRate;

    @ApiModelProperty(value = "胜率")
    private BigDecimal winRate;

    @ApiModelProperty(value = "总交易笔数")
    private Integer totalTrades;

    @ApiModelProperty(value = "盈利笔数")
    private Integer winningTrades;

    @ApiModelProperty(value = "亏损笔数")
    private Integer losingTrades;

    @ApiModelProperty(value = "平均盈利")
    private BigDecimal avgProfit;

    @ApiModelProperty(value = "平均亏损")
    private BigDecimal avgLoss;

    @ApiModelProperty(value = "盈亏比")
    private BigDecimal profitLossRatio;

    @ApiModelProperty(value = "夏普比率")
    private BigDecimal sharpeRatio;

    @ApiModelProperty(value = "最大回撤")
    private BigDecimal maxDrawdown;

    @ApiModelProperty(value = "总资产管理规模")
    private BigDecimal totalAum;

    @ApiModelProperty(value = "总跟单人数")
    private Integer totalFollowers;

    @ApiModelProperty(value = "日均交易频次")
    private BigDecimal dailyAvgTrades;
}















