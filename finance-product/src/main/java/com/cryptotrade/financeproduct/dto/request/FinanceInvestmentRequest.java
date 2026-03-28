/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户投资理财产品请求DTO
 */
@Data
public class FinanceInvestmentRequest {
    private Long productId; // 理财产品ID
    private BigDecimal investmentAmount; // 投资金额
    private String currency; // 投资币种
}














