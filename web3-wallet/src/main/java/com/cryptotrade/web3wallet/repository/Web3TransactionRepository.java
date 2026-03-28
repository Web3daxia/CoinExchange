/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.repository;

import com.cryptotrade.web3wallet.entity.Web3Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Web3交易Repository
 */
@Repository
public interface Web3TransactionRepository extends JpaRepository<Web3Transaction, Long> {
    /**
     * 根据钱包ID查询交易
     */
    List<Web3Transaction> findByWalletId(Long walletId);

    /**
     * 根据交易哈希查询
     */
    Optional<Web3Transaction> findByTxHash(String txHash);

    /**
     * 根据状态查询交易
     */
    List<Web3Transaction> findByWalletIdAndTxStatus(Long walletId, String txStatus);
}















