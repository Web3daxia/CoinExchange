/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 补仓请求DTO
 */
@Data
public class TopupRequest {
    private Long orderId; // 订单ID
    private BigDecimal topupAmount; // 补仓数量
}














