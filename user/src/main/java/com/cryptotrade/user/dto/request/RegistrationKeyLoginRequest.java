/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 注册私钥登录请求DTO
 */
@ApiModel("注册私钥登录请求")
public class RegistrationKeyLoginRequest {
    
    @ApiModelProperty(value = "注册私钥（注册时系统生成的256位字母数字组合）", required = true)
    @NotBlank(message = "注册私钥不能为空")
    private String registrationKey;
    
    // Getters and Setters
    public String getRegistrationKey() {
        return registrationKey;
    }
    
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }
}














