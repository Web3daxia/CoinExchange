/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.service.impl;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccount;
import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccountChange;
import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRule;
import com.cryptotrade.simulatedtrading.repository.SimulatedTradingAccountChangeRepository;
import com.cryptotrade.simulatedtrading.repository.SimulatedTradingAccountRepository;
import com.cryptotrade.simulatedtrading.repository.SimulatedTradingRuleRepository;
import com.cryptotrade.simulatedtrading.service.SimulatedTradingAccountService;
import com.cryptotrade.simulatedtrading.util.AccountNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 模拟交易账户Service实现
 */
@Service
public class SimulatedTradingAccountServiceImpl implements SimulatedTradingAccountService {

    @Autowired
    private SimulatedTradingAccountRepository accountRepository;

    @Autowired
    private SimulatedTradingAccountChangeRepository changeRepository;

    @Autowired
    private SimulatedTradingRuleRepository ruleRepository;

    @Override
    @Transactional
    public SimulatedTradingAccount createOrGetAccount(Long userId) {
        // 检查用户是否已有账户
        return accountRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // 创建新账户
                    BigDecimal initialBalance = getInitialBalance();

                    SimulatedTradingAccount account = new SimulatedTradingAccount();
                    account.setUserId(userId);
                    account.setAccountNo(AccountNoGenerator.generateAccountNo());
                    account.setBalance(initialBalance);
                    account.setInitialBalance(initialBalance);
                    account.setCurrency("USDT");
                    account.setMaxLeverage(getMaxLeverage());
                    account.setMaxPosition(getMaxPosition());
                    account.setMaxTradeAmount(getMaxTradeAmount());
                    account.setAccountStatus("ACTIVE");
                    account.setCreateTime(LocalDateTime.now());

                    SimulatedTradingAccount savedAccount = accountRepository.save(account);

                    // 创建账户变更记录
                    createAccountChange(savedAccount.getId(), userId, "INIT", initialBalance,
                            BigDecimal.ZERO, initialBalance, null);

                    return savedAccount;
                });
    }

    @Override
    public SimulatedTradingAccount getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("模拟账户不存在，请先创建账户"));
    }

    @Override
    public SimulatedTradingAccount getAccountByAccountNo(String accountNo) {
        return accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("模拟账户不存在"));
    }

    @Override
    @Transactional
    public SimulatedTradingAccount resetAccount(Long userId, BigDecimal newBalance) {
        SimulatedTradingAccount account = getAccountByUserId(userId);

        BigDecimal beforeBalance = account.getBalance();
        if (newBalance == null) {
            newBalance = account.getInitialBalance();
        }

        account.setBalance(newBalance);
        account.setLastResetTime(LocalDateTime.now());
        account.setResetCount(account.getResetCount() + 1);
        account.setTotalProfit(BigDecimal.ZERO);
        account.setTotalLoss(BigDecimal.ZERO);
        account.setTotalTrades(0);
        account.setTotalWinTrades(0);
        account.setTotalLoseTrades(0);
        account.setMaxDrawdown(BigDecimal.ZERO);
        account.setWinRate(BigDecimal.ZERO);

        SimulatedTradingAccount savedAccount = accountRepository.save(account);

        // 创建账户变更记录
        createAccountChange(savedAccount.getId(), userId, "RESET", newBalance.subtract(beforeBalance),
                beforeBalance, newBalance, null);

        return savedAccount;
    }

    @Override
    @Transactional
    public void updateAccountBalance(Long accountId, BigDecimal changeAmount, String changeType, Long tradeId) {
        SimulatedTradingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("模拟账户不存在: " + accountId));

        BigDecimal beforeBalance = account.getBalance();
        BigDecimal afterBalance = beforeBalance.add(changeAmount);

        if (afterBalance.compareTo(BigDecimal.ZERO) < 0) {
            afterBalance = BigDecimal.ZERO;
        }

        account.setBalance(afterBalance);
        account.setLastTradeTime(LocalDateTime.now());

        // 更新盈亏统计
        if ("TRADE_PROFIT".equals(changeType)) {
            account.setTotalProfit(account.getTotalProfit().add(changeAmount));
        } else if ("TRADE_LOSS".equals(changeType)) {
            account.setTotalLoss(account.getTotalLoss().add(changeAmount.abs()));
        }

        accountRepository.save(account);

        // 创建账户变更记录
        createAccountChange(accountId, account.getUserId(), changeType, changeAmount,
                beforeBalance, afterBalance, tradeId);
    }

    @Override
    @Transactional
    public void updateAccountStatistics(Long accountId, boolean isWin, BigDecimal profitLoss) {
        SimulatedTradingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("模拟账户不存在: " + accountId));

        account.setTotalTrades(account.getTotalTrades() + 1);

        if (isWin) {
            account.setTotalWinTrades(account.getTotalWinTrades() + 1);
        } else {
            account.setTotalLoseTrades(account.getTotalLoseTrades() + 1);
        }

        // 计算胜率
        if (account.getTotalTrades() > 0) {
            BigDecimal winRate = BigDecimal.valueOf(account.getTotalWinTrades())
                    .divide(BigDecimal.valueOf(account.getTotalTrades()), 6, RoundingMode.HALF_UP);
            account.setWinRate(winRate);
        }

        // 更新最大回撤（简化计算）
        if (profitLoss.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal drawdown = profitLoss.abs();
            if (drawdown.compareTo(account.getMaxDrawdown()) > 0) {
                account.setMaxDrawdown(drawdown);
            }
        }

        accountRepository.save(account);
    }

    /**
     * 创建账户变更记录
     */
    private void createAccountChange(Long accountId, Long userId, String changeType, BigDecimal changeAmount,
                                     BigDecimal beforeBalance, BigDecimal afterBalance, Long tradeId) {
        SimulatedTradingAccountChange change = new SimulatedTradingAccountChange();
        change.setAccountId(accountId);
        change.setUserId(userId);
        change.setChangeType(changeType);
        change.setChangeAmount(changeAmount);
        change.setBeforeBalance(beforeBalance);
        change.setAfterBalance(afterBalance);
        change.setTradeId(tradeId);
        changeRepository.save(change);
    }

    /**
     * 获取初始余额（从规则配置）
     */
    private BigDecimal getInitialBalance() {
        return ruleRepository.findByRuleKey("INITIAL_BALANCE")
                .map(rule -> new BigDecimal(rule.getRuleValue()))
                .orElse(BigDecimal.valueOf(10000));
    }

    /**
     * 获取最大杠杆（从规则配置）
     */
    private Integer getMaxLeverage() {
        return ruleRepository.findByRuleKey("MAX_LEVERAGE")
                .map(rule -> Integer.parseInt(rule.getRuleValue()))
                .orElse(10);
    }

    /**
     * 获取最大仓位（从规则配置）
     */
    private BigDecimal getMaxPosition() {
        return ruleRepository.findByRuleKey("MAX_POSITION")
                .map(rule -> new BigDecimal(rule.getRuleValue()))
                .orElse(BigDecimal.valueOf(1000));
    }

    /**
     * 获取最大交易金额（从规则配置）
     */
    private BigDecimal getMaxTradeAmount() {
        return ruleRepository.findByRuleKey("MAX_TRADE_AMOUNT")
                .map(rule -> new BigDecimal(rule.getRuleValue()))
                .orElse(null);
    }
}














