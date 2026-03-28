/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 注册响应DTO
 */
@ApiModel("注册响应")
public class RegisterResponse {
    
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    
    @ApiModelProperty(value = "用户名")
    private String username;
    
    @ApiModelProperty(value = "注册私钥（256位字母数字组合，仅首次返回，请妥善保管）")
    private String registrationKey;
    
    @ApiModelProperty(value = "警告提示")
    private String warning = "注册私钥仅显示一次，请妥善保管，丢失后无法找回";
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRegistrationKey() {
        return registrationKey;
    }
    
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }
    
    public String getWarning() {
        return warning;
    }
    
    public void setWarning(String warning) {
        this.warning = warning;
    }
}














