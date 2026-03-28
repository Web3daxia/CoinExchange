/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 设置返佣配置请求
 */
@Data
@ApiModel(description = "设置返佣配置请求")
public class SetRebateConfigRequest {
    @ApiModelProperty(value = "现货交易返佣比例", example = "20.00")
    @DecimalMin(value = "0", message = "返佣比例不能小于0")
    @DecimalMax(value = "100", message = "返佣比例不能大于100")
    private BigDecimal spotRebateRate;

    @ApiModelProperty(value = "USDT本位合约返佣比例", example = "25.00")
    @DecimalMin(value = "0", message = "返佣比例不能小于0")
    @DecimalMax(value = "100", message = "返佣比例不能大于100")
    private BigDecimal futuresUsdtRebateRate;

    @ApiModelProperty(value = "币本位合约返佣比例", example = "30.00")
    @DecimalMin(value = "0", message = "返佣比例不能小于0")
    @DecimalMax(value = "100", message = "返佣比例不能大于100")
    private BigDecimal futuresCoinRebateRate;

    @ApiModelProperty(value = "跟单返佣比例", example = "10.00")
    @DecimalMin(value = "0", message = "返佣比例不能小于0")
    @DecimalMax(value = "100", message = "返佣比例不能大于100")
    private BigDecimal copyTradingRebateRate;

    @ApiModelProperty(value = "期权交易返佣比例", example = "15.00")
    @DecimalMin(value = "0", message = "返佣比例不能小于0")
    @DecimalMax(value = "100", message = "返佣比例不能大于100")
    private BigDecimal optionsRebateRate;

    @ApiModelProperty(value = "每日返佣上限", example = "1000.00")
    private BigDecimal dailyRebateLimit;

    @ApiModelProperty(value = "返佣周期", example = "DAILY", allowableValues = "DAILY,WEEKLY,MONTHLY")
    private String rebatePeriod;

    @ApiModelProperty(value = "返佣方式", example = "USDT", allowableValues = "USDT,TOKEN")
    private String rebateMethod;
}















