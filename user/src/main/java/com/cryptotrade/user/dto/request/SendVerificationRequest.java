/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("发送验证码请求")
public class SendVerificationRequest {
    @ApiModelProperty(value = "验证类型: PHONE, EMAIL", required = true, example = "PHONE")
    @NotBlank(message = "验证类型不能为空")
    private String type;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "国家代码", example = "+86")
    private String countryCode;

    @ApiModelProperty(value = "邮箱", example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
}















