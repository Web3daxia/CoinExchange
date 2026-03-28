/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 理财产品赎回请求DTO
 */
@Data
public class FinanceRedemptionRequest {
    private Long investmentId; // 投资记录ID
    private BigDecimal redemptionAmount; // 赎回金额（部分赎回时使用，全额赎回为null）
    private String redemptionType; // 赎回类型: FULL, PARTIAL
}














