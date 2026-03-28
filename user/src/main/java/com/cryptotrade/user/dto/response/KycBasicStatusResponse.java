/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("基础KYC状态响应")
public class KycBasicStatusResponse {
    @ApiModelProperty(value = "KYC ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "证件类型")
    private String idType;

    @ApiModelProperty(value = "证件号码（脱敏）")
    private String idNumber;

    @ApiModelProperty(value = "状态: PENDING, PROCESSING, APPROVED, REJECTED")
    private String status;

    @ApiModelProperty(value = "审核类型: AUTO, MANUAL")
    private String reviewType;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime reviewedAt;
}















