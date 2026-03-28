/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 还款请求DTO
 */
@Data
public class RepaymentRequest {
    private Long orderId; // 订单ID
    private String repaymentType; // 还款类型: FULL, PARTIAL, INTEREST
    private BigDecimal amount; // 还款金额（部分还款时使用）
}














