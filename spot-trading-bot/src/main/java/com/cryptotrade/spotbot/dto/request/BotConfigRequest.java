/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 机器人配置请求DTO
 */
@Data
public class BotConfigRequest {
    @NotBlank(message = "交易对名称不能为空")
    private String pairName;

    @NotBlank(message = "基础货币不能为空")
    private String baseCurrency;

    @NotBlank(message = "计价货币不能为空")
    private String quoteCurrency;

    @NotNull(message = "下单时间间隔不能为空")
    @Min(value = 1, message = "下单时间间隔至少为1秒")
    private Integer orderIntervalSeconds;

    @NotNull(message = "初始订单数量不能为空")
    @DecimalMin(value = "0.00000001", message = "初始订单数量必须大于0")
    private BigDecimal initialOrderQuantity;

    @NotNull(message = "价格精度不能为空")
    @Min(value = 0, message = "价格精度不能小于0")
    private Integer pricePrecision;

    @NotNull(message = "数量精度不能为空")
    @Min(value = 0, message = "数量精度不能小于0")
    private Integer quantityPrecision;

    @NotBlank(message = "差价类型不能为空")
    @Pattern(regexp = "RATIO|VALUE", message = "差价类型必须是RATIO或VALUE")
    private String priceDiffType; // RATIO（比例）, VALUE（值）

    @NotNull(message = "买卖盘最高差价不能为空")
    @DecimalMin(value = "0", message = "买卖盘最高差价不能小于0")
    private BigDecimal maxPriceDiff;

    private BigDecimal priceChangeStepPercent; // 价格变化步涨%

    @NotNull(message = "最低交易量不能为空")
    @DecimalMin(value = "0.00000001", message = "最低交易量必须大于0")
    private BigDecimal minTradeQuantity;

    // 交易量随机因子（7个因子）
    private BigDecimal volumeRandomFactor1;
    private BigDecimal volumeRandomFactor2;
    private BigDecimal volumeRandomFactor3;
    private BigDecimal volumeRandomFactor4;
    private BigDecimal volumeRandomFactor5;
    private BigDecimal volumeRandomFactor6;
    private BigDecimal volumeRandomFactor7;

    private BigDecimal currentPrice;
}














