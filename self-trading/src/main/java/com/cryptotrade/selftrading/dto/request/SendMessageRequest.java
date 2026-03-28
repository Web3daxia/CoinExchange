/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("发送消息请求")
public class SendMessageRequest {
    @ApiModelProperty(value = "订单ID", required = true)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty(value = "接收者ID", required = true)
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;

    @ApiModelProperty(value = "消息类型: TEXT, IMAGE, VIDEO, FILE, EMOJI", required = true)
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "文件URL（图片、视频、文件）")
    private String fileUrl;
}















