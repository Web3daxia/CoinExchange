/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

@Data
@ApiModel("申请成为带单员请求")
public class TraderApplicationRequest {
    @ApiModelProperty(value = "交易员类型: SPOT（现货交易员）、FUTURES（合约交易员）、BOTH（两者都是）", required = true)
    @NotBlank(message = "交易员类型不能为空")
    private String traderType;

    @ApiModelProperty(value = "联系信息（Map格式：Facebook, Twitter, Telegram, WeChat, QQ, Phone等）")
    private Map<String, String> contactInfo;

    @ApiModelProperty(value = "资产证明（文件路径或URL）")
    private String assetProof;

    @ApiModelProperty(value = "总资产", required = true)
    @NotNull(message = "总资产不能为空")
    private BigDecimal totalAssets;
}















