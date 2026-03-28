/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@ApiModel("更新个人资料请求")
public class UpdateProfileRequest {
    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;

    @ApiModelProperty(value = "头像图片")
    private MultipartFile avatar;

    @ApiModelProperty(value = "性别: MALE, FEMALE, OTHER", example = "MALE")
    private String gender;

    @ApiModelProperty(value = "生日", example = "1990-01-01T00:00:00")
    private LocalDateTime birthday;
}















