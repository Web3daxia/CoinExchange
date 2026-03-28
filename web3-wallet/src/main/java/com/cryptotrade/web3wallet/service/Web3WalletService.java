/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.service;

import com.cryptotrade.web3wallet.entity.Web3Wallet;
import com.cryptotrade.web3wallet.entity.Web3TokenBalance;
import com.cryptotrade.web3wallet.entity.Web3Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Web3钱包服务接口
 */
public interface Web3WalletService {
    /**
     * 创建钱包
     */
    Web3Wallet createWallet(Long userId, String chainType, String walletName);

    /**
     * 导入钱包
     */
    Web3Wallet importWallet(Long userId, String chainType, String privateKeyOrMnemonic, String walletName);

    /**
     * 获取用户钱包列表
     */
    List<Web3Wallet> getUserWallets(Long userId);

    /**
     * 获取钱包余额
     */
    List<Web3TokenBalance> getWalletBalances(Long walletId);

    /**
     * 同步钱包余额
     */
    void syncWalletBalance(Long walletId);

    /**
     * 发送交易
     */
    Web3Transaction sendTransaction(Long walletId, String toAddress, String tokenContractAddress, BigDecimal amount);

    /**
     * 获取交易记录
     */
    List<Web3Transaction> getTransactions(Long walletId, String txStatus);

    /**
     * 查询交易状态
     */
    Web3Transaction getTransactionByHash(String txHash);
}















