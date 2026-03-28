/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.service;

import com.cryptotrade.experiencefund.entity.ContractExperienceAccount;

import java.math.BigDecimal;

/**
 * 合约体验金账户Service接口
 */
public interface ContractExperienceAccountService {
    
    /**
     * 创建体验金账户
     */
    ContractExperienceAccount createAccount(Long userId, Long activityId);
    
    /**
     * 根据用户ID获取账户
     */
    ContractExperienceAccount getAccountByUserId(Long userId);
    
    /**
     * 根据账户编号获取账户
     */
    ContractExperienceAccount getAccountByAccountNo(String accountNo);
    
    /**
     * 更新账户余额
     */
    void updateAccountBalance(Long accountId, BigDecimal changeAmount, String changeType, Long tradeId);
    
    /**
     * 检查账户是否可以交易
     */
    boolean canTrade(Long accountId);
    
    /**
     * 增加交易次数
     */
    void incrementTradeCount(Long accountId);
    
    /**
     * 处理账户过期
     */
    void expireAccount(Long accountId);
}














