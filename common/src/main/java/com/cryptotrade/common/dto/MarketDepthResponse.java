/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 市场深度响应DTO（增强版 - 支持买卖盘切换和价格精度）
 */
@Data
@ApiModel(value = "市场深度响应", description = "市场买卖盘口数据")
public class MarketDepthResponse {
    
    @ApiModelProperty(value = "交易对", example = "BTC/USDT")
    private String pair;
    
    @ApiModelProperty(value = "价格精度", example = "0.01")
    private String pricePrecision;
    
    @ApiModelProperty(value = "当前价格（已应用精度）", example = "87500.55")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "买盘数据")
    private List<DepthItem> bids;

    @ApiModelProperty(value = "卖盘数据")
    private List<DepthItem> asks;

    /**
     * 深度项（价格、数量、累计数量）
     */
    @Data
    @ApiModel(value = "深度项", description = "买卖盘口数据项")
    public static class DepthItem {
        @ApiModelProperty(value = "价格（已应用精度）", example = "87500.50")
        private BigDecimal price;

        @ApiModelProperty(value = "数量", example = "1.5")
        private BigDecimal quantity;

        @ApiModelProperty(value = "累计数量", example = "10.5", notes = "从当前项到第一项的累计数量")
        private BigDecimal cumulative;
    }
}
