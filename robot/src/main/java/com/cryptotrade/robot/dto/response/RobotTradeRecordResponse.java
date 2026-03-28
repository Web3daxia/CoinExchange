/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("交易机器人交易记录响应")
public class RobotTradeRecordResponse {
    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "机器人ID")
    private Long robotId;

    @ApiModelProperty(value = "市场类型")
    private String marketType;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "交易对名称")
    private String pairName;

    @ApiModelProperty(value = "操作: OPEN, CLOSE")
    private String action;

    @ApiModelProperty(value = "方向: BUY, SELL")
    private String side;

    @ApiModelProperty(value = "交易数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "交易价格")
    private BigDecimal price;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal fee;

    @ApiModelProperty(value = "盈亏")
    private BigDecimal profitLoss;

    @ApiModelProperty(value = "策略类型")
    private String strategyType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
}















