/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("创建评价请求")
public class CreateRatingRequest {
    @ApiModelProperty(value = "商家ID", required = true)
    @NotNull(message = "商家ID不能为空")
    private Long merchantId;

    @ApiModelProperty(value = "订单ID", required = true)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty(value = "评分（1-5星）", required = true)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于1")
    @Max(value = 5, message = "评分不能大于5")
    private Integer rating;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "是否匿名")
    private Boolean isAnonymous;
}















