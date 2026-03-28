/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产划转记录实体
 */
@Entity
@Table(name = "asset_transfer_record")
public class AssetTransferRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "currency", nullable = false, length = 20)
    private String currency;
    
    @Column(name = "amount", nullable = false, precision = 30, scale = 8)
    private BigDecimal amount;
    
    @Column(name = "from_account_type", nullable = false, length = 50)
    private String fromAccountType; // SPOT, FUTURES_USDT, FUTURES_COIN, DELIVERY, FINANCE, OPTIONS, COPY_TRADING
    
    @Column(name = "to_account_type", nullable = false, length = 50)
    private String toAccountType;
    
    @Column(name = "transfer_type", nullable = false, length = 50)
    private String transferType; // TRANSFER_IN, TRANSFER_OUT
    
    @Column(name = "status", nullable = false)
    private Integer status = 0; // 0-待处理，1-成功，2-失败
    
    @Column(name = "remark", length = 500)
    private String remark;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public String getTransferType() {
        return transferType;
    }
    
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}














