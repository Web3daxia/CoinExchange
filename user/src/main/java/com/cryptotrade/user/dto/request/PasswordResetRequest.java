/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel("密码重置请求")
public class PasswordResetRequest {
    @ApiModelProperty(value = "重置类型: EMAIL, PHONE", required = true, example = "EMAIL")
    @NotBlank(message = "重置类型不能为空")
    private String resetType;

    @ApiModelProperty(value = "邮箱", example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "国家代码", example = "+86")
    private String countryCode;

    @ApiModelProperty(value = "验证码", required = true, example = "123456")
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码长度为6位")
    private String verificationCode;

    @ApiModelProperty(value = "新密码", required = true, example = "NewPassword123!")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8-20位之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "密码必须包含大小写字母、数字和特殊字符")
    private String newPassword;
}















