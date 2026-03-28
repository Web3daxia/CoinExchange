/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产划转响应DTO
 */
@ApiModel("资产划转响应")
public class AssetTransferResponse {
    
    @ApiModelProperty(value = "划转记录ID")
    private Long transferId;
    
    @ApiModelProperty(value = "转出账户类型")
    private String fromAccountType;
    
    @ApiModelProperty(value = "转入账户类型")
    private String toAccountType;
    
    @ApiModelProperty(value = "币种")
    private String currency;
    
    @ApiModelProperty(value = "划转金额")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "状态：0-待处理，1-成功，2-失败")
    private Integer status;
    
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;
    
    // Getters and Setters
    public Long getTransferId() {
        return transferId;
    }
    
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
    
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
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}














