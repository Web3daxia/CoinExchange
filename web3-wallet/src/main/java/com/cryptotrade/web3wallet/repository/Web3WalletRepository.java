/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.repository;

import com.cryptotrade.web3wallet.entity.Web3Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Web3钱包Repository
 */
@Repository
public interface Web3WalletRepository extends JpaRepository<Web3Wallet, Long> {
    /**
     * 根据用户ID查询钱包
     */
    List<Web3Wallet> findByUserId(Long userId);

    /**
     * 根据钱包地址查询
     */
    Optional<Web3Wallet> findByWalletAddress(String walletAddress);

    /**
     * 查询用户的默认钱包
     */
    Optional<Web3Wallet> findByUserIdAndIsDefault(Long userId, Boolean isDefault);
}















