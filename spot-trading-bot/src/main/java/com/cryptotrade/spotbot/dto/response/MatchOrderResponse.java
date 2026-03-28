/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单撮合响应DTO
 */
@Data
public class MatchOrderResponse {
    private Boolean success; // 是否成功
    private String botOrderId; // 机器人订单ID
    private String userOrderId; // 用户订单ID
    private BigDecimal matchedPrice; // 成交价格
    private BigDecimal matchedQuantity; // 成交数量
    private BigDecimal matchedAmount; // 成交金额
    private LocalDateTime matchedTime; // 成交时间
    private String message; // 消息
}














