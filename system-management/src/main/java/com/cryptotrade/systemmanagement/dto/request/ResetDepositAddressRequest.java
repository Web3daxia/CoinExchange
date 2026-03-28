/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

/**
 * 重置充值地址请求DTO
 */
@Data
public class ResetDepositAddressRequest {
    private String currency; // 币种
    private String depositAddress; // 充值地址
    private String depositAddressTag; // 充值地址标签（如Memo）
}














