/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("申请成为商家请求")
public class MerchantApplicationRequest {
    @ApiModelProperty(value = "商家名称", required = true)
    @NotBlank(message = "商家名称不能为空")
    private String merchantName;

    @ApiModelProperty(value = "头像URL")
    private String avatar;

    @ApiModelProperty(value = "签名")
    private String signature;

    @ApiModelProperty(value = "个人简介")
    private String bio;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "资产证明（JSON数组）")
    private String assetProof;

    @ApiModelProperty(value = "总资产", required = true)
    @NotNull(message = "总资产不能为空")
    private BigDecimal totalAssets;
}















