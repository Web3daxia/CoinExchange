/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("私钥注册请求")
public class PrivateKeyRegisterRequest {
    @ApiModelProperty(value = "私钥", required = true, example = "0x...")
    @NotBlank(message = "私钥不能为空")
    private String privateKey;
}















