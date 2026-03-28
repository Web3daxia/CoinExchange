/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("OAuth绑定响应")
public class OAuthBindingResponse {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "OAuth提供商")
    private String provider;

    @ApiModelProperty(value = "OAuth用户名")
    private String oauthUsername;

    @ApiModelProperty(value = "OAuth邮箱")
    private String oauthEmail;

    @ApiModelProperty(value = "OAuth头像")
    private String oauthAvatar;

    @ApiModelProperty(value = "是否活跃")
    private Boolean isActive;

    @ApiModelProperty(value = "绑定时间")
    private LocalDateTime createdAt;
}















