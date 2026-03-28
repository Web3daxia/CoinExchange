/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.service.impl;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccount;
import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRecord;
import com.cryptotrade.simulatedtrading.repository.SimulatedTradingAccountRepository;
import com.cryptotrade.simulatedtrading.repository.SimulatedTradingRecordRepository;
import com.cryptotrade.simulatedtrading.service.SimulatedTradingAccountService;
import com.cryptotrade.simulatedtrading.service.SimulatedTradingService;
import com.cryptotrade.simulatedtrading.util.AccountNoGenerator;
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
 * 模拟交易Service实现
 */
@Service
public class SimulatedTradingServiceImpl implements SimulatedTradingService {

    @Autowired
    private SimulatedTradingRecordRepository tradeRepository;

    @Autowired
    private SimulatedTradingAccountRepository accountRepository;

    @Autowired
    private SimulatedTradingAccountService accountService;

    @Override
    @Transactional
    public SimulatedTradingRecord placeOrder(Long userId, Long accountId, String tradeType, String contractType,
                                              String pairName, String orderType, String side, Integer leverage,
                                              BigDecimal quantity, BigDecimal price, BigDecimal stopLossPrice,
                                              BigDecimal takeProfitPrice) {
        // 获取账户
        SimulatedTradingAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("模拟账户不存在: " + accountId));

        // 检查用户权限
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此账户");
        }

        // 检查账户状态
        if (!"ACTIVE".equals(account.getAccountStatus())) {
            throw new RuntimeException("账户状态不允许交易");
        }

        // 检查杠杆限制
        if (leverage == null) {
            leverage = 1;
        }
        if (leverage > account.getMaxLeverage()) {
            throw new RuntimeException("杠杆超过最大限制: " + account.getMaxLeverage());
        }

        // 计算交易金额
        BigDecimal amount;
        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            amount = quantity.multiply(price);
        } else {
            // 市价单，使用当前市场价格（这里简化处理，实际应该从市场数据获取）
            amount = quantity; // 简化处理
        }

        // 检查仓位限制
        if (account.getMaxPosition() != null && amount.compareTo(account.getMaxPosition()) > 0) {
            throw new RuntimeException("交易金额超过最大仓位限制: " + account.getMaxPosition());
        }

        // 检查交易金额限制
        if (account.getMaxTradeAmount() != null && amount.compareTo(account.getMaxTradeAmount()) > 0) {
            throw new RuntimeException("交易金额超过最大限制: " + account.getMaxTradeAmount());
        }

        // 计算所需资金（保证金+手续费）
        BigDecimal margin = BigDecimal.ZERO;
        if (leverage > 1 || !"SPOT".equals(tradeType)) {
            // 合约或杠杆交易需要保证金
            margin = amount.divide(BigDecimal.valueOf(leverage), 8, RoundingMode.HALF_UP);
        } else {
            // 现货交易需要全额资金
            margin = amount;
        }

        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.001)); // 假设手续费率为0.1%
        BigDecimal totalRequired = margin.add(fee);

        // 检查余额
        if (account.getBalance().compareTo(totalRequired) < 0) {
            throw new RuntimeException("账户余额不足");
        }

        // 创建交易记录
        SimulatedTradingRecord trade = new SimulatedTradingRecord();
        trade.setAccountId(accountId);
        trade.setUserId(userId);
        trade.setTradeNo(AccountNoGenerator.generateTradeNo());
        trade.setTradeType(tradeType);
        trade.setContractType(contractType);
        trade.setPairName(pairName);
        trade.setOrderType(orderType);
        trade.setSide(side);
        trade.setLeverage(leverage);
        trade.setQuantity(quantity);
        trade.setPrice(price);
        trade.setAmount(amount);
        trade.setFee(fee);
        trade.setMargin(margin);
        trade.setStopLossPrice(stopLossPrice);
        trade.setTakeProfitPrice(takeProfitPrice);
        trade.setOpenTime(LocalDateTime.now());

        // 设置交易类型相关字段
        if (!"SPOT".equals(tradeType)) {
            // 合约交易设置开仓价格和仓位方向
            if (price != null) {
                trade.setEntryPrice(price);
            }
            if ("BUY".equals(side)) {
                trade.setPositionSide("LONG");
            } else {
                trade.setPositionSide("SHORT");
            }
        }

        // 如果是市价单，立即成交
        if ("MARKET".equals(orderType)) {
            BigDecimal marketPrice = price != null ? price : getCurrentMarketPrice(pairName);
            trade.setExecutedPrice(marketPrice);
            
            if ("SPOT".equals(tradeType)) {
                // 现货交易立即完成
                trade.setTradeStatus("FILLED");
                trade.setCloseTime(LocalDateTime.now());
            } else {
                // 合约交易开仓，等待平仓
                trade.setEntryPrice(marketPrice);
                trade.setTradeStatus("OPEN");
            }

            // 先保存交易记录以获取ID
            SimulatedTradingRecord savedTrade = tradeRepository.save(trade);
            
            // 扣除资金
            BigDecimal changeAmount = totalRequired.negate();
            accountService.updateAccountBalance(accountId, changeAmount, "TRADE_LOSS", savedTrade.getId());
            
            return savedTrade;
        } else {
            // 限价单等，状态为OPEN
            trade.setTradeStatus("OPEN");
            return tradeRepository.save(trade);
        }
    }

    @Override
    @Transactional
    public SimulatedTradingRecord closePosition(Long userId, Long tradeId, BigDecimal exitPrice) {
        SimulatedTradingRecord trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在: " + tradeId));

        // 检查用户权限
        if (!trade.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此交易");
        }

        // 检查交易状态
        if (!"OPEN".equals(trade.getTradeStatus()) && !"FILLED".equals(trade.getTradeStatus())) {
            throw new RuntimeException("交易状态不允许平仓: " + trade.getTradeStatus());
        }

        // 计算盈亏
        BigDecimal profitLoss = calculateProfitLoss(trade.getEntryPrice() != null ? trade.getEntryPrice() : trade.getExecutedPrice(),
                exitPrice, trade.getQuantity(), trade.getTradeType(), trade.getPositionSide(), trade.getLeverage());

        BigDecimal closeFee = trade.getAmount().multiply(BigDecimal.valueOf(0.001)); // 平仓手续费

        // 更新交易记录
        trade.setExitPrice(exitPrice);
        trade.setCloseFee(closeFee);
        trade.setProfitLoss(profitLoss);
        trade.setTradeStatus("CLOSED");
        trade.setCloseTime(LocalDateTime.now());

        // 计算盈亏比例
        if (trade.getMargin() != null && trade.getMargin().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitLossRate = profitLoss.divide(trade.getMargin(), 6, RoundingMode.HALF_UP);
            trade.setProfitLossRate(profitLossRate);
        }

        SimulatedTradingRecord savedTrade = tradeRepository.save(trade);

        // 更新账户余额（退回保证金 + 盈亏 - 平仓手续费）
        // 如果是合约交易，退回保证金；如果是现货交易，退回的是交易金额
        BigDecimal margin = trade.getMargin() != null ? trade.getMargin() : 
                           (trade.getAmount() != null ? trade.getAmount() : BigDecimal.ZERO);
        BigDecimal changeAmount = margin.add(profitLoss).subtract(closeFee);
        String changeType = profitLoss.compareTo(BigDecimal.ZERO) >= 0 ? "TRADE_PROFIT" : "TRADE_LOSS";
        accountService.updateAccountBalance(trade.getAccountId(), changeAmount, changeType, tradeId);

        // 更新账户统计
        boolean isWin = profitLoss.compareTo(BigDecimal.ZERO) >= 0;
        accountService.updateAccountStatistics(trade.getAccountId(), isWin, profitLoss);

        return savedTrade;
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long tradeId) {
        SimulatedTradingRecord trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在: " + tradeId));

        if (!trade.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此交易");
        }

        if (!"OPEN".equals(trade.getTradeStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        trade.setTradeStatus("CANCELLED");
        tradeRepository.save(trade);

        // 如果是已扣款的订单，退回资金
        if (trade.getExecutedPrice() != null) {
            BigDecimal margin = trade.getMargin() != null ? trade.getMargin() : trade.getAmount();
            BigDecimal fee = trade.getFee() != null ? trade.getFee() : BigDecimal.ZERO;
            BigDecimal refund = margin.add(fee);
            accountService.updateAccountBalance(trade.getAccountId(), refund, "DEPOSIT", tradeId);
        }
    }

    @Override
    public SimulatedTradingRecord getTradeById(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("交易记录不存在: " + tradeId));
    }

    @Override
    public List<SimulatedTradingRecord> getAccountTrades(Long accountId) {
        return tradeRepository.findByAccountId(accountId);
    }

    @Override
    public Page<SimulatedTradingRecord> getUserTrades(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return tradeRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<SimulatedTradingRecord> getAccountOpenPositions(Long accountId) {
        return tradeRepository.findByAccountIdAndTradeStatus(accountId, "OPEN");
    }

    @Override
    public BigDecimal calculateProfitLoss(BigDecimal entryPrice, BigDecimal exitPrice, BigDecimal quantity,
                                          String tradeType, String positionSide, Integer leverage) {
        if (entryPrice == null || exitPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal priceDiff = exitPrice.subtract(entryPrice);

        if ("SPOT".equals(tradeType)) {
            // 现货交易：买入盈利 = (卖出价 - 买入价) * 数量
            return priceDiff.multiply(quantity);
        } else {
            // 合约交易：根据仓位方向计算
            if ("LONG".equals(positionSide)) {
                // 做多：价格上涨盈利
                return priceDiff.multiply(quantity);
            } else {
                // 做空：价格下跌盈利
                return priceDiff.multiply(quantity).negate();
            }
        }
    }

    /**
     * 获取当前市场价格（简化实现，实际应该从市场数据服务获取）
     */
    private BigDecimal getCurrentMarketPrice(String pairName) {
        // TODO: 从市场数据服务获取实时价格
        return BigDecimal.valueOf(50000); // 默认价格
    }
}

