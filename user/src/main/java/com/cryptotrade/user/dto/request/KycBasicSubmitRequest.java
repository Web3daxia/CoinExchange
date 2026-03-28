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
@ApiModel("基础KYC提交请求")
public class KycBasicSubmitRequest {
    @ApiModelProperty(value = "真实姓名", required = true, example = "张三")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "证件类型: ID_CARD, PASSPORT, DRIVER_LICENSE", required = true, example = "ID_CARD")
    @NotBlank(message = "证件类型不能为空")
    private String idType;

    @ApiModelProperty(value = "证件号码", required = true, example = "110101199001011234")
    @NotBlank(message = "证件号码不能为空")
    private String idNumber;

    @ApiModelProperty(value = "证件正面照片", required = true)
    @NotNull(message = "证件正面照片不能为空")
    private MultipartFile frontImage;

    @ApiModelProperty(value = "证件反面照片", required = true)
    @NotNull(message = "证件反面照片不能为空")
    private MultipartFile backImage;
}

