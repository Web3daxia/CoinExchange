/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理财账户实体
 */
@Entity
@Table(name = "finance_account", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "currency"}))
public class FinanceAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "currency", nullable = false, length = 20)
    private String currency;
    
    @Column(name = "available_balance", nullable = false, precision = 30, scale = 8)
    private BigDecimal availableBalance = BigDecimal.ZERO;
    
    @Column(name = "frozen_balance", nullable = false, precision = 30, scale = 8)
    private BigDecimal frozenBalance = BigDecimal.ZERO;
    
    @Column(name = "investing_balance", nullable = false, precision = 30, scale = 8)
    private BigDecimal investingBalance = BigDecimal.ZERO;
    
    @Column(name = "total_balance", nullable = false, precision = 30, scale = 8)
    private BigDecimal totalBalance = BigDecimal.ZERO;
    
    @Column(name = "total_profit", nullable = false, precision = 30, scale = 8)
    private BigDecimal totalProfit = BigDecimal.ZERO;
    
    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;
    
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
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }
    
    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }
    
    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }
    
    public BigDecimal getInvestingBalance() {
        return investingBalance;
    }
    
    public void setInvestingBalance(BigDecimal investingBalance) {
        this.investingBalance = investingBalance;
    }
    
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }
    
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
    
    public BigDecimal getTotalProfit() {
        return totalProfit;
    }
    
    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
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














