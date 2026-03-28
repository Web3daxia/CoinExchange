/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员资产操作请求DTO
 */
@Data
public class MemberAssetOperationRequest {
    private String currency; // 币种
    private BigDecimal amount; // 操作金额
    private String operationType; // 操作类型: DEPOSIT（充币）, FREEZE（冻结）, UNFREEZE（解冻）
    private String remark; // 备注
}














