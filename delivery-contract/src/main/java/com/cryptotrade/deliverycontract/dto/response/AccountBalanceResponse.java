/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户余额响应DTO
 */
@Data
@ApiModel("账户余额响应")
public class AccountBalanceResponse {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "余额列表")
    private List<BalanceItem> balances;

    @ApiModelProperty(value = "总权益")
    private BigDecimal totalEquity;

    @ApiModelProperty(value = "总保证金")
    private BigDecimal totalMargin;

    @ApiModelProperty(value = "总未实现盈亏")
    private BigDecimal totalUnrealizedPnl;

    @Data
    @ApiModel("余额项")
    public static class BalanceItem {
        @ApiModelProperty(value = "币种")
        private String currency;

        @ApiModelProperty(value = "可用余额")
        private BigDecimal available;

        @ApiModelProperty(value = "冻结余额")
        private BigDecimal frozen;

        @ApiModelProperty(value = "总余额")
        private BigDecimal total;

        @ApiModelProperty(value = "未实现盈亏")
        private BigDecimal unrealizedPnl;
    }
}













