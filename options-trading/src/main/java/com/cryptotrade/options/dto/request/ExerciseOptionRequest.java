/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@ApiModel("行使期权请求")
public class ExerciseOptionRequest {
    @ApiModelProperty(value = "持仓ID", required = true)
    @NotNull(message = "持仓ID不能为空")
    private Long positionId;

    @ApiModelProperty(value = "行使数量", required = true)
    @NotNull(message = "行使数量不能为空")
    @Positive(message = "行使数量必须大于0")
    private BigDecimal quantity;
}















