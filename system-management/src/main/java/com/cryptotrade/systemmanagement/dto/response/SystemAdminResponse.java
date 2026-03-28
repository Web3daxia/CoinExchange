/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统管理员响应
 */
@Data
@ApiModel(description = "系统管理员响应")
public class SystemAdminResponse {
    
    @ApiModelProperty(value = "ID")
    private Long id;
    
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    
    @ApiModelProperty(value = "头像URL")
    private String avatar;
    
    @ApiModelProperty(value = "用户名")
    private String username;
    
    @ApiModelProperty(value = "手机号码")
    private String phone;
    
    @ApiModelProperty(value = "邮箱号码")
    private String email;
    
    @ApiModelProperty(value = "是否启用谷歌验证码")
    private Boolean enableGoogleAuth;
    
    @ApiModelProperty(value = "状态")
    private String status;
    
    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;
    
    @ApiModelProperty(value = "最后登录设备")
    private String lastLoginDevice;
    
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createdAt;
    
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;
}












