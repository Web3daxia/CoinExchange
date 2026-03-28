/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("创建订单请求")
public class CreateOrderRequest {
    @ApiModelProperty(value = "交易对名称", required = true, example = "BTC/USDT")
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT", required = true, example = "LIMIT")
    @NotBlank(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY, SELL", required = true, example = "BUY")
    @NotBlank(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "价格（限价单必填）", example = "50000.00")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", required = true, example = "0.001")
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    @ApiModelProperty(value = "止损价格（止损单/止盈单必填）", example = "49000.00")
    private BigDecimal stopPrice;
}















