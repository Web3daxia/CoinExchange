/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("用户设备响应")
public class UserDeviceResponse {
    @ApiModelProperty(value = "设备ID")
    private Long id;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginAt;

    @ApiModelProperty(value = "是否活跃")
    private Boolean isActive;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
}















