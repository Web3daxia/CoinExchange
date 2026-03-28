/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 质押借币订单请求DTO
 */
@Data
public class PledgeLoanOrderRequest {
    private String pledgeCurrency; // 质押币种
    private BigDecimal pledgeAmount; // 质押数量
    private String loanCurrency; // 借款币种
    private BigDecimal loanAmount; // 借款数量
    private Integer loanTermDays; // 借款期限（天）
}














