/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service.impl;

import com.cryptotrade.deliverycontract.entity.*;
import com.cryptotrade.deliverycontract.repository.*;
import com.cryptotrade.deliverycontract.service.DeliveryContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交割合约服务实现
 */
@Service
public class DeliveryContractServiceImpl implements DeliveryContractService {
    @Autowired
    private DeliveryContractRepository contractRepository;

    @Autowired
    private DeliveryOrderRepository orderRepository;

    @Autowired
    private DeliveryPositionRepository positionRepository;

    @Autowired
    private DeliveryTradeRepository tradeRepository;

    @Autowired
    private DeliverySettlementRepository settlementRepository;

    @Autowired
    private DeliveryMarketDataRepository marketDataRepository;

    @Override
    @Transactional
    public DeliveryOrder createOrder(Long userId, Long contractId, DeliveryOrder order) {
        DeliveryContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("合约未启用");
        }

        // 生成订单号
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setContractId(contractId);
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // 计算保证金
        BigDecimal margin = calculateMargin(order.getQuantity(), order.getPrice(), order.getLeverage(), contract);
        order.setMargin(margin);

        // 检查保证金是否足够（这里应该调用钱包服务）
        // walletService.checkBalance(userId, margin);

        order = orderRepository.save(order);

        // 尝试撮合订单
        matchOrder(order);

        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        if (!"PENDING".equals(order.getStatus()) && !"PARTIAL".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public DeliveryOrder modifyOrder(Long userId, Long orderId, BigDecimal price) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许修改");
        }

        if (!"LIMIT".equals(order.getPriceType())) {
            throw new RuntimeException("只有限价单可以修改价格");
        }

        order.setPrice(price);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<DeliveryOrder> getOrders(Long userId, String status) {
        if (status != null) {
            return orderRepository.findByUserIdAndStatus(userId, status);
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<DeliveryPosition> getPositions(Long userId) {
        return positionRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    @Override
    @Transactional
    public DeliveryOrder closePosition(Long userId, Long positionId, BigDecimal quantity) {
        DeliveryPosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("持仓不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该持仓");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            throw new RuntimeException("持仓状态异常");
        }

        // 创建平仓订单
        DeliveryOrder closeOrder = new DeliveryOrder();
        closeOrder.setOrderNo(generateOrderNo());
        closeOrder.setUserId(userId);
        closeOrder.setContractId(position.getContractId());
        closeOrder.setOrderType("CLOSE");
        closeOrder.setSide(position.getSide().equals("LONG") ? "SELL" : "BUY");
        closeOrder.setPriceType("MARKET");
        closeOrder.setQuantity(quantity != null ? quantity : position.getQuantity());
        closeOrder.setFilledQuantity(BigDecimal.ZERO);
        closeOrder.setLeverage(position.getLeverage());
        closeOrder.setMargin(BigDecimal.ZERO);
        closeOrder.setStatus("PENDING");
        closeOrder.setCreatedAt(LocalDateTime.now());
        closeOrder.setUpdatedAt(LocalDateTime.now());

        closeOrder = orderRepository.save(closeOrder);

        // 尝试撮合
        matchOrder(closeOrder);

        return closeOrder;
    }

    @Override
    public DeliveryMarketData getMarketData(Long contractId) {
        return marketDataRepository.findFirstByContractIdOrderByCreatedAtDesc(contractId)
                .orElseThrow(() -> new RuntimeException("行情数据不存在"));
    }

    @Override
    @Transactional
    public void checkLiquidation(Long userId, Long positionId) {
        DeliveryPosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("持仓不存在"));

        // 获取当前价格
        DeliveryMarketData marketData = getMarketData(position.getContractId());
        BigDecimal currentPrice = marketData.getPrice();

        // 计算强平价格
        BigDecimal liquidationPrice = calculateLiquidationPrice(position, currentPrice);

        // 检查是否需要强平
        if (shouldLiquidate(position, currentPrice, liquidationPrice)) {
            // 执行强平
            liquidatePosition(position, currentPrice);
        }
    }

    @Override
    @Transactional
    public DeliverySettlement settleContract(Long contractId, Long positionId) {
        DeliveryContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        DeliveryPosition position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("持仓不存在"));

        // 获取结算价格
        BigDecimal settlementPrice = contract.getSettlementPrice();
        if (settlementPrice == null) {
            DeliveryMarketData marketData = getMarketData(contractId);
            settlementPrice = marketData.getPrice();
        }

        // 计算结算盈亏
        BigDecimal settlementPnl = calculateSettlementPnl(position, settlementPrice);

        // 创建结算记录
        DeliverySettlement settlement = new DeliverySettlement();
        settlement.setSettlementNo(generateSettlementNo());
        settlement.setContractId(contractId);
        settlement.setUserId(position.getUserId());
        settlement.setPositionId(positionId);
        settlement.setSettlementPrice(settlementPrice);
        settlement.setSettlementPnl(settlementPnl);
        settlement.setSettlementType("DELIVERY");
        settlement.setStatus("PENDING");
        settlement.setCreatedAt(LocalDateTime.now());
        settlement.setUpdatedAt(LocalDateTime.now());

        settlement = settlementRepository.save(settlement);

        // 结算到用户账户（调用钱包服务）
        // walletService.addBalance(position.getUserId(), contract.getQuoteCurrency(), settlementPnl);

        settlement.setStatus("SETTLED");
        settlement.setSettledAt(LocalDateTime.now());
        settlement.setUpdatedAt(LocalDateTime.now());
        settlementRepository.save(settlement);

        // 更新持仓状态
        position.setStatus("CLOSED");
        position.setClosedAt(LocalDateTime.now());
        position.setUpdatedAt(LocalDateTime.now());
        positionRepository.save(position);

        return settlement;
    }

    // ==================== 私有方法 ====================

    private String generateOrderNo() {
        return "DC" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private String generateSettlementNo() {
        return "DS" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private BigDecimal calculateMargin(BigDecimal quantity, BigDecimal price, BigDecimal leverage, DeliveryContract contract) {
        BigDecimal notional = quantity.multiply(price);
        return notional.divide(leverage, 8, BigDecimal.ROUND_HALF_UP)
                .multiply(contract.getInitialMarginRate())
                .divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP);
    }

    private void matchOrder(DeliveryOrder order) {
        // 查找匹配的对手订单
        String oppositeSide = order.getSide().equals("BUY") ? "SELL" : "BUY";
        List<DeliveryOrder> oppositeOrders = orderRepository.findByContractIdAndStatusAndPriceType(
                order.getContractId(), "PENDING", "LIMIT");

        for (DeliveryOrder oppositeOrder : oppositeOrders) {
            if (!oppositeOrder.getSide().equals(oppositeSide)) {
                continue;
            }

            // 检查价格是否匹配
            if (order.getPriceType().equals("MARKET") || 
                (order.getPrice() != null && oppositeOrder.getPrice() != null &&
                 order.getPrice().compareTo(oppositeOrder.getPrice()) >= 0)) {
                
                // 执行撮合
                executeTrade(order, oppositeOrder);
                
                if (order.getFilledQuantity().compareTo(order.getQuantity()) >= 0) {
                    break;
                }
            }
        }
    }

    private void executeTrade(DeliveryOrder buyOrder, DeliveryOrder sellOrder) {
        BigDecimal tradeQuantity = buyOrder.getQuantity().subtract(buyOrder.getFilledQuantity())
                .min(sellOrder.getQuantity().subtract(sellOrder.getFilledQuantity()));
        BigDecimal tradePrice = sellOrder.getPrice(); // 使用卖方价格

        // 创建成交记录
        DeliveryTrade trade = new DeliveryTrade();
        trade.setTradeNo(generateTradeNo());
        trade.setContractId(buyOrder.getContractId());
        trade.setBuyOrderId(buyOrder.getId());
        trade.setSellOrderId(sellOrder.getId());
        trade.setBuyerId(buyOrder.getUserId());
        trade.setSellerId(sellOrder.getUserId());
        trade.setPrice(tradePrice);
        trade.setQuantity(tradeQuantity);
        trade.setFee(calculateFee(tradeQuantity, tradePrice));
        trade.setFeeCurrency("USDT");
        trade.setCreatedAt(LocalDateTime.now());
        tradeRepository.save(trade);

        // 更新订单
        buyOrder.setFilledQuantity(buyOrder.getFilledQuantity().add(tradeQuantity));
        sellOrder.setFilledQuantity(sellOrder.getFilledQuantity().add(tradeQuantity));

        if (buyOrder.getFilledQuantity().compareTo(buyOrder.getQuantity()) >= 0) {
            buyOrder.setStatus("FILLED");
            buyOrder.setFilledAt(LocalDateTime.now());
        } else {
            buyOrder.setStatus("PARTIAL");
        }

        if (sellOrder.getFilledQuantity().compareTo(sellOrder.getQuantity()) >= 0) {
            sellOrder.setStatus("FILLED");
            sellOrder.setFilledAt(LocalDateTime.now());
        } else {
            sellOrder.setStatus("PARTIAL");
        }

        buyOrder.setUpdatedAt(LocalDateTime.now());
        sellOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);

        // 更新或创建持仓
        updatePosition(buyOrder, sellOrder, tradeQuantity, tradePrice);
    }

    private void updatePosition(DeliveryOrder buyOrder, DeliveryOrder sellOrder, BigDecimal quantity, BigDecimal price) {
        // 更新买方持仓（做多）
        if ("OPEN".equals(buyOrder.getOrderType()) && "BUY".equals(buyOrder.getSide())) {
            updateOrCreatePosition(buyOrder.getUserId(), buyOrder.getContractId(), "LONG", quantity, price, buyOrder.getLeverage());
        }

        // 更新卖方持仓（做空）
        if ("OPEN".equals(sellOrder.getOrderType()) && "SELL".equals(sellOrder.getSide())) {
            updateOrCreatePosition(sellOrder.getUserId(), sellOrder.getContractId(), "SHORT", quantity, price, sellOrder.getLeverage());
        }
    }

    private void updateOrCreatePosition(Long userId, Long contractId, String side, BigDecimal quantity, BigDecimal price, BigDecimal leverage) {
        DeliveryPosition position = positionRepository.findByUserIdAndContractIdAndSideAndStatus(
                userId, contractId, side, "ACTIVE").orElse(null);

        if (position == null) {
            position = new DeliveryPosition();
            position.setUserId(userId);
            position.setContractId(contractId);
            position.setSide(side);
            position.setQuantity(quantity);
            position.setAvgOpenPrice(price);
            position.setCurrentPrice(price);
            position.setLeverage(leverage);
            position.setMargin(calculatePositionMargin(quantity, price, leverage));
            position.setMaintenanceMargin(calculateMaintenanceMargin(quantity, price, leverage));
            position.setUnrealizedPnl(BigDecimal.ZERO);
            position.setRealizedPnl(BigDecimal.ZERO);
            position.setStatus("ACTIVE");
            position.setOpenedAt(LocalDateTime.now());
            position.setCreatedAt(LocalDateTime.now());
            position.setUpdatedAt(LocalDateTime.now());
        } else {
            // 更新持仓
            BigDecimal totalValue = position.getQuantity().multiply(position.getAvgOpenPrice())
                    .add(quantity.multiply(price));
            BigDecimal totalQuantity = position.getQuantity().add(quantity);
            position.setAvgOpenPrice(totalValue.divide(totalQuantity, 8, BigDecimal.ROUND_HALF_UP));
            position.setQuantity(totalQuantity);
            position.setMargin(position.getMargin().add(calculatePositionMargin(quantity, price, leverage)));
            position.setUpdatedAt(LocalDateTime.now());
        }

        positionRepository.save(position);
    }

    private BigDecimal calculatePositionMargin(BigDecimal quantity, BigDecimal price, BigDecimal leverage) {
        return quantity.multiply(price).divide(leverage, 8, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateMaintenanceMargin(BigDecimal quantity, BigDecimal price, BigDecimal leverage) {
        return calculatePositionMargin(quantity, price, leverage).multiply(new BigDecimal("0.5"));
    }

    private BigDecimal calculateLiquidationPrice(DeliveryPosition position, BigDecimal currentPrice) {
        // 简化计算
        BigDecimal marginRatio = position.getMargin().divide(
                position.getQuantity().multiply(currentPrice), 8, BigDecimal.ROUND_HALF_UP);
        if (position.getSide().equals("LONG")) {
            return currentPrice.multiply(BigDecimal.ONE.subtract(marginRatio));
        } else {
            return currentPrice.multiply(BigDecimal.ONE.add(marginRatio));
        }
    }

    private boolean shouldLiquidate(DeliveryPosition position, BigDecimal currentPrice, BigDecimal liquidationPrice) {
        if (position.getSide().equals("LONG")) {
            return currentPrice.compareTo(liquidationPrice) <= 0;
        } else {
            return currentPrice.compareTo(liquidationPrice) >= 0;
        }
    }

    private void liquidatePosition(DeliveryPosition position, BigDecimal currentPrice) {
        // 执行强平
        position.setStatus("LIQUIDATED");
        position.setClosedAt(LocalDateTime.now());
        position.setCurrentPrice(currentPrice);
        position.setUpdatedAt(LocalDateTime.now());
        positionRepository.save(position);
    }

    private BigDecimal calculateSettlementPnl(DeliveryPosition position, BigDecimal settlementPrice) {
        if (position.getSide().equals("LONG")) {
            return position.getQuantity().multiply(settlementPrice.subtract(position.getAvgOpenPrice()));
        } else {
            return position.getQuantity().multiply(position.getAvgOpenPrice().subtract(settlementPrice));
        }
    }

    private BigDecimal calculateFee(BigDecimal quantity, BigDecimal price) {
        return quantity.multiply(price).multiply(new BigDecimal("0.001")); // 0.1% 手续费
    }

    private String generateTradeNo() {
        return "DT" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}















