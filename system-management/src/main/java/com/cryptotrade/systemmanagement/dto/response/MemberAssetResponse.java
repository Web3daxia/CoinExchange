/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员资产响应DTO
 */
@Data
public class MemberAssetResponse {
    private Long id;
    private Long userId;
    private String currency; // 币种名称
    private BigDecimal availableBalance; // 可用余额
    private BigDecimal frozenBalance; // 冻结余额
    private BigDecimal pendingReleaseBalance; // 待释放资产
    private Boolean isLocked; // 是否锁定
    private String depositAddress; // 充值地址
    private String depositAddressTag; // 充值地址标签
}














