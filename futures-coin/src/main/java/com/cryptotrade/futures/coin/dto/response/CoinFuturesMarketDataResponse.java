/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("币本位永续合约市场数据响应")
public class CoinFuturesMarketDataResponse {
    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "合约类型")
    private String contractType;

    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "指数价格")
    private BigDecimal indexPrice;

    @ApiModelProperty(value = "标记价格")
    private BigDecimal markPrice;

    @ApiModelProperty(value = "资金费率")
    private BigDecimal fundingRate;

    @ApiModelProperty(value = "下次资金费率结算时间")
    private LocalDateTime nextFundingTime;

    @ApiModelProperty(value = "24小时涨跌幅")
    private BigDecimal priceChange24h;

    @ApiModelProperty(value = "24小时成交量")
    private BigDecimal volume24h;

    @ApiModelProperty(value = "24小时成交额")
    private BigDecimal amount24h;

    @ApiModelProperty(value = "24小时最高价")
    private BigDecimal high24h;

    @ApiModelProperty(value = "24小时最低价")
    private BigDecimal low24h;

    @ApiModelProperty(value = "买一价")
    private BigDecimal bidPrice;

    @ApiModelProperty(value = "卖一价")
    private BigDecimal askPrice;

    @ApiModelProperty(value = "买盘深度")
    private List<Map<String, Object>> bids;

    @ApiModelProperty(value = "卖盘深度")
    private List<Map<String, Object>> asks;

    @ApiModelProperty(value = "最大杠杆")
    private Integer maxLeverage;

    @ApiModelProperty(value = "最小杠杆")
    private Integer minLeverage;
}
