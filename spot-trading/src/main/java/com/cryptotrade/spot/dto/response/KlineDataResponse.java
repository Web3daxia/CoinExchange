/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("K线数据响应")
public class KlineDataResponse {
    @ApiModelProperty(value = "K线数据列表")
    private List<KlineItem> klines;

    @Data
    @ApiModel("K线项")
    public static class KlineItem {
        @ApiModelProperty(value = "时间")
        private LocalDateTime time;

        @ApiModelProperty(value = "开盘价")
        private BigDecimal open;

        @ApiModelProperty(value = "最高价")
        private BigDecimal high;

        @ApiModelProperty(value = "最低价")
        private BigDecimal low;

        @ApiModelProperty(value = "收盘价")
        private BigDecimal close;

        @ApiModelProperty(value = "成交量")
        private BigDecimal volume;
    }
}















