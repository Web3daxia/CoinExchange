/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("登录历史响应")
public class LoginHistoryResponse {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "登录IP")
    private String loginIp;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "登录位置")
    private String loginLocation;

    @ApiModelProperty(value = "登录状态")
    private String loginStatus;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginAt;
}















