/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统管理员登录响应
 */
@Data
@ApiModel(description = "系统管理员登录响应")
public class SystemAdminLoginResponse {
    
    @ApiModelProperty(value = "管理员信息")
    private SystemAdminResponse admin;
    
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;
    
    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;
    
    @ApiModelProperty(value = "令牌过期时间（秒）")
    private Long expiresIn;
}












