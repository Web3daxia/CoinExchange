/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.service.impl;

import com.cryptotrade.wallet.dto.request.AssetTransferRequest;
import com.cryptotrade.wallet.dto.response.AssetTransferResponse;
import com.cryptotrade.wallet.entity.AssetTransferRecord;
import com.cryptotrade.wallet.entity.DeliveryContractAccount;
import com.cryptotrade.wallet.entity.FinanceAccount;
import com.cryptotrade.wallet.repository.AssetTransferRecordRepository;
import com.cryptotrade.wallet.repository.DeliveryContractAccountRepository;
import com.cryptotrade.wallet.repository.FinanceAccountRepository;
import com.cryptotrade.wallet.service.AssetTransferService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 资产划转服务实现
 */
@Service
public class AssetTransferServiceImpl implements AssetTransferService {
    
    @Autowired
    private AssetTransferRecordRepository transferRecordRepository;
    
    @Autowired
    private DeliveryContractAccountRepository deliveryAccountRepository;
    
    @Autowired
    private FinanceAccountRepository financeAccountRepository;
    
    @Autowired
    private WalletService walletService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssetTransferResponse transfer(Long userId, AssetTransferRequest request) {
        String fromAccountType = request.getFromAccountType();
        String toAccountType = request.getToAccountType();
        String currency = request.getCurrency();
        BigDecimal amount = request.getAmount();
        
        // 创建划转记录
        AssetTransferRecord record = new AssetTransferRecord();
        record.setUserId(userId);
        record.setCurrency(currency);
        record.setAmount(amount);
        record.setFromAccountType(fromAccountType);
        record.setToAccountType(toAccountType);
        record.setTransferType("TRANSFER_OUT");
        record.setStatus(0); // 待处理
        record.setRemark(request.getRemark());
        record = transferRecordRepository.save(record);
        
        try {
            // 从转出账户扣减
            deductFromAccount(userId, fromAccountType, currency, amount);
            
            // 添加到转入账户
            addToAccount(userId, toAccountType, currency, amount);
            
            // 更新划转记录状态为成功
            record.setStatus(1);
            record = transferRecordRepository.save(record);
            
            // 构建响应
            AssetTransferResponse response = new AssetTransferResponse();
            response.setTransferId(record.getId());
            response.setFromAccountType(fromAccountType);
            response.setToAccountType(toAccountType);
            response.setCurrency(currency);
            response.setAmount(amount);
            response.setStatus(record.getStatus());
            response.setCreatedAt(record.getCreatedAt());
            
            return response;
            
        } catch (Exception e) {
            // 更新划转记录状态为失败
            record.setStatus(2);
            transferRecordRepository.save(record);
            throw new RuntimeException("资产划转失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从账户扣减资产
     */
    private void deductFromAccount(Long userId, String accountType, String currency, BigDecimal amount) {
        switch (accountType) {
            case "DELIVERY":
                DeliveryContractAccount deliveryAccount = deliveryAccountRepository
                    .findByUserIdAndCurrency(userId, currency)
                    .orElseThrow(() -> new RuntimeException("交割合约账户不存在"));
                if (deliveryAccount.getAvailableBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("交割合约账户余额不足");
                }
                deliveryAccount.setAvailableBalance(deliveryAccount.getAvailableBalance().subtract(amount));
                deliveryAccount.setTotalBalance(deliveryAccount.getTotalBalance().subtract(amount));
                deliveryAccountRepository.save(deliveryAccount);
                break;
            case "FINANCE":
                FinanceAccount financeAccount = financeAccountRepository
                    .findByUserIdAndCurrency(userId, currency)
                    .orElseThrow(() -> new RuntimeException("理财账户不存在"));
                if (financeAccount.getAvailableBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("理财账户余额不足");
                }
                financeAccount.setAvailableBalance(financeAccount.getAvailableBalance().subtract(amount));
                financeAccount.setTotalBalance(financeAccount.getTotalBalance().subtract(amount));
                financeAccountRepository.save(financeAccount);
                break;
            case "SPOT":
            case "FUTURES_USDT":
            case "FUTURES_COIN":
            case "OPTIONS":
            case "COPY_TRADING":
                // 使用 WalletService 扣减钱包余额
                BigDecimal availableBalance = walletService.getAvailableBalance(userId, accountType, currency);
                if (availableBalance.compareTo(amount) < 0) {
                    throw new RuntimeException(accountType + "账户余额不足，可用余额: " + availableBalance);
                }
                walletService.deductBalance(userId, accountType, currency, amount);
                break;
            default:
                throw new RuntimeException("不支持的账户类型: " + accountType);
        }
    }
    
    /**
     * 添加到账户
     */
    private void addToAccount(Long userId, String accountType, String currency, BigDecimal amount) {
        switch (accountType) {
            case "DELIVERY":
                DeliveryContractAccount deliveryAccount = deliveryAccountRepository
                    .findByUserIdAndCurrency(userId, currency)
                    .orElseGet(() -> {
                        DeliveryContractAccount newAccount = new DeliveryContractAccount();
                        newAccount.setUserId(userId);
                        newAccount.setCurrency(currency);
                        newAccount.setAvailableBalance(BigDecimal.ZERO);
                        newAccount.setFrozenBalance(BigDecimal.ZERO);
                        newAccount.setTotalBalance(BigDecimal.ZERO);
                        return newAccount;
                    });
                deliveryAccount.setAvailableBalance(deliveryAccount.getAvailableBalance().add(amount));
                deliveryAccount.setTotalBalance(deliveryAccount.getTotalBalance().add(amount));
                deliveryAccountRepository.save(deliveryAccount);
                break;
            case "FINANCE":
                FinanceAccount financeAccount = financeAccountRepository
                    .findByUserIdAndCurrency(userId, currency)
                    .orElseGet(() -> {
                        FinanceAccount newAccount = new FinanceAccount();
                        newAccount.setUserId(userId);
                        newAccount.setCurrency(currency);
                        newAccount.setAvailableBalance(BigDecimal.ZERO);
                        newAccount.setFrozenBalance(BigDecimal.ZERO);
                        newAccount.setInvestingBalance(BigDecimal.ZERO);
                        newAccount.setTotalBalance(BigDecimal.ZERO);
                        newAccount.setTotalProfit(BigDecimal.ZERO);
                        return newAccount;
                    });
                financeAccount.setAvailableBalance(financeAccount.getAvailableBalance().add(amount));
                financeAccount.setTotalBalance(financeAccount.getTotalBalance().add(amount));
                financeAccountRepository.save(financeAccount);
                break;
            case "SPOT":
            case "FUTURES_USDT":
            case "FUTURES_COIN":
            case "OPTIONS":
            case "COPY_TRADING":
                // 使用 WalletService 增加钱包余额
                walletService.addBalance(userId, accountType, currency, amount);
                break;
            default:
                throw new RuntimeException("不支持的账户类型: " + accountType);
        }
    }
    
    @Override
    public Page<AssetTransferRecord> getTransferHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transferRecordRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    public AssetTransferRecord getTransferDetail(Long transferId) {
        return transferRecordRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("划转记录不存在"));
    }
}













