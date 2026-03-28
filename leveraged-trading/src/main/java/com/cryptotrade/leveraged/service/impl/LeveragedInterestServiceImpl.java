/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedAccount;
import com.cryptotrade.leveraged.entity.LeveragedInterestRecord;
import com.cryptotrade.leveraged.repository.LeveragedAccountRepository;
import com.cryptotrade.leveraged.repository.LeveragedInterestRecordRepository;
import com.cryptotrade.leveraged.service.LeveragedInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 杠杆利息服务实现类
 */
@Service
public class LeveragedInterestServiceImpl implements LeveragedInterestService {

    @Autowired
    private LeveragedInterestRecordRepository interestRecordRepository;

    @Autowired
    private LeveragedAccountRepository accountRepository;

    @Override
    public List<LeveragedInterestRecord> getInterestRecords(Long userId, String pairName) {
        return interestRecordRepository.findByUserIdAndPairName(userId, pairName);
    }

    @Override
    public BigDecimal previewInterest(Long userId, String pairName, BigDecimal amount, Integer leverage) {
        Optional<LeveragedAccount> accountOpt = accountRepository.findByUserIdAndPairName(userId, pairName);
        if (!accountOpt.isPresent()) {
            throw new RuntimeException("杠杆账户不存在");
        }

        LeveragedAccount account = accountOpt.get();
        BigDecimal interestRate = account.getInterestRate();
        if (interestRate == null) {
            interestRate = new BigDecimal("0.0001"); // 默认利率
        }

        // 计算借款金额
        BigDecimal borrowedAmount = amount.multiply(new BigDecimal(leverage)).subtract(amount);
        
        // 计算一小时利息（假设按小时结算）
        return calculateInterest(borrowedAmount, interestRate, 1);
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal borrowedAmount, BigDecimal interestRate, Integer hours) {
        if (borrowedAmount == null || borrowedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (interestRate == null) {
            interestRate = new BigDecimal("0.0001"); // 默认利率
        }

        // 利息 = 借款金额 * 利率 * 小时数
        BigDecimal interest = borrowedAmount.multiply(interestRate).multiply(new BigDecimal(hours));
        return interest.setScale(8, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void settleInterest(Long accountId, LocalDateTime settlementTime) {
        Optional<LeveragedAccount> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent()) {
            return;
        }

        LeveragedAccount account = accountOpt.get();
        
        // 计算利息
        BigDecimal borrowedAmount = account.getBorrowedAmount();
        if (borrowedAmount == null || borrowedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal interestRate = account.getInterestRate();
        if (interestRate == null) {
            interestRate = new BigDecimal("0.0001"); // 默认利率
        }

        // 计算利息（假设按小时结算）
        BigDecimal interestAmount = calculateInterest(borrowedAmount, interestRate, 1);

        // 创建利息记录
        LeveragedInterestRecord record = new LeveragedInterestRecord();
        record.setUserId(account.getUserId());
        record.setAccountId(accountId);
        record.setPairName(account.getPairName());
        record.setBorrowedAmount(borrowedAmount);
        record.setInterestRate(interestRate);
        record.setInterestAmount(interestAmount);
        record.setSettlementCycle("HOURLY");
        record.setSettlementTime(settlementTime);
        record.setStatus("SETTLED");

        interestRecordRepository.save(record);

        // 从账户余额中扣除利息（这里需要调用钱包服务）
        // TODO: 调用钱包服务扣除利息
    }

    @Override
    public List<LeveragedInterestRecord> getInterestRecordsBySettlementCycle(String settlementCycle) {
        return interestRecordRepository.findBySettlementCycle(settlementCycle);
    }

    @Override
    public List<LeveragedInterestRecord> getInterestRecordsByStatus(String status) {
        return interestRecordRepository.findByStatus(status);
    }
}













