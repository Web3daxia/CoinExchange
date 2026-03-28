/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service;

import com.cryptotrade.wallet.entity.WalletTransfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 钱包资产划转服务接口
 */
public interface WalletTransferService {
    /**
     * 查询用户所有账户的余额
     * @param userId 用户ID
     * @return 账户余额Map
     */
    Map<String, Object> getAllAccountBalances(Long userId);

    /**
     * 跨账户划转资产
     * @param userId 用户ID
     * @param fromAccountType 源账户类型
     * @param toAccountType 目标账户类型
     * @param currency 币种
     * @param amount 划转金额
     * @param remark 备注
     * @return 划转记录
     */
    WalletTransfer transferBetweenAccounts(Long userId, String fromAccountType, String toAccountType,
                                          String currency, BigDecimal amount, String remark);

    /**
     * 查询资产划转历史
     * @param userId 用户ID
     * @return 划转记录列表
     */
    List<WalletTransfer> getTransferHistory(Long userId);

    /**
     * 确认资金划转操作（需要安全验证）
     * @param userId 用户ID
     * @param transferId 划转ID
     * @param verificationCode 验证码
     */
    void confirmTransfer(Long userId, Long transferId, String verificationCode);
}















