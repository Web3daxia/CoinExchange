/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.repository;

import com.cryptotrade.web3wallet.entity.Web3TokenBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Web3代币余额Repository
 */
@Repository
public interface Web3TokenBalanceRepository extends JpaRepository<Web3TokenBalance, Long> {
    /**
     * 根据钱包ID查询余额
     */
    List<Web3TokenBalance> findByWalletId(Long walletId);

    /**
     * 根据钱包ID和代币符号查询
     */
    Optional<Web3TokenBalance> findByWalletIdAndTokenSymbol(Long walletId, String tokenSymbol);
}















