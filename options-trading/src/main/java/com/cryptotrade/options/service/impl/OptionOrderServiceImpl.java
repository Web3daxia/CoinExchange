/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.entity.OptionOrder;
import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.repository.OptionOrderRepository;
import com.cryptotrade.options.repository.OptionPositionRepository;
import com.cryptotrade.options.service.OptionOrderService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 期权订单服务实现类
 */
@Service
public class OptionOrderServiceImpl implements OptionOrderService {

    @Autowired
    private OptionOrderRepository optionOrderRepository;

    @Autowired
    private OptionContractRepository optionContractRepository;

    @Autowired
    private OptionPositionRepository optionPositionRepository;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public OptionOrder createOrder(Long userId, Long contractId, String orderType, String side,
                                  BigDecimal quantity, BigDecimal price) {
        // 验证合约是否存在
        Optional<OptionContract> contractOpt = optionContractRepository.findById(contractId);
        if (!contractOpt.isPresent()) {
            throw new RuntimeException("期权合约不存在");
        }

        OptionContract contract = contractOpt.get();

        // 检查合约状态
        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("期权合约不可交易");
        }

        // 检查是否已到期
        if (contract.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("期权合约已到期");
        }

        // 计算总金额和手续费
        BigDecimal totalAmount = quantity.multiply(price);
        BigDecimal fee = totalAmount.multiply(new BigDecimal("0.001")); // 0.1%手续费

        // 如果是买入期权（开仓或平仓），需要检查余额
        if ("BUY".equals(side)) {
            BigDecimal requiredAmount = totalAmount.add(fee);
            // 检查钱包余额（使用USDT作为结算货币）
            BigDecimal balance = walletService.getAvailableBalance(userId, "SPOT", contract.getQuoteCurrency());
            if (balance.compareTo(requiredAmount) < 0) {
                throw new RuntimeException("余额不足");
            }
        }

        // 创建订单
        OptionOrder order = new OptionOrder();
        order.setUserId(userId);
        order.setContractId(contractId);
        order.setOrderType(orderType);
        order.setSide(side);
        order.setOptionType(contract.getOptionType());
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setTotalAmount(totalAmount);
        order.setFee(fee);
        order.setStatus("PENDING");
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setFilledAmount(BigDecimal.ZERO);

        return optionOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        OptionOrder order = optionOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        order.setStatus("CANCELLED");
        optionOrderRepository.save(order);
    }

    @Override
    public List<OptionOrder> getUserOrders(Long userId) {
        return optionOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public OptionOrder getOrder(Long userId, Long orderId) {
        OptionOrder order = optionOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此订单");
        }

        return order;
    }

    @Override
    @Transactional
    public OptionPosition executeOrder(Long orderId) {
        OptionOrder order = optionOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许执行");
        }

        OptionContract contract = optionContractRepository.findById(order.getContractId())
                .orElseThrow(() -> new RuntimeException("期权合约不存在"));

        // 更新订单状态
        order.setStatus("FILLED");
        order.setFilledQuantity(order.getQuantity());
        order.setFilledAmount(order.getTotalAmount());
        order.setFilledAt(LocalDateTime.now());
        optionOrderRepository.save(order);

        // 处理资金
        if ("BUY".equals(order.getSide())) {
            // 买入期权，扣除资金
            walletService.deductBalance(order.getUserId(), contract.getQuoteCurrency(), "SPOT",
                    order.getTotalAmount().add(order.getFee()));
        } else {
            // 卖出期权，增加资金
            walletService.addBalance(order.getUserId(), contract.getQuoteCurrency(), "SPOT",
                    order.getTotalAmount().subtract(order.getFee()));
        }

        // 如果是开仓订单，创建或更新持仓
        if ("OPEN".equals(order.getOrderType())) {
            return createOrUpdatePosition(order, contract);
        } else {
            // 平仓订单，更新持仓
            closePosition(order, contract);
            return null;
        }
    }

    @Override
    public List<OptionOrder> getOrderHistory(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return optionOrderRepository.findByUserIdAndCreatedAtBetween(userId, startTime, endTime);
    }

    /**
     * 创建或更新持仓
     */
    private OptionPosition createOrUpdatePosition(OptionOrder order, OptionContract contract) {
        String side = "BUY".equals(order.getSide()) ? "LONG" : "SHORT";

        Optional<OptionPosition> positionOpt = optionPositionRepository.findByUserIdAndContractIdAndSide(
                order.getUserId(), order.getContractId(), side);

        OptionPosition position;
        if (positionOpt.isPresent()) {
            position = positionOpt.get();
            // 更新平均价格和数量
            BigDecimal totalCost = position.getAveragePrice().multiply(position.getQuantity())
                    .add(order.getPrice().multiply(order.getQuantity()));
            BigDecimal totalQuantity = position.getQuantity().add(order.getQuantity());
            position.setAveragePrice(totalCost.divide(totalQuantity, 8, BigDecimal.ROUND_HALF_UP));
            position.setQuantity(totalQuantity);
        } else {
            position = new OptionPosition();
            position.setUserId(order.getUserId());
            position.setContractId(order.getContractId());
            position.setOptionType(contract.getOptionType());
            position.setSide(side);
            position.setQuantity(order.getQuantity());
            position.setAveragePrice(order.getPrice());
            position.setStrikePrice(contract.getStrikePrice());
            position.setExpiryDate(contract.getExpiryDate());
            position.setStatus("ACTIVE");
            position.setUnrealizedPnl(BigDecimal.ZERO);
            position.setRealizedPnl(BigDecimal.ZERO);
        }

        // 更新当前价格和标的资产价格
        position.setCurrentPrice(contract.getCurrentPrice());
        position.setUnderlyingPrice(contract.getUnderlyingPrice());

        // 计算保证金（期权费）
        position.setMargin(position.getAveragePrice().multiply(position.getQuantity()));

        return optionPositionRepository.save(position);
    }

    /**
     * 平仓
     */
    private void closePosition(OptionOrder order, OptionContract contract) {
        String oppositeSide = "BUY".equals(order.getSide()) ? "SHORT" : "LONG";

        Optional<OptionPosition> positionOpt = optionPositionRepository.findByUserIdAndContractIdAndSide(
                order.getUserId(), order.getContractId(), oppositeSide);

        if (!positionOpt.isPresent()) {
            throw new RuntimeException("持仓不存在，无法平仓");
        }

        OptionPosition position = positionOpt.get();

        if (position.getQuantity().compareTo(order.getQuantity()) < 0) {
            throw new RuntimeException("平仓数量超过持仓数量");
        }

        // 计算已实现盈亏
        BigDecimal pnl;
        if ("LONG".equals(position.getSide())) {
            // 持有看涨/看跌期权，平仓时卖出
            pnl = order.getPrice().subtract(position.getAveragePrice()).multiply(order.getQuantity());
        } else {
            // 卖出看涨/看跌期权，平仓时买入
            pnl = position.getAveragePrice().subtract(order.getPrice()).multiply(order.getQuantity());
        }

        // 更新持仓
        BigDecimal remainingQuantity = position.getQuantity().subtract(order.getQuantity());
        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            position.setStatus("CLOSED");
            position.setQuantity(BigDecimal.ZERO);
        } else {
            position.setQuantity(remainingQuantity);
        }

        position.setRealizedPnl(position.getRealizedPnl().add(pnl));
        optionPositionRepository.save(position);
    }
}




