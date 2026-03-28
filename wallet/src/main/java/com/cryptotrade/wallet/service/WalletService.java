/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import java.math.BigDecimal;

public interface WalletService {
    /**
     * 冻结余额
     */
    void freezeBalance(Long userId, String accountType, String currency, BigDecimal amount);

    /**
     * 解冻余额
     */
    void unfreezeBalance(Long userId, String accountType, String currency, BigDecimal amount);

    /**
     * 扣除余额
     */
    void deductBalance(Long userId, String accountType, String currency, BigDecimal amount);

    /**
     * 增加余额
     */
    void addBalance(Long userId, String accountType, String currency, BigDecimal amount);

    /**
     * 获取可用余额
     */
    BigDecimal getAvailableBalance(Long userId, String accountType, String currency);

    /**
     * 检查余额是否充足
     */
    boolean checkBalance(Long userId, String accountType, String currency, BigDecimal amount);
}















