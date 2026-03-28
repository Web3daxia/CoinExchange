/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 市场深度请求DTO（增强版 - 支持买卖盘切换和价格精度）
 */
@Data
@ApiModel(value = "市场深度请求", description = "用于获取市场买卖盘口数据的请求体")
public class MarketDepthRequest {
    
    @ApiModelProperty(value = "交易对", required = true, example = "BTC/USDT")
    @NotBlank(message = "交易对不能为空")
    private String pair;

    @ApiModelProperty(value = "深度级别", example = "20", notes = "返回的买卖盘口数量，例如20表示返回20档买盘和20档卖盘")
    private Integer limit = 20;

    @ApiModelProperty(value = "盘口类型", example = "BOTH", allowableValues = "BUY,SELL,BOTH", 
                     notes = "BOTH: 买卖盘都显示, BUY: 只显示买盘, SELL: 只显示卖盘")
    @NotNull(message = "盘口类型不能为空")
    private String depthType = "BOTH";

    @ApiModelProperty(value = "价格精度", example = "0.01", 
                     notes = "价格精度设置，例如0.01表示保留两位小数，1表示保留整数。如果BTC现价87500.556895，精度0.01返回87500.55，精度1返回87500")
    private String pricePrecision;
}
