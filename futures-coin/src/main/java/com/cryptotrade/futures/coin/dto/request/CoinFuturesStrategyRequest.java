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
@ApiModel("币本位永续合约策略配置请求")
public class CoinFuturesStrategyRequest {
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "策略类型: ARBITRAGE, HEDGE, INTER_TEMPORAL_ARBITRAGE", required = true)
    @NotBlank(message = "策略类型不能为空")
    private String strategyType;

    @ApiModelProperty(value = "策略参数（Map格式）")
    private Map<String, Object> strategyParams;

    // 套利策略参数
    @ApiModelProperty(value = "第一个交易对（套利用）", example = "BTC/BTC")
    private String pairName1;

    @ApiModelProperty(value = "第二个交易对（套利用）", example = "ETH/ETH")
    private String pairName2;

    @ApiModelProperty(value = "价差阈值", example = "0.01")
    private BigDecimal priceDifferenceThreshold;

    // 对冲策略参数
    @ApiModelProperty(value = "对冲交易对", example = "BTC/BTC")
    private String hedgePairName;

    @ApiModelProperty(value = "对冲比例", example = "1.0")
    private BigDecimal hedgeRatio;

    // 跨期套利参数
    @ApiModelProperty(value = "现货交易对", example = "BTC/USDT")
    private String spotPairName;

    @ApiModelProperty(value = "期货交易对", example = "BTC/BTC")
    private String futuresPairName;

    @ApiModelProperty(value = "基差阈值", example = "0.02")
    private BigDecimal basisThreshold;

    // 风险控制参数
    @ApiModelProperty(value = "最大亏损")
    private BigDecimal maxLoss;

    @ApiModelProperty(value = "最大持仓")
    private BigDecimal maxPosition;
}















