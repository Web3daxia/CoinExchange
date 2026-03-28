/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.service.impl;

import com.cryptotrade.experiencefund.entity.ContractExperienceAccount;
import com.cryptotrade.experiencefund.entity.ContractExperienceTrade;
import com.cryptotrade.experiencefund.repository.ContractExperienceAccountRepository;
import com.cryptotrade.experiencefund.repository.ContractExperienceTradeRepository;
import com.cryptotrade.experiencefund.service.ContractExperienceAccountService;
import com.cryptotrade.experiencefund.service.ContractExperienceTradeService;
import com.cryptotrade.experiencefund.util.AccountNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合约体验金交易Service实现
 */
@Service
public class ContractExperienceTradeServiceImpl implements ContractExperienceTradeService {

    @Autowired
    private ContractExperienceTradeRepository tradeRepository;

    @Autowired
    private ContractExperienceAccountRepository accountRepository;

    @Autowired
    private ContractExperienceAccountService accountService;

    @Override
    @Transactional
    public ContractExperienceTrade openPosition(Long userId, Long accountId, String contractType, String pairName,
                                                String tradeType, String orderType, Integer leverage,
                                                BigDecimal positionSize, BigDecimal entryPrice) {
        // 获取账户
        ContractExperienceAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("体验金账户不存在: " + accountId));

        // 检查用户权限
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此账户");
        }

        // 检查是否可以交易
        if (!accountService.canTrade(accountId)) {
            throw new RuntimeException("账户当前无法交易");
        }

        // 检查杠杆限制
        if (leverage > account.getMaxLeverage()) {
            throw new RuntimeException("杠杆超过最大限制: " + account.getMaxLeverage());
        }

        // 检查仓位限制
        if (account.getMaxPosition() != null && positionSize.compareTo(account.getMaxPosition()) > 0) {
            throw new RuntimeException("仓位超过最大限制: " + account.getMaxPosition());
        }

        // 计算所需保证金和手续费
        BigDecimal margin = positionSize.divide(BigDecimal.valueOf(leverage), 8, RoundingMode.HALF_UP);
        BigDecimal openFee = margin.multiply(BigDecimal.valueOf(0.001)); // 假设手续费率为0.1%

        BigDecimal totalRequired = margin.add(openFee);

        // 检查余额
        if (account.getBalance().compareTo(totalRequired) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 创建交易记录
        ContractExperienceTrade trade = new ContractExperienceTrade();
        trade.setAccountId(accountId);
        trade.setUserId(userId);
        trade.setTradeNo(AccountNoGenerator.generateTradeNo());
        trade.setContractType(contractType);
        trade.setPairName(pairName);
        trade.setTradeType(tradeType);
        trade.setOrderType(orderType);
        trade.setLeverage(leverage);
        trade.setPositionSize(positionSize);
        trade.setEntryPrice(entryPrice);
        trade.setOpenFee(openFee);
        trade.setTradeStatus("OPEN");
        trade.setOpenTime(LocalDateTime.now());

        ContractExperienceTrade savedTrade = tradeRepository.save(trade);

        // 扣除保证金和手续费
        BigDecimal changeAmount = totalRequired.negate();
        accountService.updateAccountBalance(accountId, changeAmount, "TRADE_LOSS", savedTrade.getId());

        // 增加交易次数
        accountService.incrementTradeCount(accountId);

        return savedTrade;
    }

    @Override
    @Transactional
    public ContractExperienceTrade closePosition(Long userId, Long tradeId, BigDecimal exitPrice) {
        // 获取交易记录
        ContractExperienceTrade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在: " + tradeId));

        // 检查用户权限
        if (!trade.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此交易");
        }

        // 检查交易状态
        if (!"OPEN".equals(trade.getTradeStatus())) {
            throw new RuntimeException("交易状态不允许平仓: " + trade.getTradeStatus());
        }

        // 计算盈亏
        BigDecimal profitLoss = calculateProfitLoss(trade.getEntryPrice(), exitPrice, trade.getPositionSize(), trade.getTradeType());
        BigDecimal closeFee = trade.getPositionSize().multiply(BigDecimal.valueOf(0.001)); // 平仓手续费

        // 计算保证金
        BigDecimal margin = trade.getPositionSize().divide(BigDecimal.valueOf(trade.getLeverage()), 8, RoundingMode.HALF_UP);

        // 更新交易记录
        trade.setExitPrice(exitPrice);
        trade.setCloseFee(closeFee);
        trade.setProfitLoss(profitLoss);
        trade.setTradeStatus("CLOSED");
        trade.setCloseTime(LocalDateTime.now());

        // 计算盈亏比例
        if (margin.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitLossRate = profitLoss.divide(margin, 6, RoundingMode.HALF_UP);
            trade.setProfitLossRate(profitLossRate);
        }

        ContractExperienceTrade savedTrade = tradeRepository.save(trade);

        // 更新账户余额（退回保证金 + 盈亏 - 平仓手续费）
        BigDecimal changeAmount = margin.add(profitLoss).subtract(closeFee);
        String changeType = profitLoss.compareTo(BigDecimal.ZERO) >= 0 ? "TRADE_PROFIT" : "TRADE_LOSS";
        accountService.updateAccountBalance(trade.getAccountId(), changeAmount, changeType, tradeId);

        return savedTrade;
    }

    @Override
    public ContractExperienceTrade getTradeById(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在: " + tradeId));
    }

    @Override
    public List<ContractExperienceTrade> getAccountTrades(Long accountId) {
        return tradeRepository.findByAccountId(accountId);
    }

    @Override
    public Page<ContractExperienceTrade> getUserTrades(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return tradeRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<ContractExperienceTrade> getAccountOpenPositions(Long accountId) {
        return tradeRepository.findByAccountIdAndTradeStatus(accountId, "OPEN");
    }

    @Override
    public BigDecimal calculateProfitLoss(BigDecimal entryPrice, BigDecimal exitPrice, BigDecimal positionSize, String tradeType) {
        BigDecimal priceDiff = exitPrice.subtract(entryPrice);

        if ("OPEN_LONG".equals(tradeType) || "CLOSE_SHORT".equals(tradeType)) {
            // 做多：价格上涨盈利
            return priceDiff.multiply(positionSize);
        } else {
            // 做空：价格下跌盈利
            return priceDiff.multiply(positionSize).negate();
        }
    }
}

