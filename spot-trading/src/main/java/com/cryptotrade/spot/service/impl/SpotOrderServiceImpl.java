/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.spot.dto.response.OrderResponse;
import com.cryptotrade.spot.entity.SpotOrder;
import com.cryptotrade.spot.entity.TradingPair;
import com.cryptotrade.spot.repository.SpotOrderRepository;
import com.cryptotrade.spot.repository.TradingPairRepository;
import com.cryptotrade.spot.service.SpotOrderService;
import com.cryptotrade.spot.service.TradingFeeService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {

    @Autowired
    private SpotOrderRepository spotOrderRepository;

    @Autowired
    private TradingPairRepository tradingPairRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TradingFeeService tradingFeeService;

    @Override
    @Transactional
    public SpotOrder createOrder(Long userId, CreateOrderRequest request) {
        // 1. 验证交易对是否存在
        TradingPair tradingPair = tradingPairRepository.findByPairName(request.getPairName())
                .orElseThrow(() -> new RuntimeException("交易对不存在: " + request.getPairName()));

        if (!"ACTIVE".equals(tradingPair.getStatus())) {
            throw new RuntimeException("交易对已停用");
        }

        // 2. 验证订单参数
        validateOrderRequest(request);

        // 3. 解析交易对，获取基础货币和计价货币
        String[] pairParts = request.getPairName().split("/");
        String baseCurrency = pairParts[0]; // 如 BTC
        String quoteCurrency = pairParts[1]; // 如 USDT

        // 4. 验证用户余额
        BigDecimal requiredAmount;
        String currency;
        
        if ("BUY".equals(request.getSide())) {
            // 买入：需要USDT（计价货币）
            if ("MARKET".equals(request.getOrderType())) {
                // 市价单：需要估算金额（使用当前价格）
                BigDecimal currentPrice = tradingPair.getCurrentPrice();
                if (currentPrice == null) {
                    throw new RuntimeException("无法获取当前价格，请稍后重试");
                }
                requiredAmount = request.getQuantity().multiply(currentPrice);
            } else {
                // 限价单：使用指定价格
                requiredAmount = request.getQuantity().multiply(request.getPrice());
            }
            currency = quoteCurrency;
        } else {
            // 卖出：需要基础货币
            requiredAmount = request.getQuantity();
            currency = baseCurrency;
        }

        // 检查余额
        if (!walletService.checkBalance(userId, "SPOT", currency, requiredAmount)) {
            BigDecimal available = walletService.getAvailableBalance(userId, "SPOT", currency);
            throw new RuntimeException("余额不足，需要: " + requiredAmount + " " + currency + 
                    ", 可用: " + available + " " + currency);
        }

        // 5. 冻结余额
        walletService.freezeBalance(userId, "SPOT", currency, requiredAmount);

        // 6. 创建订单记录
        SpotOrder order = new SpotOrder();
        order.setUserId(userId);
        order.setPairName(request.getPairName());
        order.setOrderType(request.getOrderType());
        order.setSide(request.getSide());
        order.setPrice(request.getPrice());
        order.setQuantity(request.getQuantity());
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setStatus("PENDING");
        order.setFee(BigDecimal.ZERO);
        order.setStopPrice(request.getStopPrice());
        order.setAvgPrice(null);

        order = spotOrderRepository.save(order);

        // 7. 订单撮合处理
        try {
            matchOrders(order);
        } catch (Exception e) {
            // 如果撮合失败，解冻余额
            walletService.unfreezeBalance(userId, "SPOT", currency, requiredAmount);
            order.setStatus("CANCELLED");
            spotOrderRepository.save(order);
            throw new RuntimeException("订单创建失败: " + e.getMessage(), e);
        }

        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        SpotOrder order = spotOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("只能取消待成交状态的订单，当前状态: " + order.getStatus());
        }

        // 解析交易对
        String[] pairParts = order.getPairName().split("/");
        String baseCurrency = pairParts[0];
        String quoteCurrency = pairParts[1];

        // 计算需要解冻的金额（未成交部分）
        BigDecimal remainingQuantity = order.getQuantity().subtract(order.getFilledQuantity());
        BigDecimal frozenAmount;
        String currency;
        
        if ("BUY".equals(order.getSide())) {
            if (order.getPrice() != null) {
                frozenAmount = remainingQuantity.multiply(order.getPrice());
            } else {
                // 市价单，使用当前价格估算
                TradingPair tradingPair = tradingPairRepository.findByPairName(order.getPairName())
                        .orElseThrow(() -> new RuntimeException("交易对不存在"));
                frozenAmount = remainingQuantity.multiply(tradingPair.getCurrentPrice());
            }
            currency = quoteCurrency;
        } else {
            frozenAmount = remainingQuantity;
            currency = baseCurrency;
        }

        // 解冻余额
        walletService.unfreezeBalance(userId, "SPOT", currency, frozenAmount);

        // 更新订单状态
        order.setStatus("CANCELLED");
        spotOrderRepository.save(order);
    }

    @Override
    public OrderResponse getOrderStatus(Long userId, Long orderId) {
        SpotOrder order = spotOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此订单");
        }

        return convertToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrderHistory(Long userId, Pageable pageable) {
        List<SpotOrder> orders = spotOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        // 手动分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), orders.size());
        List<SpotOrder> pagedOrders = orders.subList(start, end);
        
        List<OrderResponse> responses = pagedOrders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, orders.size());
    }

    @Override
    @Transactional
    public void matchOrders(SpotOrder order) {
        if (!"PENDING".equals(order.getStatus())) {
            return;
        }

        String pairName = order.getPairName();
        String side = order.getSide();
        String orderType = order.getOrderType();

        if ("MARKET".equals(orderType)) {
            // 市价单：立即撮合
            matchMarketOrder(order);
        } else if ("LIMIT".equals(orderType)) {
            // 限价单：加入订单簿等待撮合
            matchLimitOrder(order);
        } else if ("STOP_LOSS".equals(orderType) || "TAKE_PROFIT".equals(orderType)) {
            // 止损单/止盈单：检查价格条件
            checkConditionalOrder(order);
        }
    }

    @Override
    @Transactional
    public void settleOrder(SpotOrder order, BigDecimal filledQuantity, BigDecimal avgPrice) {
        String[] pairParts = order.getPairName().split("/");
        String baseCurrency = pairParts[0];
        String quoteCurrency = pairParts[1];

        // 计算实际使用的金额
        BigDecimal actualAmount = filledQuantity.multiply(avgPrice);
        
        // 计算手续费
        BigDecimal fee = tradingFeeService.calculateFee(order.getUserId(), order.getPairName(), 
                filledQuantity, avgPrice, order.getSide());

        if ("BUY".equals(order.getSide())) {
            // 买入：扣除USDT，增加基础货币
            // 解冻USDT
            walletService.unfreezeBalance(order.getUserId(), "SPOT", quoteCurrency, actualAmount);
            // 扣除实际使用的USDT（包含手续费）
            walletService.deductBalance(order.getUserId(), "SPOT", quoteCurrency, actualAmount.add(fee));
            
            // 增加基础货币
            walletService.addBalance(order.getUserId(), "SPOT", baseCurrency, filledQuantity);
        } else {
            // 卖出：扣除基础货币，增加USDT
            // 解冻并扣除基础货币
            walletService.unfreezeBalance(order.getUserId(), "SPOT", baseCurrency, filledQuantity);
            walletService.deductBalance(order.getUserId(), "SPOT", baseCurrency, filledQuantity);
            
            // 增加USDT（扣除手续费）
            walletService.addBalance(order.getUserId(), "SPOT", quoteCurrency, 
                    actualAmount.subtract(fee));
        }

        // 更新订单
        order.setFilledQuantity(filledQuantity);
        order.setAvgPrice(avgPrice);
        order.setFee(fee);
        
        if (filledQuantity.compareTo(order.getQuantity()) >= 0) {
            order.setStatus("FILLED");
        } else {
            order.setStatus("PARTIAL_FILLED");
        }
        
        spotOrderRepository.save(order);
    }

    /**
     * 验证订单请求
     */
    private void validateOrderRequest(CreateOrderRequest request) {
        if ("LIMIT".equals(request.getOrderType()) && request.getPrice() == null) {
            throw new RuntimeException("限价单必须指定价格");
        }

        if (("STOP_LOSS".equals(request.getOrderType()) || "TAKE_PROFIT".equals(request.getOrderType())) 
                && request.getStopPrice() == null) {
            throw new RuntimeException("止损单/止盈单必须指定止损价格");
        }

        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        if (request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("价格必须大于0");
        }
    }

    /**
     * 撮合市价单
     */
    private void matchMarketOrder(SpotOrder order) {
        // 获取对手盘订单（价格优先、时间优先）
        List<SpotOrder> oppositeOrders = getOppositeOrders(order);
        
        if (oppositeOrders.isEmpty()) {
            // 没有对手盘，订单保持PENDING状态
            return;
        }

        BigDecimal remainingQuantity = order.getQuantity().subtract(order.getFilledQuantity());
        BigDecimal totalFilled = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SpotOrder oppositeOrder : oppositeOrders) {
            if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal oppositeRemaining = oppositeOrder.getQuantity()
                    .subtract(oppositeOrder.getFilledQuantity());
            BigDecimal fillQuantity = remainingQuantity.min(oppositeRemaining);
            BigDecimal fillPrice = oppositeOrder.getPrice();

            // 结算对手盘订单
            settleOrder(oppositeOrder, 
                    oppositeOrder.getFilledQuantity().add(fillQuantity),
                    calculateAvgPrice(oppositeOrder, fillQuantity, fillPrice));

            totalFilled = totalFilled.add(fillQuantity);
            totalAmount = totalAmount.add(fillQuantity.multiply(fillPrice));
            remainingQuantity = remainingQuantity.subtract(fillQuantity);
        }

        if (totalFilled.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgPrice = totalAmount.divide(totalFilled, 8, RoundingMode.HALF_UP);
            settleOrder(order, totalFilled, avgPrice);
        }
    }

    /**
     * 撮合限价单
     */
    private void matchLimitOrder(SpotOrder order) {
        // 获取可匹配的对手盘订单
        List<SpotOrder> matchableOrders = getMatchableOrders(order);
        
        if (matchableOrders.isEmpty()) {
            // 没有可匹配的订单，加入订单簿等待
            return;
        }

        BigDecimal remainingQuantity = order.getQuantity().subtract(order.getFilledQuantity());
        BigDecimal totalFilled = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SpotOrder matchOrder : matchableOrders) {
            if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            // 检查价格是否匹配
            if (!isPriceMatched(order, matchOrder)) {
                continue;
            }

            BigDecimal matchRemaining = matchOrder.getQuantity()
                    .subtract(matchOrder.getFilledQuantity());
            BigDecimal fillQuantity = remainingQuantity.min(matchRemaining);
            BigDecimal fillPrice = matchOrder.getPrice();

            // 结算对手盘订单
            settleOrder(matchOrder,
                    matchOrder.getFilledQuantity().add(fillQuantity),
                    calculateAvgPrice(matchOrder, fillQuantity, fillPrice));

            totalFilled = totalFilled.add(fillQuantity);
            totalAmount = totalAmount.add(fillQuantity.multiply(fillPrice));
            remainingQuantity = remainingQuantity.subtract(fillQuantity);
        }

        if (totalFilled.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgPrice = totalAmount.divide(totalFilled, 8, RoundingMode.HALF_UP);
            settleOrder(order, totalFilled, avgPrice);
        }
    }

    /**
     * 检查条件单（止损单/止盈单）
     */
    private void checkConditionalOrder(SpotOrder order) {
        TradingPair tradingPair = tradingPairRepository.findByPairName(order.getPairName())
                .orElseThrow(() -> new RuntimeException("交易对不存在"));

        BigDecimal currentPrice = tradingPair.getCurrentPrice();
        if (currentPrice == null) {
            return;
        }

        boolean shouldTrigger = false;

        if ("STOP_LOSS".equals(order.getOrderType())) {
            if ("BUY".equals(order.getSide())) {
                // 买入止损：价格跌至止损价时触发
                shouldTrigger = currentPrice.compareTo(order.getStopPrice()) <= 0;
            } else {
                // 卖出止损：价格涨至止损价时触发
                shouldTrigger = currentPrice.compareTo(order.getStopPrice()) >= 0;
            }
        } else if ("TAKE_PROFIT".equals(order.getOrderType())) {
            if ("BUY".equals(order.getSide())) {
                // 买入止盈：价格涨至止盈价时触发
                shouldTrigger = currentPrice.compareTo(order.getStopPrice()) >= 0;
            } else {
                // 卖出止盈：价格跌至止盈价时触发
                shouldTrigger = currentPrice.compareTo(order.getStopPrice()) <= 0;
            }
        }

        if (shouldTrigger) {
            // 触发条件单，转为限价单进行撮合
            order.setOrderType("LIMIT");
            order.setPrice(order.getStopPrice());
            matchLimitOrder(order);
        }
    }

    /**
     * 获取对手盘订单
     */
    private List<SpotOrder> getOppositeOrders(SpotOrder order) {
        String oppositeSide = "BUY".equals(order.getSide()) ? "SELL" : "BUY";
        String status = "PENDING";
        
        if ("BUY".equals(order.getSide())) {
            // 买单：找卖单，价格从低到高
            return spotOrderRepository.findByPairNameAndStatusAndPriceLessThanEqualOrderByPriceAsc(
                    order.getPairName(), status, order.getPrice() != null ? order.getPrice() : 
                    new java.math.BigDecimal("999999999"));
        } else {
            // 卖单：找买单，价格从高到低
            return spotOrderRepository.findByPairNameAndStatusAndPriceGreaterThanEqualOrderByPriceDesc(
                    order.getPairName(), status, BigDecimal.ZERO);
        }
    }

    /**
     * 获取可匹配的订单
     */
    private List<SpotOrder> getMatchableOrders(SpotOrder order) {
        return getOppositeOrders(order);
    }

    /**
     * 检查价格是否匹配
     */
    private boolean isPriceMatched(SpotOrder order1, SpotOrder order2) {
        if (order1.getPrice() == null || order2.getPrice() == null) {
            return false;
        }

        if ("BUY".equals(order1.getSide())) {
            // 买单：订单价格 >= 对手盘价格
            return order1.getPrice().compareTo(order2.getPrice()) >= 0;
        } else {
            // 卖单：订单价格 <= 对手盘价格
            return order1.getPrice().compareTo(order2.getPrice()) <= 0;
        }
    }

    /**
     * 计算平均价格
     */
    private BigDecimal calculateAvgPrice(SpotOrder order, BigDecimal newFillQuantity, BigDecimal newFillPrice) {
        BigDecimal oldFilled = order.getFilledQuantity();
        BigDecimal oldAmount = oldFilled.multiply(order.getAvgPrice() != null ? 
                order.getAvgPrice() : BigDecimal.ZERO);
        BigDecimal newAmount = newFillQuantity.multiply(newFillPrice);
        
        BigDecimal totalFilled = oldFilled.add(newFillQuantity);
        BigDecimal totalAmount = oldAmount.add(newAmount);
        
        if (totalFilled.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return totalAmount.divide(totalFilled, 8, RoundingMode.HALF_UP);
    }

    /**
     * 转换为OrderResponse
     */
    private OrderResponse convertToOrderResponse(SpotOrder order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setPairName(order.getPairName());
        response.setOrderType(order.getOrderType());
        response.setSide(order.getSide());
        response.setPrice(order.getPrice());
        response.setQuantity(order.getQuantity());
        response.setFilledQuantity(order.getFilledQuantity());
        response.setAvgPrice(order.getAvgPrice());
        response.setStatus(order.getStatus());
        response.setFee(order.getFee());
        response.setStopPrice(order.getStopPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}


