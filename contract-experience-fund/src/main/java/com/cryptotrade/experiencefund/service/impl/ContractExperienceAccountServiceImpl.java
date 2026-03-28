/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.service.impl;

import com.cryptotrade.experiencefund.entity.ContractExperienceAccount;
import com.cryptotrade.experiencefund.entity.ContractExperienceActivity;
import com.cryptotrade.experiencefund.entity.ContractExperienceAccountChange;
import com.cryptotrade.experiencefund.repository.ContractExperienceAccountChangeRepository;
import com.cryptotrade.experiencefund.repository.ContractExperienceAccountRepository;
import com.cryptotrade.experiencefund.repository.ContractExperienceActivityRepository;
import com.cryptotrade.experiencefund.service.ContractExperienceAccountService;
import com.cryptotrade.experiencefund.util.AccountNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合约体验金账户Service实现
 */
@Service
public class ContractExperienceAccountServiceImpl implements ContractExperienceAccountService {

    @Autowired
    private ContractExperienceAccountRepository accountRepository;

    @Autowired
    private ContractExperienceActivityRepository activityRepository;

    @Autowired
    private ContractExperienceAccountChangeRepository changeRepository;

    @Override
    @Transactional
    public ContractExperienceAccount createAccount(Long userId, Long activityId) {
        // 检查用户是否已有账户
        if (accountRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("用户已存在体验金账户");
        }

        // 获取活动信息
        ContractExperienceActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("体验金活动不存在: " + activityId));

        // 检查活动状态
        if (!"ACTIVE".equals(activity.getStatus())) {
            throw new RuntimeException("体验金活动不可用");
        }

        // 创建账户
        ContractExperienceAccount account = new ContractExperienceAccount();
        account.setUserId(userId);
        account.setAccountNo(AccountNoGenerator.generateAccountNo());
        account.setInitialAmount(activity.getExperienceAmount());
        account.setBalance(activity.getExperienceAmount());
        account.setCurrency(activity.getCurrency());
        account.setMaxLeverage(activity.getMaxLeverage());
        account.setMaxPosition(activity.getMaxPosition());
        account.setDailyTradeLimit(activity.getDailyTradeLimit());
        account.setAccountStatus("ACTIVE");
        account.setCreateTime(LocalDateTime.now());
        account.setExpireTime(LocalDateTime.now().plusDays(activity.getValidDays()));

        ContractExperienceAccount savedAccount = accountRepository.save(account);

        // 创建账户变更记录
        ContractExperienceAccountChange change = new ContractExperienceAccountChange();
        change.setAccountId(savedAccount.getId());
        change.setUserId(userId);
        change.setChangeType("INIT");
        change.setChangeAmount(activity.getExperienceAmount());
        change.setBeforeBalance(BigDecimal.ZERO);
        change.setAfterBalance(activity.getExperienceAmount());
        change.setActivityId(activityId);
        changeRepository.save(change);

        // 更新活动的已发放数量
        activity.setIssuedCount(activity.getIssuedCount() + 1);
        activityRepository.save(activity);

        return savedAccount;
    }

    @Override
    public ContractExperienceAccount getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在"));
    }

    @Override
    public ContractExperienceAccount getAccountByAccountNo(String accountNo) {
        return accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在"));
    }

    @Override
    @Transactional
    public void updateAccountBalance(Long accountId, BigDecimal changeAmount, String changeType, Long tradeId) {
        ContractExperienceAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在: " + accountId));

        BigDecimal beforeBalance = account.getBalance();
        BigDecimal afterBalance = beforeBalance.add(changeAmount);

        // 更新账户余额
        account.setBalance(afterBalance);

        // 更新盈亏统计
        if ("TRADE_PROFIT".equals(changeType)) {
            account.setTotalProfit(account.getTotalProfit().add(changeAmount));
        } else if ("TRADE_LOSS".equals(changeType)) {
            account.setTotalLoss(account.getTotalLoss().add(changeAmount.abs()));
        }

        accountRepository.save(account);

        // 创建账户变更记录
        ContractExperienceAccountChange change = new ContractExperienceAccountChange();
        change.setAccountId(accountId);
        change.setUserId(account.getUserId());
        change.setChangeType(changeType);
        change.setChangeAmount(changeAmount);
        change.setBeforeBalance(beforeBalance);
        change.setAfterBalance(afterBalance);
        change.setTradeId(tradeId);
        changeRepository.save(change);
    }

    @Override
    public boolean canTrade(Long accountId) {
        ContractExperienceAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在: " + accountId));

        // 检查账户状态
        if (!"ACTIVE".equals(account.getAccountStatus())) {
            return false;
        }

        // 检查是否过期
        if (LocalDateTime.now().isAfter(account.getExpireTime())) {
            return false;
        }

        // 检查余额
        if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // 检查每日交易次数限制
        if (account.getDailyTradeLimit() != null) {
            LocalDate today = LocalDate.now();
            if (account.getLastTradeDate() == null || !account.getLastTradeDate().equals(today)) {
                // 新的一天，重置交易次数
                account.setDailyTradeCount(0);
                account.setLastTradeDate(today);
                accountRepository.save(account);
            }
            if (account.getDailyTradeCount() >= account.getDailyTradeLimit()) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional
    public void incrementTradeCount(Long accountId) {
        ContractExperienceAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在: " + accountId));

        LocalDate today = LocalDate.now();
        if (account.getLastTradeDate() == null || !account.getLastTradeDate().equals(today)) {
            account.setDailyTradeCount(0);
            account.setLastTradeDate(today);
        }

        account.setDailyTradeCount(account.getDailyTradeCount() + 1);
        account.setTotalTrades(account.getTotalTrades() + 1);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void expireAccount(Long accountId) {
        ContractExperienceAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在: " + accountId));

        account.setAccountStatus("EXPIRED");
        accountRepository.save(account);
    }
}














