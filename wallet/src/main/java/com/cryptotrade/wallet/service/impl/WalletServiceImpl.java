/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service.impl;

import com.cryptotrade.wallet.entity.Wallet;
import com.cryptotrade.wallet.repository.WalletRepository;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    @Transactional
    public void freezeBalance(Long userId, String accountType, String currency, BigDecimal amount) {
        Wallet wallet = getOrCreateWallet(userId, accountType, currency);
        
        BigDecimal availableBalance = wallet.getBalance().subtract(wallet.getFrozenBalance());
        if (availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，可用余额: " + availableBalance);
        }

        wallet.setFrozenBalance(wallet.getFrozenBalance().add(amount));
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void unfreezeBalance(Long userId, String accountType, String currency, BigDecimal amount) {
        Wallet wallet = getOrCreateWallet(userId, accountType, currency);
        
        if (wallet.getFrozenBalance().compareTo(amount) < 0) {
            throw new RuntimeException("冻结余额不足");
        }

        wallet.setFrozenBalance(wallet.getFrozenBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void deductBalance(Long userId, String accountType, String currency, BigDecimal amount) {
        Wallet wallet = getOrCreateWallet(userId, accountType, currency);
        
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void addBalance(Long userId, String accountType, String currency, BigDecimal amount) {
        Wallet wallet = getOrCreateWallet(userId, accountType, currency);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    @Override
    public BigDecimal getAvailableBalance(Long userId, String accountType, String currency) {
        Wallet wallet = getOrCreateWallet(userId, accountType, currency);
        return wallet.getBalance().subtract(wallet.getFrozenBalance());
    }

    @Override
    public boolean checkBalance(Long userId, String accountType, String currency, BigDecimal amount) {
        BigDecimal availableBalance = getAvailableBalance(userId, accountType, currency);
        return availableBalance.compareTo(amount) >= 0;
    }

    private Wallet getOrCreateWallet(Long userId, String accountType, String currency) {
        Optional<Wallet> walletOpt = walletRepository.findByUserIdAndAccountTypeAndCurrency(
                userId, accountType, currency);
        
        if (walletOpt.isPresent()) {
            return walletOpt.get();
        }

        // 创建新钱包
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setAccountType(accountType);
        wallet.setCurrency(currency);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setFrozenBalance(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }
}















