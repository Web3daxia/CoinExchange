/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("创建杠杆订单请求")
public class CreateLeveragedOrderRequest {
    @ApiModelProperty(value = "交易对名称", required = true)
    @NotNull(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "订单类型: MARKET, LIMIT, STOP_LOSS, TAKE_PROFIT, STOP_LIMIT, CONDITIONAL", required = true)
    @NotNull(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY, SELL", required = true)
    @NotNull(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "操作: OPEN, CLOSE", required = true)
    @NotNull(message = "操作不能为空")
    private String action;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    @ApiModelProperty(value = "价格（限价单、止损限价单）")
    private BigDecimal price;

    @ApiModelProperty(value = "止损价格（止损单、止损限价单）")
    private BigDecimal stopPrice;

    @ApiModelProperty(value = "止盈价格（止盈单）")
    private BigDecimal takeProfitPrice;

    @ApiModelProperty(value = "触发价格（条件单）")
    private BigDecimal triggerPrice;

    @ApiModelProperty(value = "条件类型（条件单）: ABOVE, BELOW")
    private String conditionType;

    @ApiModelProperty(value = "杠杆倍数", required = true)
    @NotNull(message = "杠杆倍数不能为空")
    @Positive(message = "杠杆倍数必须大于0")
    private Integer leverage;
}















