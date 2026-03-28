/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

/**
 * 锁定/解锁资产请求DTO
 */
@Data
public class LockAssetRequest {
    private String currency; // 币种
    private Boolean isLocked; // 是否锁定
    private String remark; // 备注
}














