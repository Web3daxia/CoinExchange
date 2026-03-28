/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("创建期权订单请求")
public class CreateOptionOrderRequest {
    @ApiModelProperty(value = "期权合约ID", required = true)
    @NotNull(message = "期权合约ID不能为空")
    private Long contractId;

    @ApiModelProperty(value = "订单类型: OPEN（开仓）、CLOSE（平仓）", required = true)
    @NotNull(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY（买入）、SELL（卖出）", required = true)
    @NotNull(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    @ApiModelProperty(value = "价格（期权费）", required = true)
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;
}















