/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 重置账户请求DTO
 */
@Data
public class ResetAccountRequest {
    private BigDecimal newBalance; // 新的余额（如果不提供，则重置为初始余额）
}














