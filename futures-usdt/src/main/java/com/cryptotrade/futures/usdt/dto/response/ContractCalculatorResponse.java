/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * USDT本位合约计算器响应DTO
 */
@ApiModel("USDT本位合约计算器响应")
public class ContractCalculatorResponse {
    
    @ApiModelProperty(value = "初始保证金")
    private BigDecimal initialMargin;
    
    @ApiModelProperty(value = "维持保证金")
    private BigDecimal maintenanceMargin;
    
    @ApiModelProperty(value = "未实现盈亏")
    private BigDecimal unrealizedPnl;
    
    @ApiModelProperty(value = "收益率")
    private BigDecimal profitRate;
    
    @ApiModelProperty(value = "保证金率")
    private BigDecimal marginRate;
    
    @ApiModelProperty(value = "强平价格")
    private BigDecimal liquidationPrice;
    
    @ApiModelProperty(value = "平仓价格（盈亏为0的价格）")
    private BigDecimal breakEvenPrice;
    
    @ApiModelProperty(value = "当前标记价格")
    private BigDecimal markPrice;
    
    // Getters and Setters
    public BigDecimal getInitialMargin() {
        return initialMargin;
    }
    
    public void setInitialMargin(BigDecimal initialMargin) {
        this.initialMargin = initialMargin;
    }
    
    public BigDecimal getMaintenanceMargin() {
        return maintenanceMargin;
    }
    
    public void setMaintenanceMargin(BigDecimal maintenanceMargin) {
        this.maintenanceMargin = maintenanceMargin;
    }
    
    public BigDecimal getUnrealizedPnl() {
        return unrealizedPnl;
    }
    
    public void setUnrealizedPnl(BigDecimal unrealizedPnl) {
        this.unrealizedPnl = unrealizedPnl;
    }
    
    public BigDecimal getProfitRate() {
        return profitRate;
    }
    
    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }
    
    public BigDecimal getMarginRate() {
        return marginRate;
    }
    
    public void setMarginRate(BigDecimal marginRate) {
        this.marginRate = marginRate;
    }
    
    public BigDecimal getLiquidationPrice() {
        return liquidationPrice;
    }
    
    public void setLiquidationPrice(BigDecimal liquidationPrice) {
        this.liquidationPrice = liquidationPrice;
    }
    
    public BigDecimal getBreakEvenPrice() {
        return breakEvenPrice;
    }
    
    public void setBreakEvenPrice(BigDecimal breakEvenPrice) {
        this.breakEvenPrice = breakEvenPrice;
    }
    
    public BigDecimal getMarkPrice() {
        return markPrice;
    }
    
    public void setMarkPrice(BigDecimal markPrice) {
        this.markPrice = markPrice;
    }
}














