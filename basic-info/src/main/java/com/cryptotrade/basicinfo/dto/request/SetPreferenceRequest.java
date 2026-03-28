/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设置用户偏好请求")
public class SetPreferenceRequest {
    @ApiModelProperty(value = "语言代码")
    private String languageCode;

    @ApiModelProperty(value = "法币，如 USD, EUR, CNY")
    private String currency;

    @ApiModelProperty(value = "API端点ID")
    private Long apiEndpointId;

    @ApiModelProperty(value = "时区")
    private String timezone;

    @ApiModelProperty(value = "日期格式")
    private String dateFormat;

    @ApiModelProperty(value = "数字格式")
    private String numberFormat;
}















