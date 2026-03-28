/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("审核带单员申请请求")
public class ReviewTraderApplicationRequest {
    @ApiModelProperty(value = "申请ID", required = true)
    @NotNull(message = "申请ID不能为空")
    private Long applicationId;

    @ApiModelProperty(value = "是否通过", required = true)
    @NotNull(message = "审核结果不能为空")
    private Boolean approved;

    @ApiModelProperty(value = "拒绝原因（拒绝时必填）")
    private String rejectReason;
}















