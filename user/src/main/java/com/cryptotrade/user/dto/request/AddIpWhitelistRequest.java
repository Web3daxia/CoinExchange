/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("添加IP白名单请求")
public class AddIpWhitelistRequest {
    @ApiModelProperty(value = "IP地址或IP段（CIDR格式）", required = true, example = "192.168.1.100 或 192.168.1.0/24")
    @NotBlank(message = "IP地址不能为空")
    private String ipAddress;

    @ApiModelProperty(value = "描述", example = "家庭网络")
    private String description;
}















