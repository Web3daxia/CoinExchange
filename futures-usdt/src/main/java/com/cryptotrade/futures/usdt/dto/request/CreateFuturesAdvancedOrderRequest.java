/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("创建永续合约高级订单请求")
public class CreateFuturesAdvancedOrderRequest {
    @ApiModelProperty(value = "订单类型: ADVANCED_LIMIT, TRAILING, TRAILING_LIMIT, ICEBERG, SEGMENTED, TIME_WEIGHTED", required = true)
    @NotBlank(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "交易对名称", required = true, example = "BTC/USDT")
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

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

    @ApiModelProperty(value = "价格（限价单必填）")
    private BigDecimal price;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    // 高级限价单参数
    @ApiModelProperty(value = "有效时间类型: GTC, IOC, FOK")
    private String timeInForce;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "价格范围最小值")
    private BigDecimal priceRangeMin;

    @ApiModelProperty(value = "价格范围最大值")
    private BigDecimal priceRangeMax;

    // 追踪委托参数
    @ApiModelProperty(value = "追踪距离（百分比）")
    private BigDecimal trailingDistance;

    // 追逐限价单参数
    @ApiModelProperty(value = "追逐限价价格")
    private BigDecimal trailingLimitPrice;

    // 冰山策略参数
    @ApiModelProperty(value = "冰山总数量")
    private BigDecimal icebergTotalQuantity;

    @ApiModelProperty(value = "冰山每次显示数量")
    private BigDecimal icebergDisplayQuantity;

    // 分段委托参数
    @ApiModelProperty(value = "分段总数量")
    private BigDecimal segmentedTotalQuantity;

    @ApiModelProperty(value = "分段数量")
    private Integer segmentedCount;

    // 分时委托参数
    @ApiModelProperty(value = "时间间隔（秒）")
    private Integer timeInterval;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
}
