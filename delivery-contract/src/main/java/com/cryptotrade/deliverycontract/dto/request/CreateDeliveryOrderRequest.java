/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建交割合约订单请求DTO
 */
@Data
@ApiModel("创建交割合约订单请求")
public class CreateDeliveryOrderRequest {
    @ApiModelProperty(value = "合约ID", required = true)
    @NotNull(message = "合约ID不能为空")
    private Long contractId;

    @ApiModelProperty(value = "订单类型: OPEN（开仓）、CLOSE（平仓）、STOP_LOSS（止损）、TAKE_PROFIT（止盈）", required = true)
    @NotNull(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY（买入/做多）、SELL（卖出/做空）", required = true)
    @NotNull(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "价格类型: LIMIT（限价）、MARKET（市价）", required = true)
    @NotNull(message = "价格类型不能为空")
    private String priceType;

    @ApiModelProperty(value = "限价价格（限价单必填）")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;

    @ApiModelProperty(value = "杠杆倍数", required = true)
    @NotNull(message = "杠杆倍数不能为空")
    private BigDecimal leverage;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;
}













