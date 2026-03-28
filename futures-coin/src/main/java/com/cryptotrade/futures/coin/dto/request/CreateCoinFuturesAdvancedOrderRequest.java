/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@ApiModel("创建币本位永续合约高级订单请求")
public class CreateCoinFuturesAdvancedOrderRequest {
    @ApiModelProperty(value = "交易对名称", required = true, example = "BTC/BTC")
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "订单类型: ADVANCED_LIMIT, TRAILING, TRAILING_LIMIT, ICEBERG, SEGMENTED, TIME_WEIGHTED", required = true)
    @NotBlank(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "方向: BUY, SELL", required = true)
    @NotBlank(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "仓位方向: LONG, SHORT", required = true)
    @NotBlank(message = "仓位方向不能为空")
    private String positionSide;

    @ApiModelProperty(value = "保证金模式: CROSS, ISOLATED, SEGMENTED, COMBINED", required = true)
    @NotBlank(message = "保证金模式不能为空")
    private String marginMode;

    @ApiModelProperty(value = "杠杆倍数", required = true, example = "10")
    @NotNull(message = "杠杆倍数不能为空")
    @Positive(message = "杠杆倍数必须大于0")
    private Integer leverage;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    @ApiModelProperty(value = "价格（限价单必填）")
    private BigDecimal price;

    @ApiModelProperty(value = "高级订单参数（根据订单类型不同而不同）")
    private Map<String, Object> params;
}















