/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("高级订单请求")
public class AdvancedOrderRequest {
    @ApiModelProperty(value = "订单类型: ADVANCED_LIMIT, TIME_WEIGHTED, RECURRING, TRAILING, ICEBERG", required = true)
    @NotBlank(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "交易对名称", required = true)
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @ApiModelProperty(value = "方向: BUY, SELL", required = true)
    @NotBlank(message = "方向不能为空")
    private String side;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    // 高级限价单参数
    @ApiModelProperty(value = "有效时间类型: GTC, IOC, FOK")
    private String timeInForce;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;

    // 分时委托参数
    @ApiModelProperty(value = "时间间隔（秒）")
    private Integer timeInterval;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    // 循环委托参数
    @ApiModelProperty(value = "循环周期: DAILY, WEEKLY, MONTHLY")
    private String recurringPeriod;

    @ApiModelProperty(value = "循环金额")
    private BigDecimal recurringAmount;

    // 追踪委托参数
    @ApiModelProperty(value = "追踪距离（百分比）")
    private BigDecimal trailingDistance;

    @ApiModelProperty(value = "止损价格")
    private BigDecimal stopLossPrice;

    @ApiModelProperty(value = "止盈价格")
    private BigDecimal takeProfitPrice;

    // 冰山策略参数
    @ApiModelProperty(value = "总数量")
    private BigDecimal icebergTotalQuantity;

    @ApiModelProperty(value = "每次显示数量")
    private BigDecimal icebergDisplayQuantity;
}















