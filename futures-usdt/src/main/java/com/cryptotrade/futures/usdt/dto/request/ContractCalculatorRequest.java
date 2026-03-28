/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * USDT本位合约计算器请求DTO
 */
@ApiModel("USDT本位合约计算器请求")
public class ContractCalculatorRequest {
    
    @ApiModelProperty(value = "交易对", required = true, example = "BTC/USDT")
    @NotNull(message = "交易对不能为空")
    private String pairName;
    
    @ApiModelProperty(value = "持仓方向：LONG（多仓）, SHORT（空仓）", required = true, example = "LONG")
    @NotNull(message = "持仓方向不能为空")
    private String positionSide; // LONG, SHORT
    
    @ApiModelProperty(value = "开仓均价", required = true, example = "50000.00000000")
    @NotNull(message = "开仓均价不能为空")
    @DecimalMin(value = "0.00000001", message = "开仓均价必须大于0")
    private BigDecimal entryPrice;
    
    @ApiModelProperty(value = "持仓数量（张数）", required = true, example = "10.00000000")
    @NotNull(message = "持仓数量不能为空")
    @DecimalMin(value = "0.00000001", message = "持仓数量必须大于0")
    private BigDecimal quantity;
    
    @ApiModelProperty(value = "杠杆倍数", required = true, example = "10")
    @NotNull(message = "杠杆倍数不能为空")
    @DecimalMin(value = "1", message = "杠杆倍数必须大于等于1")
    private Integer leverage;
    
    @ApiModelProperty(value = "保证金模式：CROSS（全仓）, ISOLATED（逐仓）", required = true, example = "ISOLATED")
    @NotNull(message = "保证金模式不能为空")
    private String marginMode; // CROSS, ISOLATED
    
    @ApiModelProperty(value = "标记价格（可选，不提供则使用当前价格）", example = "51000.00000000")
    private BigDecimal markPrice;
    
    @ApiModelProperty(value = "合约面值（默认1）", example = "1.00000000")
    private BigDecimal faceValue = BigDecimal.ONE;
    
    @ApiModelProperty(value = "合约乘数（默认1）", example = "1.00000000")
    private BigDecimal contractMultiplier = BigDecimal.ONE;
    
    @ApiModelProperty(value = "维持保证金率（默认0.005）", example = "0.00500000")
    private BigDecimal maintenanceMarginRate = new BigDecimal("0.005");
    
    @ApiModelProperty(value = "手续费率（默认0.0005）", example = "0.00050000")
    private BigDecimal feeRate = new BigDecimal("0.0005");
    
    @ApiModelProperty(value = "保证金余额（逐仓模式必填）", example = "5000.00000000")
    private BigDecimal marginBalance;
    
    // Getters and Setters
    public String getPairName() {
        return pairName;
    }
    
    public void setPairName(String pairName) {
        this.pairName = pairName;
    }
    
    public String getPositionSide() {
        return positionSide;
    }
    
    public void setPositionSide(String positionSide) {
        this.positionSide = positionSide;
    }
    
    public BigDecimal getEntryPrice() {
        return entryPrice;
    }
    
    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public Integer getLeverage() {
        return leverage;
    }
    
    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }
    
    public String getMarginMode() {
        return marginMode;
    }
    
    public void setMarginMode(String marginMode) {
        this.marginMode = marginMode;
    }
    
    public BigDecimal getMarkPrice() {
        return markPrice;
    }
    
    public void setMarkPrice(BigDecimal markPrice) {
        this.markPrice = markPrice;
    }
    
    public BigDecimal getFaceValue() {
        return faceValue;
    }
    
    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }
    
    public BigDecimal getContractMultiplier() {
        return contractMultiplier;
    }
    
    public void setContractMultiplier(BigDecimal contractMultiplier) {
        this.contractMultiplier = contractMultiplier;
    }
    
    public BigDecimal getMaintenanceMarginRate() {
        return maintenanceMarginRate;
    }
    
    public void setMaintenanceMarginRate(BigDecimal maintenanceMarginRate) {
        this.maintenanceMarginRate = maintenanceMarginRate;
    }
    
    public BigDecimal getFeeRate() {
        return feeRate;
    }
    
    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }
    
    public BigDecimal getMarginBalance() {
        return marginBalance;
    }
    
    public void setMarginBalance(BigDecimal marginBalance) {
        this.marginBalance = marginBalance;
    }
}














