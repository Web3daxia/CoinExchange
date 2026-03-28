/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("登录请求")
public class LoginRequest {
    @ApiModelProperty(value = "登录类型: PASSWORD, PRIVATE_KEY, GOOGLE, FACEBOOK, TWITTER, TELEGRAM", 
                     required = true, example = "PASSWORD")
    @NotBlank(message = "登录类型不能为空")
    private String loginType;

    @ApiModelProperty(value = "手机号或邮箱", example = "13800138000")
    private String account;

    @ApiModelProperty(value = "密码", example = "Password123!")
    private String password;

    @ApiModelProperty(value = "私钥")
    private String privateKey;

    @ApiModelProperty(value = "OAuth token（第三方登录时使用）")
    private String oauthToken;

    @ApiModelProperty(value = "二次验证码")
    private String twoFactorCode;
}















