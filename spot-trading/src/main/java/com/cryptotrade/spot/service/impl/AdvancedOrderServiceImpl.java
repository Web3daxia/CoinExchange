/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.dto.request.AdvancedOrderRequest;
import com.cryptotrade.spot.entity.AdvancedOrder;
import com.cryptotrade.spot.repository.AdvancedOrderRepository;
import com.cryptotrade.spot.service.AdvancedOrderService;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.spot.service.SpotOrderService;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvancedOrderServiceImpl implements AdvancedOrderService {

    @Autowired
    private AdvancedOrderRepository advancedOrderRepository;

    @Autowired
    private SpotOrderService spotOrderService;

    @Autowired
    private MarketDataService marketDataService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public AdvancedOrder createAdvancedOrder(Long userId, AdvancedOrderRequest request) {
        AdvancedOrder order = new AdvancedOrder();
        order.setUserId(userId);
        order.setPairName(request.getPairName());
        order.setOrderType(request.getOrderType());
        order.setSide(request.getSide());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStatus("PENDING");
        
        // 根据订单类型设置特定参数
        if ("ADVANCED_LIMIT".equals(request.getOrderType())) {
            order.setTimeInForce(request.getTimeInForce());
            order.setExpireTime(request.getExpireTime());
        } else if ("TIME_WEIGHTED".equals(request.getOrderType())) {
            order.setTimeInterval(request.getTimeInterval());
            order.setStartTime(request.getStartTime());
            order.setEndTime(request.getEndTime());
        } else if ("RECURRING".equals(request.getOrderType())) {
            order.setRecurringPeriod(request.getRecurringPeriod());
            order.setRecurringAmount(request.getRecurringAmount());
            // nextExecutionTime will be set when the order is processed
        } else if ("TRAILING".equals(request.getOrderType())) {
            order.setTrailingDistance(request.getTrailingDistance());
            order.setStopLossPrice(request.getStopLossPrice());
            order.setTakeProfitPrice(request.getTakeProfitPrice());
        } else if ("ICEBERG".equals(request.getOrderType())) {
            order.setIcebergTotalQuantity(request.getIcebergTotalQuantity());
            order.setIcebergDisplayQuantity(request.getIcebergDisplayQuantity());
            order.setIcebergFilledQuantity(BigDecimal.ZERO);
        }

        return advancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelAdvancedOrder(Long userId, Long orderId) {
        AdvancedOrder order = advancedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("高级订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus()) && !"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("只能取消待执行或激活状态的订单");
        }

        order.setStatus("CANCELLED");
        advancedOrderRepository.save(order);
    }

    @Override
    public List<AdvancedOrder> getUserAdvancedOrders(Long userId) {
        return advancedOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void executeAdvancedOrders() {
        // 查找所有待执行和激活状态的高级订单
        List<String> statuses = java.util.Arrays.asList("PENDING", "ACTIVE");
        List<AdvancedOrder> ordersToExecute = advancedOrderRepository.findByStatusIn(statuses);

        // 执行所有需要处理的订单
        for (AdvancedOrder order : ordersToExecute) {
            try {
                switch (order.getOrderType()) {
                    case "ADVANCED_LIMIT":
                        executeAdvancedLimitOrder(order);
                        break;
                    case "TIME_WEIGHTED":
                        executeTimeWeightedOrder(order);
                        break;
                    case "RECURRING":
                        executeRecurringOrder(order);
                        break;
                    case "TRAILING":
                        executeTrailingOrder(order);
                        break;
                    case "ICEBERG":
                        executeIcebergOrder(order);
                        break;
                }
            } catch (Exception e) {
                // 记录错误，继续处理下一个订单
                System.err.println("执行高级订单失败: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void executeAdvancedLimitOrder(AdvancedOrder order) {
        // 检查订单是否过期
        if (order.getExpireTime() != null && LocalDateTime.now().isAfter(order.getExpireTime())) {
            order.setStatus("EXPIRED");
            advancedOrderRepository.save(order);
            return;
        }

        // 根据timeInForce执行
        CreateOrderRequest request = new CreateOrderRequest();
        request.setPairName(order.getPairName());
        request.setSide(order.getSide());
        request.setQuantity(order.getQuantity());
        request.setPrice(order.getPrice());

        if ("IOC".equals(order.getTimeInForce())) {
            // Immediate-Or-Cancel: 立即成交，未成交部分取消
            request.setOrderType("MARKET");
        } else if ("FOK".equals(order.getTimeInForce())) {
            // Fill-Or-Kill: 全部成交或全部取消
            request.setOrderType("MARKET");
        } else {
            // GTC: Good-Til-Canceled
            request.setOrderType("LIMIT");
        }

        try {
            spotOrderService.createOrder(order.getUserId(), request);
            if ("IOC".equals(order.getTimeInForce()) || "FOK".equals(order.getTimeInForce())) {
                order.setStatus("EXECUTED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            order.setStatus("FAILED");
        }

        advancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTimeWeightedOrder(AdvancedOrder order) {
        LocalDateTime now = LocalDateTime.now();

        // 检查是否在时间范围内
        if (order.getStartTime() != null && now.isBefore(order.getStartTime())) {
            return; // 还未到开始时间
        }

        if (order.getEndTime() != null && now.isAfter(order.getEndTime())) {
            order.setStatus("EXPIRED");
            advancedOrderRepository.save(order);
            return;
        }

        // 计算剩余数量
        BigDecimal filledQuantity = order.getFilledQuantity() != null ? order.getFilledQuantity() : BigDecimal.ZERO;
        BigDecimal remainingQuantity = order.getQuantity().subtract(filledQuantity);
        
        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("FILLED");
            advancedOrderRepository.save(order);
            return;
        }

        // 检查是否需要执行（根据时间间隔判断）
        // 这里需要检查上次执行时间，避免频繁执行
        // 简化处理：如果状态是PENDING，立即执行；如果是ACTIVE，需要检查时间间隔
        if ("ACTIVE".equals(order.getStatus())) {
            // 检查上次更新时间，如果距离现在的时间小于时间间隔，则不执行
            if (order.getUpdatedAt() != null && order.getTimeInterval() != null) {
                Duration duration = Duration.between(order.getUpdatedAt(), now);
                if (duration.getSeconds() < order.getTimeInterval()) {
                    return; // 还未到执行时间
                }
            }
        }

        // 计算每次下单的数量
        BigDecimal orderQuantity;
        if (order.getTimeInterval() != null && order.getStartTime() != null && order.getEndTime() != null) {
            // 根据时间间隔和剩余时间计算每次下单数量
            long totalSeconds = Duration.between(order.getStartTime(), order.getEndTime()).getSeconds();
            long intervals = totalSeconds / order.getTimeInterval();
            if (intervals > 0) {
                orderQuantity = remainingQuantity.divide(new BigDecimal(intervals), 8, RoundingMode.DOWN);
            } else {
                orderQuantity = remainingQuantity;
            }
        } else {
            // 简化处理：每次下单固定比例（10%）
            orderQuantity = remainingQuantity.multiply(new BigDecimal("0.1"));
        }
        
        // 确保订单数量不为零且不超过剩余数量
        if (orderQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            orderQuantity = remainingQuantity; // 如果计算出的数量太小，直接使用剩余数量
        }
        orderQuantity = orderQuantity.min(remainingQuantity);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setPairName(order.getPairName());
        request.setSide(order.getSide());
        request.setQuantity(orderQuantity);
        request.setPrice(order.getPrice());
        request.setOrderType("LIMIT");

        try {
            spotOrderService.createOrder(order.getUserId(), request);
            
            // 更新已执行数量
            BigDecimal newFilledQuantity = filledQuantity.add(orderQuantity);
            order.setFilledQuantity(newFilledQuantity);
            
            // 检查是否已完成
            if (newFilledQuantity.compareTo(order.getQuantity()) >= 0) {
                order.setStatus("FILLED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            // 记录错误
            System.err.println("分时委托执行失败: " + e.getMessage());
            // 如果订单状态是PENDING，标记为失败；如果是ACTIVE，保持激活状态
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
        }

        advancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeRecurringOrder(AdvancedOrder order) {
        LocalDateTime now = LocalDateTime.now();

        // 首次执行：如果nextExecutionTime为null，设置初始执行时间
        if (order.getNextExecutionTime() == null && "PENDING".equals(order.getStatus())) {
            LocalDateTime firstExecutionTime = calculateNextExecutionTime(now, order.getRecurringPeriod());
            order.setNextExecutionTime(firstExecutionTime);
            order.setStatus("ACTIVE");
            advancedOrderRepository.save(order);
            return;
        }

        // 检查是否到了执行时间
        if (order.getNextExecutionTime() == null || now.isBefore(order.getNextExecutionTime())) {
            return;
        }

        // 创建订单
        CreateOrderRequest request = new CreateOrderRequest();
        request.setPairName(order.getPairName());
        request.setSide(order.getSide());
        request.setQuantity(order.getRecurringAmount() != null ? order.getRecurringAmount() : order.getQuantity());
        
        // 如果价格为空，使用市价单
        if (order.getPrice() != null) {
            request.setPrice(order.getPrice());
            request.setOrderType("LIMIT");
        } else {
            request.setOrderType("MARKET");
        }

        try {
            spotOrderService.createOrder(order.getUserId(), request);

            // 计算下次执行时间
            LocalDateTime nextTime = calculateNextExecutionTime(now, order.getRecurringPeriod());
            order.setNextExecutionTime(nextTime);
            order.setStatus("ACTIVE");
        } catch (Exception e) {
            // 如果订单状态是PENDING，标记为失败；如果是ACTIVE，保持激活状态等待下次执行
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
            System.err.println("循环委托执行失败: " + e.getMessage());
        }

        advancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTrailingOrder(AdvancedOrder order) {
        try {
            // 获取当前市场价格
            BigDecimal currentPrice = marketDataService.getMarketData(order.getPairName()).getCurrentPrice();
            if (currentPrice == null) {
                return; // 无法获取价格，等待下次执行
            }

            BigDecimal trailingDistance = order.getTrailingDistance() != null ? 
                    order.getTrailingDistance() : new BigDecimal("0.01"); // 默认1%
            
            BigDecimal trailingPrice = order.getTrailingPrice();
            boolean shouldCreateOrder = false;
            BigDecimal newPrice = null;

            if ("BUY".equals(order.getSide())) {
                // 买入追踪：价格下跌时，买入价格跟随下跌
                if (trailingPrice == null) {
                    // 首次设置，使用当前价格减去追踪距离
                    newPrice = currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"))));
                    order.setTrailingPrice(newPrice);
                    shouldCreateOrder = true;
                } else {
                    // 如果当前价格比追踪价格更低，更新追踪价格
                    BigDecimal newTrailingPrice = currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"))));
                    if (newTrailingPrice.compareTo(trailingPrice) < 0) {
                        order.setTrailingPrice(newTrailingPrice);
                        newPrice = newTrailingPrice;
                        shouldCreateOrder = true;
                    }
                }
            } else {
                // 卖出追踪：价格上涨时，卖出价格跟随上涨
                if (trailingPrice == null) {
                    // 首次设置，使用当前价格加上追踪距离
                    newPrice = currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"))));
                    order.setTrailingPrice(newPrice);
                    shouldCreateOrder = true;
                } else {
                    // 如果当前价格比追踪价格更高，更新追踪价格
                    BigDecimal newTrailingPrice = currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"))));
                    if (newTrailingPrice.compareTo(trailingPrice) > 0) {
                        order.setTrailingPrice(newTrailingPrice);
                        newPrice = newTrailingPrice;
                        shouldCreateOrder = true;
                    }
                }
            }

            // 检查止损和止盈
            if (order.getStopLossPrice() != null) {
                if ("BUY".equals(order.getSide()) && currentPrice.compareTo(order.getStopLossPrice()) <= 0) {
                    order.setStatus("STOPPED");
                    advancedOrderRepository.save(order);
                    return;
                } else if ("SELL".equals(order.getSide()) && currentPrice.compareTo(order.getStopLossPrice()) >= 0) {
                    order.setStatus("STOPPED");
                    advancedOrderRepository.save(order);
                    return;
                }
            }

            if (order.getTakeProfitPrice() != null) {
                if ("BUY".equals(order.getSide()) && currentPrice.compareTo(order.getTakeProfitPrice()) >= 0) {
                    // 达到止盈价，创建订单
                    shouldCreateOrder = true;
                    newPrice = order.getTakeProfitPrice();
                } else if ("SELL".equals(order.getSide()) && currentPrice.compareTo(order.getTakeProfitPrice()) <= 0) {
                    // 达到止盈价，创建订单
                    shouldCreateOrder = true;
                    newPrice = order.getTakeProfitPrice();
                }
            }

            // 如果需要创建订单
            if (shouldCreateOrder && newPrice != null) {
                CreateOrderRequest request = new CreateOrderRequest();
                request.setPairName(order.getPairName());
                request.setSide(order.getSide());
                request.setQuantity(order.getQuantity());
                request.setPrice(newPrice);
                request.setOrderType("LIMIT");

                try {
                    spotOrderService.createOrder(order.getUserId(), request);
                    order.setStatus("FILLED");
                } catch (Exception e) {
                    // 如果创建订单失败，保持激活状态等待下次执行
                    order.setStatus("ACTIVE");
                    System.err.println("追踪委托创建订单失败: " + e.getMessage());
                }
            } else {
                // 保持激活状态
                if ("PENDING".equals(order.getStatus())) {
                    order.setStatus("ACTIVE");
                }
            }
        } catch (Exception e) {
            System.err.println("执行追踪委托失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
        }

        advancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeIcebergOrder(AdvancedOrder order) {
        // 冰山策略：需要检查当前订单是否已成交，如果成交了，再创建下一个订单
        // 这里简化处理：直接创建下一个显示订单
        
        BigDecimal icebergFilledQuantity = order.getIcebergFilledQuantity() != null ? 
                order.getIcebergFilledQuantity() : BigDecimal.ZERO;
        BigDecimal remainingQuantity = order.getIcebergTotalQuantity().subtract(icebergFilledQuantity);

        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("FILLED");
            advancedOrderRepository.save(order);
            return;
        }

        // 检查是否需要创建新订单
        // 如果状态是PENDING，立即创建第一个订单
        // 如果状态是ACTIVE，需要检查上次创建的订单是否已成交（这里简化处理，直接创建下一个订单）
        // 实际应用中应该查询已创建的订单状态来判断
        
        // 每次显示的数量
        BigDecimal displayQuantity = order.getIcebergDisplayQuantity().min(remainingQuantity);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setPairName(order.getPairName());
        request.setSide(order.getSide());
        request.setQuantity(displayQuantity);
        request.setPrice(order.getPrice());
        request.setOrderType("LIMIT");

        try {
            spotOrderService.createOrder(order.getUserId(), request);
            
            // 更新已下单数量（不是已成交数量，实际应该等订单成交后再更新）
            // 这里简化处理，假设订单会立即成交
            BigDecimal newFilledQuantity = icebergFilledQuantity.add(displayQuantity);
            order.setIcebergFilledQuantity(newFilledQuantity);

            if (newFilledQuantity.compareTo(order.getIcebergTotalQuantity()) >= 0) {
                order.setStatus("FILLED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            // 如果订单状态是PENDING，标记为失败；如果是ACTIVE，保持激活状态
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
            System.err.println("冰山策略执行失败: " + e.getMessage());
        }

        advancedOrderRepository.save(order);
    }

    /**
     * 计算下次执行时间
     */
    private LocalDateTime calculateNextExecutionTime(LocalDateTime currentTime, String period) {
        if (period == null || period.isEmpty()) {
            return currentTime.plusDays(1); // 默认1天
        }

        period = period.toUpperCase().trim();
        
        // 处理格式: "DAILY", "WEEKLY", "MONTHLY" 或 "1d", "1w", "1h" 等
        if ("DAILY".equals(period)) {
            return currentTime.plusDays(1);
        } else if ("WEEKLY".equals(period)) {
            return currentTime.plusWeeks(1);
        } else if ("MONTHLY".equals(period)) {
            return currentTime.plusMonths(1);
        } else if (period.endsWith("D")) {
            try {
                int days = Integer.parseInt(period.substring(0, period.length() - 1));
                return currentTime.plusDays(days);
            } catch (NumberFormatException e) {
                return currentTime.plusDays(1);
            }
        } else if (period.endsWith("H")) {
            try {
                int hours = Integer.parseInt(period.substring(0, period.length() - 1));
                return currentTime.plusHours(hours);
            } catch (NumberFormatException e) {
                return currentTime.plusHours(24);
            }
        } else if (period.endsWith("W")) {
            try {
                int weeks = Integer.parseInt(period.substring(0, period.length() - 1));
                return currentTime.plusWeeks(weeks);
            } catch (NumberFormatException e) {
                return currentTime.plusWeeks(1);
            }
        } else if (period.endsWith("M")) {
            try {
                int months = Integer.parseInt(period.substring(0, period.length() - 1));
                return currentTime.plusMonths(months);
            } catch (NumberFormatException e) {
                return currentTime.plusMonths(1);
            }
        }
        
        return currentTime.plusDays(1); // 默认1天
    }
}


