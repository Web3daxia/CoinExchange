/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 资产划转请求DTO
 */
@ApiModel("资产划转请求")
public class AssetTransferRequest {
    
    @ApiModelProperty(value = "转出账户类型", required = true, example = "SPOT")
    @NotBlank(message = "转出账户类型不能为空")
    private String fromAccountType;
    
    @ApiModelProperty(value = "转入账户类型", required = true, example = "FUTURES_USDT")
    @NotBlank(message = "转入账户类型不能为空")
    private String toAccountType;
    
    @ApiModelProperty(value = "币种", required = true, example = "USDT")
    @NotBlank(message = "币种不能为空")
    private String currency;
    
    @ApiModelProperty(value = "划转金额", required = true, example = "1000.00000000")
    @NotNull(message = "划转金额不能为空")
    @DecimalMin(value = "0.00000001", message = "划转金额必须大于0")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "备注", example = "划转到合约账户")
    private String remark;
    
    // Getters and Setters
    public String getFromAccountType() {
        return fromAccountType;
    }
    
    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }
    
    public String getToAccountType() {
        return toAccountType;
    }
    
    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}














