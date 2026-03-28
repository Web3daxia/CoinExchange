/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedAccount;
import com.cryptotrade.leveraged.entity.LeveragedOrder;
import com.cryptotrade.leveraged.entity.LeveragedPosition;
import com.cryptotrade.leveraged.repository.LeveragedAccountRepository;
import com.cryptotrade.leveraged.repository.LeveragedOrderRepository;
import com.cryptotrade.leveraged.repository.LeveragedPositionRepository;
import com.cryptotrade.leveraged.service.LeverageService;
import com.cryptotrade.leveraged.service.LeveragedOrderService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 杠杆订单服务实现类
 */
@Service
public class LeveragedOrderServiceImpl implements LeveragedOrderService {

    @Autowired
    private LeveragedOrderRepository leveragedOrderRepository;

    @Autowired
    private LeveragedAccountRepository leveragedAccountRepository;

    @Autowired
    private LeveragedPositionRepository leveragedPositionRepository;

    @Autowired
    private LeverageService leverageService;

    @Autowired
    private WalletService walletService;

    // 默认手续费率 0.1%
    private static final BigDecimal DEFAULT_FEE_RATE = new BigDecimal("0.001");

    @Override
    @Transactional
    public LeveragedOrder createMarketOrder(Long userId, String pairName, String side, String action,
                                           BigDecimal quantity, Integer leverage) {
        // 获取或创建杠杆账户
        LeveragedAccount account = leverageService.getLeverage(userId, pairName);
        if (account.getId() == null) {
            account = leveragedAccountRepository.save(account);
        }

        // 获取当前市场价格（这里需要从市场数据服务获取）
        BigDecimal currentPrice = getCurrentPrice(pairName);

        // 创建市价单
        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(account.getId());
        order.setPairName(pairName);
        order.setOrderType("MARKET");
        order.setSide(side);
        order.setAction(action);
        order.setQuantity(quantity);
        order.setPrice(currentPrice); // 市价单使用当前价格
        order.setLeverage(leverage);
        order.setStatus("PENDING");

        // 计算所需保证金
        BigDecimal requiredMargin = calculateRequiredMargin(quantity, currentPrice, leverage);
        order.setFee(quantity.multiply(currentPrice).multiply(DEFAULT_FEE_RATE));

        // 检查余额
        if ("OPEN".equals(action)) {
            checkBalance(userId, pairName, requiredMargin);
        }

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public LeveragedOrder createLimitOrder(Long userId, String pairName, String side, String action,
                                          BigDecimal quantity, BigDecimal price, Integer leverage) {
        LeveragedAccount account = leverageService.getLeverage(userId, pairName);
        if (account.getId() == null) {
            account = leveragedAccountRepository.save(account);
        }

        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(account.getId());
        order.setPairName(pairName);
        order.setOrderType("LIMIT");
        order.setSide(side);
        order.setAction(action);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setLeverage(leverage);
        order.setStatus("PENDING");

        BigDecimal requiredMargin = calculateRequiredMargin(quantity, price, leverage);
        order.setFee(quantity.multiply(price).multiply(DEFAULT_FEE_RATE));

        if ("OPEN".equals(action)) {
            checkBalance(userId, pairName, requiredMargin);
        }

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public LeveragedOrder createStopLossOrder(Long userId, Long positionId, BigDecimal stopPrice) {
        LeveragedPosition position = leveragedPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此仓位");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            throw new RuntimeException("仓位状态不允许设置止损");
        }

        // 更新仓位的止损价格
        position.setStopLossPrice(stopPrice);
        leveragedPositionRepository.save(position);

        // 创建止损单
        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(position.getAccountId());
        order.setPairName(position.getPairName());
        order.setOrderType("STOP_LOSS");
        order.setSide("LONG".equals(position.getSide()) ? "SELL" : "BUY");
        order.setAction("CLOSE");
        order.setQuantity(position.getQuantity());
        order.setStopPrice(stopPrice);
        order.setLeverage(position.getLeverage());
        order.setStatus("PENDING");

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public LeveragedOrder createTakeProfitOrder(Long userId, Long positionId, BigDecimal takeProfitPrice) {
        LeveragedPosition position = leveragedPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此仓位");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            throw new RuntimeException("仓位状态不允许设置止盈");
        }

        // 更新仓位的止盈价格
        position.setTakeProfitPrice(takeProfitPrice);
        leveragedPositionRepository.save(position);

        // 创建止盈单
        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(position.getAccountId());
        order.setPairName(position.getPairName());
        order.setOrderType("TAKE_PROFIT");
        order.setSide("LONG".equals(position.getSide()) ? "SELL" : "BUY");
        order.setAction("CLOSE");
        order.setQuantity(position.getQuantity());
        order.setTakeProfitPrice(takeProfitPrice);
        order.setLeverage(position.getLeverage());
        order.setStatus("PENDING");

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public LeveragedOrder createStopLimitOrder(Long userId, String pairName, String side, String action,
                                              BigDecimal quantity, BigDecimal stopPrice, BigDecimal limitPrice,
                                              Integer leverage) {
        LeveragedAccount account = leverageService.getLeverage(userId, pairName);
        if (account.getId() == null) {
            account = leveragedAccountRepository.save(account);
        }

        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(account.getId());
        order.setPairName(pairName);
        order.setOrderType("STOP_LIMIT");
        order.setSide(side);
        order.setAction(action);
        order.setQuantity(quantity);
        order.setStopPrice(stopPrice);
        order.setPrice(limitPrice);
        order.setLeverage(leverage);
        order.setStatus("PENDING");

        BigDecimal requiredMargin = calculateRequiredMargin(quantity, limitPrice, leverage);
        order.setFee(quantity.multiply(limitPrice).multiply(DEFAULT_FEE_RATE));

        if ("OPEN".equals(action)) {
            checkBalance(userId, pairName, requiredMargin);
        }

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public LeveragedOrder createConditionalOrder(Long userId, String pairName, String side, String action,
                                                 BigDecimal quantity, BigDecimal triggerPrice, String conditionType,
                                                 BigDecimal price, Integer leverage) {
        LeveragedAccount account = leverageService.getLeverage(userId, pairName);
        if (account.getId() == null) {
            account = leveragedAccountRepository.save(account);
        }

        LeveragedOrder order = new LeveragedOrder();
        order.setUserId(userId);
        order.setAccountId(account.getId());
        order.setPairName(pairName);
        order.setOrderType("CONDITIONAL");
        order.setSide(side);
        order.setAction(action);
        order.setQuantity(quantity);
        order.setTriggerPrice(triggerPrice);
        order.setConditionType(conditionType);
        order.setPrice(price);
        order.setLeverage(leverage);
        order.setStatus("PENDING");

        BigDecimal orderPrice = price != null ? price : triggerPrice;
        BigDecimal requiredMargin = calculateRequiredMargin(quantity, orderPrice, leverage);
        order.setFee(quantity.multiply(orderPrice).multiply(DEFAULT_FEE_RATE));

        if ("OPEN".equals(action)) {
            checkBalance(userId, pairName, requiredMargin);
        }

        return leveragedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        LeveragedOrder order = leveragedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        order.setStatus("CANCELLED");
        leveragedOrderRepository.save(order);
    }

    @Override
    public List<LeveragedOrder> getUserOrders(Long userId) {
        return leveragedOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<LeveragedOrder> getOrderHistory(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return leveragedOrderRepository.findByUserIdAndCreatedAtBetween(userId, startTime, endTime);
    }

    @Override
    @Transactional
    public LeveragedPosition executeOrder(Long orderId) {
        LeveragedOrder order = leveragedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许执行");
        }

        // 获取当前市场价格
        BigDecimal currentPrice = getCurrentPrice(order.getPairName());

        // 更新订单状态
        order.setStatus("FILLED");
        order.setFilledQuantity(order.getQuantity());
        order.setFilledAmount(order.getQuantity().multiply(currentPrice));
        order.setFilledAt(LocalDateTime.now());
        leveragedOrderRepository.save(order);

        // 处理资金和仓位
        if ("OPEN".equals(order.getAction())) {
            return createPosition(order, currentPrice);
        } else {
            closePosition(order, currentPrice);
            return null;
        }
    }

    @Override
    @Scheduled(fixedRate = 10000) // 每10秒执行一次
    public void checkAndTriggerConditionalOrders() {
        List<LeveragedOrder> conditionalOrders = leveragedOrderRepository.findByStatusAndTriggerPriceIsNotNull("PENDING");

        for (LeveragedOrder order : conditionalOrders) {
            try {
                BigDecimal currentPrice = getCurrentPrice(order.getPairName());
                boolean shouldTrigger = false;

                if ("ABOVE".equals(order.getConditionType())) {
                    shouldTrigger = currentPrice.compareTo(order.getTriggerPrice()) >= 0;
                } else if ("BELOW".equals(order.getConditionType())) {
                    shouldTrigger = currentPrice.compareTo(order.getTriggerPrice()) <= 0;
                }

                if (shouldTrigger) {
                    order.setStatus("TRIGGERED");
                    order.setTriggeredAt(LocalDateTime.now());
                    leveragedOrderRepository.save(order);

                    // 如果触发后是限价单，保持PENDING状态等待成交
                    // 如果触发后是市价单，立即执行
                    if (order.getPrice() == null) {
                        executeOrder(order.getId());
                    }
                }
            } catch (Exception e) {
                System.err.println("检查条件单失败: " + order.getId() + ", " + e.getMessage());
            }
        }
    }

    /**
     * 创建仓位
     */
    private LeveragedPosition createPosition(LeveragedOrder order, BigDecimal fillPrice) {
        LeveragedAccount account = leveragedAccountRepository.findById(order.getAccountId())
                .orElseThrow(() -> new RuntimeException("杠杆账户不存在"));

        // 计算所需保证金和借入资金
        BigDecimal totalValue = order.getQuantity().multiply(fillPrice);
        BigDecimal margin = totalValue.divide(new BigDecimal(order.getLeverage()), 8, RoundingMode.HALF_UP);
        BigDecimal borrowedAmount = totalValue.subtract(margin);

        // 更新账户
        account.setMargin(account.getMargin().add(margin));
        account.setBorrowedAmount(account.getBorrowedAmount().add(borrowedAmount));
        account.setAvailableBalance(account.getAvailableBalance().subtract(margin));
        leveragedAccountRepository.save(account);

        // 创建仓位
        LeveragedPosition position = new LeveragedPosition();
        position.setUserId(order.getUserId());
        position.setAccountId(order.getAccountId());
        position.setPairName(order.getPairName());
        position.setSide("BUY".equals(order.getSide()) ? "LONG" : "SHORT");
        position.setQuantity(order.getQuantity());
        position.setEntryPrice(fillPrice);
        position.setCurrentPrice(fillPrice);
        position.setLeverage(order.getLeverage());
        position.setMargin(margin);
        position.setInitialMargin(margin);
        position.setMaintenanceMargin(margin.multiply(new BigDecimal("0.5"))); // 维持保证金为初始保证金的50%
        position.setBorrowedAmount(borrowedAmount);
        position.setUnrealizedPnl(BigDecimal.ZERO);
        position.setRealizedPnl(BigDecimal.ZERO);
        position.setStatus("ACTIVE");

        // 计算强平价格
        position.setLiquidationPrice(calculateLiquidationPrice(position));

        return leveragedPositionRepository.save(position);
    }

    /**
     * 平仓
     */
    private void closePosition(LeveragedOrder order, BigDecimal fillPrice) {
        // 找到对应的仓位
        String oppositeSide = "BUY".equals(order.getSide()) ? "SHORT" : "LONG";
        Optional<LeveragedPosition> positionOpt = leveragedPositionRepository
                .findByUserIdAndPairNameAndSideAndStatus(order.getUserId(), order.getPairName(), oppositeSide, "ACTIVE");

        if (!positionOpt.isPresent()) {
            throw new RuntimeException("仓位不存在，无法平仓");
        }

        LeveragedPosition position = positionOpt.get();

        // 计算盈亏
        BigDecimal pnl;
        if ("LONG".equals(position.getSide())) {
            pnl = fillPrice.subtract(position.getEntryPrice()).multiply(position.getQuantity());
        } else {
            pnl = position.getEntryPrice().subtract(fillPrice).multiply(position.getQuantity());
        }

        // 更新仓位
        position.setStatus("CLOSED");
        position.setRealizedPnl(position.getRealizedPnl().add(pnl));
        leveragedPositionRepository.save(position);

        // 更新账户
        LeveragedAccount account = leveragedAccountRepository.findById(order.getAccountId())
                .orElseThrow(() -> new RuntimeException("杠杆账户不存在"));

        account.setMargin(account.getMargin().subtract(position.getMargin()));
        account.setBorrowedAmount(account.getBorrowedAmount().subtract(position.getBorrowedAmount()));
        account.setAvailableBalance(account.getAvailableBalance().add(position.getMargin()).add(pnl));
        leveragedAccountRepository.save(account);
    }

    /**
     * 计算所需保证金
     */
    private BigDecimal calculateRequiredMargin(BigDecimal quantity, BigDecimal price, Integer leverage) {
        BigDecimal totalValue = quantity.multiply(price);
        return totalValue.divide(new BigDecimal(leverage), 8, RoundingMode.HALF_UP);
    }

    /**
     * 检查余额
     */
    private void checkBalance(Long userId, String pairName, BigDecimal requiredMargin) {
        String[] parts = pairName.split("/");
        String currency = parts[1]; // 计价货币

        BigDecimal balance = walletService.getAvailableBalance(userId, "SPOT", currency);
        if (balance.compareTo(requiredMargin) < 0) {
            throw new RuntimeException("余额不足，需要保证金: " + requiredMargin);
        }
    }

    /**
     * 获取当前价格（需要从市场数据服务获取）
     */
    private BigDecimal getCurrentPrice(String pairName) {
        // TODO: 从市场数据服务获取当前价格
        // 这里返回一个默认值，实际应该从spot-trading模块的市场数据服务获取
        return new BigDecimal("50000");
    }

    /**
     * 计算强平价格
     */
    private BigDecimal calculateLiquidationPrice(LeveragedPosition position) {
        // 强平价格 = 开仓价格 * (1 - 1/杠杆倍数 * (1 - 维持保证金率))
        BigDecimal leverage = new BigDecimal(position.getLeverage());
        BigDecimal maintenanceMarginRate = position.getMaintenanceMargin()
                .divide(position.getInitialMargin(), 8, RoundingMode.HALF_UP);

        if ("LONG".equals(position.getSide())) {
            BigDecimal factor = BigDecimal.ONE.subtract(
                    BigDecimal.ONE.divide(leverage, 8, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.subtract(maintenanceMarginRate))
            );
            return position.getEntryPrice().multiply(factor);
        } else {
            BigDecimal factor = BigDecimal.ONE.add(
                    BigDecimal.ONE.divide(leverage, 8, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.subtract(maintenanceMarginRate))
            );
            return position.getEntryPrice().multiply(factor);
        }
    }
}




