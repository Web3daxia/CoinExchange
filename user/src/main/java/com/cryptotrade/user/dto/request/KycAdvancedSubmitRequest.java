/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("高级KYC提交请求")
public class KycAdvancedSubmitRequest {
    @ApiModelProperty(value = "手持证件照片", required = true)
    @NotNull(message = "手持证件照片不能为空")
    private MultipartFile handheldImage;

    @ApiModelProperty(value = "验证视频", required = true)
    @NotNull(message = "验证视频不能为空")
    private MultipartFile video;

    @ApiModelProperty(value = "验证语句", required = true, example = "我正在进行身份认证，用户ID为123")
    @NotBlank(message = "验证语句不能为空")
    private String verificationStatement;
}

