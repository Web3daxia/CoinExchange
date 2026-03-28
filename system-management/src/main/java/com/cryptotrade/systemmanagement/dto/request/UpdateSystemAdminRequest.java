/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * 更新系统管理员请求
 */
@Data
@ApiModel(description = "更新系统管理员请求")
public class UpdateSystemAdminRequest {
    
    @ApiModelProperty(value = "角色ID", example = "1")
    private Long roleId;
    
    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;
    
    @ApiModelProperty(value = "手机号码", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    @ApiModelProperty(value = "邮箱号码", example = "admin@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @ApiModelProperty(value = "安全码（用于二次验证）", example = "123456")
    private String securityCode;
    
    @ApiModelProperty(value = "是否启用谷歌验证码", example = "true")
    private Boolean enableGoogleAuth;
    
    @ApiModelProperty(value = "状态: ACTIVE, INACTIVE, SUSPENDED", example = "ACTIVE")
    private String status;
}












