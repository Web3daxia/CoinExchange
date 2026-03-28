/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedFundTransferRecord;
import com.cryptotrade.leveraged.repository.LeveragedFundTransferRecordRepository;
import com.cryptotrade.leveraged.service.LeveragedFundTransferService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 杠杆资金转账服务实现类
 */
@Service
public class LeveragedFundTransferServiceImpl implements LeveragedFundTransferService {

    @Autowired
    private LeveragedFundTransferRecordRepository transferRecordRepository;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public LeveragedFundTransferRecord transferIn(Long userId, String pairName, String currency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("转账金额必须大于0");
        }

        // 检查现货账户余额是否充足
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, "SPOT", currency);
        if (availableBalance == null || availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("现货账户余额不足，可用余额: " + (availableBalance != null ? availableBalance : BigDecimal.ZERO));
        }

        // 生成转账ID
        String transferId = "LT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建转账记录（初始状态为 PENDING）
        LeveragedFundTransferRecord record = new LeveragedFundTransferRecord();
        record.setTransferId(transferId);
        record.setUserId(userId);
        record.setPairName(pairName);
        record.setCurrency(currency);
        record.setAmount(amount);
        record.setFromAccount("SPOT");
        record.setToAccount("LEVERAGED");
        record.setTransferType("DEPOSIT");
        record.setStatus("PENDING");

        record = transferRecordRepository.save(record);

        try {
            // 调用钱包服务转入资金：从 SPOT 账户扣除，向 LEVERAGED 账户增加
            walletService.deductBalance(userId, "SPOT", currency, amount);
            walletService.addBalance(userId, "LEVERAGED", currency, amount);

            // 转账成功，更新状态
            record.setStatus("SUCCESS");
            transferRecordRepository.save(record);
        } catch (Exception e) {
            // 转账失败，更新状态
            record.setStatus("FAILED");
            record.setRemark("转账失败: " + e.getMessage());
            transferRecordRepository.save(record);
            throw new RuntimeException("转入资金失败: " + e.getMessage(), e);
        }

        return record;
    }

    @Override
    @Transactional
    public LeveragedFundTransferRecord transferOut(Long userId, String pairName, String currency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("转账金额必须大于0");
        }

        // 检查杠杆账户余额是否充足
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, "LEVERAGED", currency);
        if (availableBalance == null || availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("杠杆账户余额不足，可用余额: " + (availableBalance != null ? availableBalance : BigDecimal.ZERO));
        }

        // 生成转账ID
        String transferId = "LT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建转账记录（初始状态为 PENDING）
        LeveragedFundTransferRecord record = new LeveragedFundTransferRecord();
        record.setTransferId(transferId);
        record.setUserId(userId);
        record.setPairName(pairName);
        record.setCurrency(currency);
        record.setAmount(amount);
        record.setFromAccount("LEVERAGED");
        record.setToAccount("SPOT");
        record.setTransferType("WITHDRAW");
        record.setStatus("PENDING");

        record = transferRecordRepository.save(record);

        try {
            // 调用钱包服务转出资金：从 LEVERAGED 账户扣除，向 SPOT 账户增加
            walletService.deductBalance(userId, "LEVERAGED", currency, amount);
            walletService.addBalance(userId, "SPOT", currency, amount);

            // 转账成功，更新状态
            record.setStatus("SUCCESS");
            transferRecordRepository.save(record);
        } catch (Exception e) {
            // 转账失败，更新状态
            record.setStatus("FAILED");
            record.setRemark("转账失败: " + e.getMessage());
            transferRecordRepository.save(record);
            throw new RuntimeException("转出资金失败: " + e.getMessage(), e);
        }

        return record;
    }

    @Override
    public List<LeveragedFundTransferRecord> getTransferHistory(Long userId, String pairName, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null) {
            return transferRecordRepository.findByCreatedAtBetween(startTime, endTime);
        }
        
        if (pairName != null) {
            return transferRecordRepository.findByUserIdAndPairName(userId, pairName);
        }
        
        return transferRecordRepository.findByUserId(userId);
    }

    @Override
    public LeveragedFundTransferRecord getTransferRecord(String transferId) {
        Optional<LeveragedFundTransferRecord> recordOpt = transferRecordRepository.findByTransferId(transferId);
        return recordOpt.orElseThrow(() -> new RuntimeException("转账记录不存在"));
    }

    @Override
    public List<LeveragedFundTransferRecord> getTransferRecordsByType(String transferType) {
        return transferRecordRepository.findByTransferType(transferType);
    }

    @Override
    public List<LeveragedFundTransferRecord> getTransferRecordsByStatus(String status) {
        return transferRecordRepository.findByStatus(status);
    }
}












