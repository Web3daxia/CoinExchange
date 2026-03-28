/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("提现验证请求")
public class VerifyWithdrawRequest {
    @ApiModelProperty(value = "提现ID", required = true)
    @NotNull(message = "提现ID不能为空")
    private Long withdrawId;

    @ApiModelProperty(value = "邮箱验证码")
    private String emailCode;

    @ApiModelProperty(value = "手机验证码")
    private String phoneCode;

    @ApiModelProperty(value = "谷歌验证码")
    private String googleCode;
}















