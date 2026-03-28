/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录响应")
public class LoginResponse {
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "令牌类型")
    private String tokenType = "Bearer";

    @ApiModelProperty(value = "过期时间（秒）")
    private Long expiresIn;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "是否需要绑定邮箱或手机")
    private Boolean needBindContact;
}















