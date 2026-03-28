/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("OAuth登录请求")
public class OAuthLoginRequest {
    @ApiModelProperty(value = "OAuth token", required = true)
    @NotBlank(message = "OAuth token不能为空")
    private String oauthToken;

    @ApiModelProperty(value = "OAuth provider", example = "GOOGLE")
    private String provider;
}

