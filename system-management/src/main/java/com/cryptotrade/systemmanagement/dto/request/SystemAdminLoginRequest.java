/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 系统管理员登录请求
 */
@Data
@ApiModel(description = "系统管理员登录请求")
public class SystemAdminLoginRequest {
    
    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @ApiModelProperty(value = "密码", required = true, example = "Admin123!")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @ApiModelProperty(value = "谷歌验证码（如果启用了谷歌验证）", example = "123456")
    private String googleAuthCode;
    
    @ApiModelProperty(value = "安全码（如果需要）", example = "123456")
    private String securityCode;
    
    @ApiModelProperty(value = "登录IP", example = "192.168.1.100")
    private String loginIp;
    
    @ApiModelProperty(value = "登录设备", example = "Chrome/Windows")
    private String loginDevice;
}












