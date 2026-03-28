/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.service;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccount;

import java.math.BigDecimal;

/**
 * 模拟交易账户Service接口
 */
public interface SimulatedTradingAccountService {
    
    /**
     * 创建或获取模拟账户
     */
    SimulatedTradingAccount createOrGetAccount(Long userId);
    
    /**
     * 根据用户ID获取账户
     */
    SimulatedTradingAccount getAccountByUserId(Long userId);
    
    /**
     * 根据账户编号获取账户
     */
    SimulatedTradingAccount getAccountByAccountNo(String accountNo);
    
    /**
     * 重置账户余额
     */
    SimulatedTradingAccount resetAccount(Long userId, BigDecimal newBalance);
    
    /**
     * 更新账户余额
     */
    void updateAccountBalance(Long accountId, BigDecimal changeAmount, String changeType, Long tradeId);
    
    /**
     * 更新账户统计信息
     */
    void updateAccountStatistics(Long accountId, boolean isWin, BigDecimal profitLoss);
}














