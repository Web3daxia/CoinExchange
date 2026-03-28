/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码请求
 */
@Data
@ApiModel(description = "修改密码请求")
public class ChangePasswordRequest {
    
    @ApiModelProperty(value = "旧密码", required = true, example = "OldPassword123!")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    @ApiModelProperty(value = "新密码", required = true, example = "NewPassword123!")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "密码必须包含大小写字母、数字和特殊字符，长度至少8位")
    private String newPassword;
}












