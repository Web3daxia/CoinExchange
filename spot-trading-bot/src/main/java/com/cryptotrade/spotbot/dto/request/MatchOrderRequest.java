/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 订单撮合请求DTO
 */
@Data
public class MatchOrderRequest {
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @NotBlank(message = "订单方向不能为空")
    @Pattern(regexp = "BUY|SELL", message = "订单方向必须是BUY或SELL")
    private String side; // BUY, SELL

    @NotBlank(message = "订单类型不能为空")
    @Pattern(regexp = "MARKET|LIMIT", message = "订单类型必须是MARKET或LIMIT")
    private String orderType; // MARKET, LIMIT

    private BigDecimal price; // 限价单价格

    @NotNull(message = "订单数量不能为空")
    @DecimalMin(value = "0.00000001", message = "订单数量必须大于0")
    private BigDecimal quantity; // 订单数量

    private Long userId; // 用户ID

    private String userOrderId; // 用户订单ID
}














