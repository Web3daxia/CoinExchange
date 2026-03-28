/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

/**
 * 更新会员信息请求DTO
 */
@Data
public class UpdateMemberInfoRequest {
    private String email; // 会员邮箱
    private String phone; // 手机号码
    private String country; // 国家
    private String loginPassword; // 登录密码
    private String assetPassword; // 资产管理密码
}














