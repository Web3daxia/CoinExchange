/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("绑定OAuth请求")
public class BindOAuthRequest {
    @ApiModelProperty(value = "OAuth provider: GOOGLE, FACEBOOK, TWITTER, TELEGRAM", required = true, example = "GOOGLE")
    @NotBlank(message = "OAuth provider不能为空")
    private String provider;

    @ApiModelProperty(value = "OAuth token", required = true)
    @NotBlank(message = "OAuth token不能为空")
    private String oauthToken;
}















