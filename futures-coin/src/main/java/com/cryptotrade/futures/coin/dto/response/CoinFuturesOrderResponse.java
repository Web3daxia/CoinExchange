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
@ApiModel("币本位永续合约订单响应")
public class CoinFuturesOrderResponse {
    @ApiModelProperty(value = "订单ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "合约类型")
    private String contractType;

    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY, SELL")
    private String side;

    @ApiModelProperty(value = "仓位方向: LONG, SHORT")
    private String positionSide;

    @ApiModelProperty(value = "保证金模式")
    private String marginMode;

    @ApiModelProperty(value = "杠杆倍数")
    private Integer leverage;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "已成交数量")
    private BigDecimal filledQuantity;

    @ApiModelProperty(value = "平均成交价格")
    private BigDecimal avgPrice;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "手续费")
    private BigDecimal fee;

    @ApiModelProperty(value = "保证金（基础资产，如BTC）")
    private BigDecimal margin;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;
}















