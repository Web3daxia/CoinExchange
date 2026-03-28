/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.dto.request.CreateFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.usdt.dto.request.CreateFuturesOrderRequest;
import com.cryptotrade.futures.usdt.dto.response.FuturesMarketDataResponse;
import com.cryptotrade.futures.usdt.entity.FuturesAdvancedOrder;
import com.cryptotrade.futures.usdt.entity.FuturesContract;
import com.cryptotrade.futures.usdt.repository.FuturesAdvancedOrderRepository;
import com.cryptotrade.futures.usdt.repository.FuturesContractRepository;
import com.cryptotrade.futures.usdt.service.FuturesAdvancedOrderService;
import com.cryptotrade.futures.usdt.service.FuturesMarketDataService;
import com.cryptotrade.futures.usdt.service.FuturesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FuturesAdvancedOrderServiceImpl implements FuturesAdvancedOrderService {

    @Autowired
    private FuturesAdvancedOrderRepository futuresAdvancedOrderRepository;

    @Autowired
    private FuturesOrderService futuresOrderService;

    @Autowired
    private FuturesMarketDataService futuresMarketDataService;

    @Autowired
    private FuturesContractRepository futuresContractRepository;

    @Override
    @Transactional
    public FuturesAdvancedOrder createAdvancedOrder(Long userId, CreateFuturesAdvancedOrderRequest request) {
        // 1. 验证合约是否存在
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(request.getPairName(), "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("USDT本位合约不存在: " + request.getPairName()));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("合约已停用");
        }

        // 2. 验证杠杆倍数
        if (request.getLeverage() < contract.getMinLeverage() ||
            request.getLeverage() > contract.getMaxLeverage()) {
            throw new RuntimeException("杠杆倍数不在允许范围内，最小: " + contract.getMinLeverage() +
                    ", 最大: " + contract.getMaxLeverage());
        }

        // 3. 创建高级订单实体
        FuturesAdvancedOrder order = new FuturesAdvancedOrder();
        order.setUserId(userId);
        order.setPairName(request.getPairName());
        order.setOrderType(request.getOrderType());
        order.setSide(request.getSide());
        order.setPositionSide(request.getPositionSide());
        order.setMarginMode(request.getMarginMode());
        order.setLeverage(request.getLeverage());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStatus("PENDING");
        order.setFee(BigDecimal.ZERO); // 初始手续费为0

        // 根据订单类型设置特定参数
        switch (request.getOrderType()) {
            case "ADVANCED_LIMIT":
                order.setTimeInForce(request.getTimeInForce() != null ? request.getTimeInForce() : "GTC");
                order.setExpireTime(request.getExpireTime());
                order.setPriceRangeMin(request.getPriceRangeMin());
                order.setPriceRangeMax(request.getPriceRangeMax());
                break;
            case "TRAILING":
                order.setTrailingDistance(request.getTrailingDistance());
                break;
            case "TRAILING_LIMIT":
                order.setTrailingDistance(request.getTrailingDistance());
                order.setTrailingLimitPrice(request.getTrailingLimitPrice());
                break;
            case "ICEBERG":
                order.setIcebergTotalQuantity(request.getIcebergTotalQuantity() != null ? 
                        request.getIcebergTotalQuantity() : request.getQuantity());
                order.setIcebergDisplayQuantity(request.getIcebergDisplayQuantity());
                order.setIcebergFilledQuantity(BigDecimal.ZERO);
                break;
            case "SEGMENTED":
                order.setSegmentedTotalQuantity(request.getSegmentedTotalQuantity() != null ? 
                        request.getSegmentedTotalQuantity() : request.getQuantity());
                order.setSegmentedCount(request.getSegmentedCount());
                order.setSegmentedFilledCount(0);
                break;
            case "TIME_WEIGHTED":
                order.setTimeInterval(request.getTimeInterval());
                order.setStartTime(request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now());
                order.setEndTime(request.getEndTime());
                break;
            default:
                throw new IllegalArgumentException("不支持的高级订单类型: " + request.getOrderType());
        }

        return futuresAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelAdvancedOrder(Long userId, Long orderId) {
        FuturesAdvancedOrder order = futuresAdvancedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("高级订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus()) && !"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("只能取消PENDING或ACTIVE状态的订单");
        }

        order.setStatus("CANCELLED");
        futuresAdvancedOrderRepository.save(order);
    }

    @Override
    public List<FuturesAdvancedOrder> getUserAdvancedOrders(Long userId) {
        return futuresAdvancedOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void executeAdvancedOrders() {
        // 查询所有PENDING或ACTIVE状态的高级订单
        List<String> statuses = Arrays.asList("PENDING", "ACTIVE");
        List<FuturesAdvancedOrder> orders = futuresAdvancedOrderRepository.findByStatusIn(statuses);

        for (FuturesAdvancedOrder order : orders) {
            try {
                switch (order.getOrderType()) {
                    case "ADVANCED_LIMIT":
                        executeAdvancedLimitOrder(order);
                        break;
                    case "TRAILING":
                        executeTrailingOrder(order);
                        break;
                    case "TRAILING_LIMIT":
                        executeTrailingLimitOrder(order);
                        break;
                    case "ICEBERG":
                        executeIcebergOrder(order);
                        break;
                    case "SEGMENTED":
                        executeSegmentedOrder(order);
                        break;
                    case "TIME_WEIGHTED":
                        executeTimeWeightedOrder(order);
                        break;
                }
            } catch (Exception e) {
                System.err.println("执行高级订单失败: " + order.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void executeAdvancedLimitOrder(FuturesAdvancedOrder order) {
        if (!"ADVANCED_LIMIT".equals(order.getOrderType())) {
            return;
        }

        // 检查订单是否过期
        if (order.getExpireTime() != null && LocalDateTime.now().isAfter(order.getExpireTime())) {
            order.setStatus("EXPIRED");
            futuresAdvancedOrderRepository.save(order);
            return;
        }

        // 获取当前市场价格
        BigDecimal currentPrice = getCurrentPrice(order.getPairName());
        if (currentPrice == null) {
            return; // 无法获取价格，等待下次执行
        }

        // 检查价格是否在范围内
        if (order.getPriceRangeMin() != null && currentPrice.compareTo(order.getPriceRangeMin()) < 0) {
            return;
        }
        if (order.getPriceRangeMax() != null && currentPrice.compareTo(order.getPriceRangeMax()) > 0) {
            return;
        }

        CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(order.getQuantity());
        createOrderRequest.setPrice(order.getPrice()); // 使用高级订单设定的价格

        switch (order.getTimeInForce()) {
            case "IOC": // Immediate-Or-Cancel: 立即成交，未成交部分取消
                createOrderRequest.setOrderType("MARKET"); // 简化处理，IOC/FOK直接按市价单处理
                break;
            case "FOK": // Fill-Or-Kill: 全部成交或全部取消
                createOrderRequest.setOrderType("MARKET"); // 简化处理
                break;
            case "GTC": // Good-Til-Canceled
            default:
                createOrderRequest.setOrderType("LIMIT");
                break;
        }

        try {
            futuresOrderService.createOrder(order.getUserId(), createOrderRequest);
            if ("IOC".equals(order.getTimeInForce()) || "FOK".equals(order.getTimeInForce())) {
                order.setStatus("COMPLETED"); // 假设立即成交
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("执行高级限价单失败: " + e.getMessage());
            order.setStatus("FAILED");
        }
        futuresAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTrailingOrder(FuturesAdvancedOrder order) {
        if (!"TRAILING".equals(order.getOrderType())) {
            return;
        }

        try {
            BigDecimal currentPrice = getCurrentPrice(order.getPairName());
            if (currentPrice == null) {
                return;
            }

            BigDecimal trailingDistance = order.getTrailingDistance() != null ?
                    order.getTrailingDistance() : new BigDecimal("0.01"); // 默认1%

            BigDecimal trailingPrice = order.getTrailingPrice();
            BigDecimal newOrderPrice = null;
            boolean shouldCreateOrder = false;

            if ("BUY".equals(order.getSide())) {
                // 买入追踪：价格下跌时，买入价格跟随下跌
                if (trailingPrice == null) {
                    // 首次设置，初始化追踪价格为当前价格减去追踪距离
                    trailingPrice = currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    order.setTrailingPrice(trailingPrice);
                    futuresAdvancedOrderRepository.save(order);
                    return; // 等待下次执行
                } else {
                    // 如果当前价格比追踪价格更低，更新追踪价格
                    BigDecimal potentialTrailingPrice = currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    if (potentialTrailingPrice.compareTo(trailingPrice) < 0) {
                        order.setTrailingPrice(potentialTrailingPrice);
                        futuresAdvancedOrderRepository.save(order);
                        return; // 追踪价格更新，等待下次执行
                    } else if (currentPrice.compareTo(trailingPrice) >= 0) {
                        // 价格反弹，触发买入
                        newOrderPrice = trailingPrice; // 以追踪到的最低价买入
                        shouldCreateOrder = true;
                    }
                }
            } else { // SELL
                // 卖出追踪：价格上涨时，卖出价格跟随上涨
                if (trailingPrice == null) {
                    // 首次设置，初始化追踪价格为当前价格加上追踪距离
                    trailingPrice = currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    order.setTrailingPrice(trailingPrice);
                    futuresAdvancedOrderRepository.save(order);
                    return; // 等待下次执行
                } else {
                    // 如果当前价格比追踪价格更高，更新追踪价格
                    BigDecimal potentialTrailingPrice = currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    if (potentialTrailingPrice.compareTo(trailingPrice) > 0) {
                        order.setTrailingPrice(potentialTrailingPrice);
                        futuresAdvancedOrderRepository.save(order);
                        return; // 追踪价格更新，等待下次执行
                    } else if (currentPrice.compareTo(trailingPrice) <= 0) {
                        // 价格反转下跌，触发卖出
                        newOrderPrice = trailingPrice; // 以追踪到的最高价卖出
                        shouldCreateOrder = true;
                    }
                }
            }

            if (shouldCreateOrder && newOrderPrice != null) {
                CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
                createOrderRequest.setPairName(order.getPairName());
                createOrderRequest.setSide(order.getSide());
                createOrderRequest.setPositionSide(order.getPositionSide());
                createOrderRequest.setMarginMode(order.getMarginMode());
                createOrderRequest.setLeverage(order.getLeverage());
                createOrderRequest.setQuantity(order.getQuantity());
                createOrderRequest.setPrice(newOrderPrice);
                createOrderRequest.setOrderType("LIMIT"); // 追踪委托通常以限价单形式触发

                futuresOrderService.createOrder(order.getUserId(), createOrderRequest);
                order.setStatus("COMPLETED");
                futuresAdvancedOrderRepository.save(order);
            } else {
                // 如果是PENDING状态，首次执行后变为ACTIVE
                if ("PENDING".equals(order.getStatus())) {
                    order.setStatus("ACTIVE");
                    futuresAdvancedOrderRepository.save(order);
                }
            }

        } catch (Exception e) {
            System.err.println("执行追踪委托失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
                futuresAdvancedOrderRepository.save(order);
            }
        }
    }

    @Override
    @Transactional
    public void executeTrailingLimitOrder(FuturesAdvancedOrder order) {
        if (!"TRAILING_LIMIT".equals(order.getOrderType())) {
            return;
        }

        try {
            BigDecimal currentPrice = getCurrentPrice(order.getPairName());
            if (currentPrice == null) {
                return;
            }

            BigDecimal trailingDistance = order.getTrailingDistance() != null ?
                    order.getTrailingDistance() : new BigDecimal("0.01"); // 默认1%
            BigDecimal trailingLimitPrice = order.getTrailingLimitPrice(); // 用户设定的限价

            BigDecimal newTriggerPrice = null;
            boolean shouldCreateOrder = false;

            if ("BUY".equals(order.getSide())) {
                // 买入追逐限价：价格下跌时，触发价格跟随下跌
                if (order.getTrailingPrice() == null) { // trailingPrice在此处作为触发价格
                    order.setTrailingPrice(currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP))));
                    futuresAdvancedOrderRepository.save(order);
                    return;
                } else {
                    BigDecimal potentialTriggerPrice = currentPrice.multiply(BigDecimal.ONE.subtract(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    if (potentialTriggerPrice.compareTo(order.getTrailingPrice()) < 0) {
                        order.setTrailingPrice(potentialTriggerPrice);
                        futuresAdvancedOrderRepository.save(order);
                        return;
                    } else if (currentPrice.compareTo(order.getTrailingPrice()) >= 0) {
                        // 价格反弹，触发买入
                        newTriggerPrice = order.getTrailingPrice();
                        shouldCreateOrder = true;
                    }
                }
            } else { // SELL
                // 卖出追逐限价：价格上涨时，触发价格跟随上涨
                if (order.getTrailingPrice() == null) {
                    order.setTrailingPrice(currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP))));
                    futuresAdvancedOrderRepository.save(order);
                    return;
                } else {
                    BigDecimal potentialTriggerPrice = currentPrice.multiply(BigDecimal.ONE.add(trailingDistance.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)));
                    if (potentialTriggerPrice.compareTo(order.getTrailingPrice()) > 0) {
                        order.setTrailingPrice(potentialTriggerPrice);
                        futuresAdvancedOrderRepository.save(order);
                        return;
                    } else if (currentPrice.compareTo(order.getTrailingPrice()) <= 0) {
                        // 价格反转下跌，触发卖出
                        newTriggerPrice = order.getTrailingPrice();
                        shouldCreateOrder = true;
                    }
                }
            }

            if (shouldCreateOrder && newTriggerPrice != null) {
                CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
                createOrderRequest.setPairName(order.getPairName());
                createOrderRequest.setSide(order.getSide());
                createOrderRequest.setPositionSide(order.getPositionSide());
                createOrderRequest.setMarginMode(order.getMarginMode());
                createOrderRequest.setLeverage(order.getLeverage());
                createOrderRequest.setQuantity(order.getQuantity());
                createOrderRequest.setPrice(trailingLimitPrice); // 使用用户设定的限价
                createOrderRequest.setOrderType("LIMIT");

                futuresOrderService.createOrder(order.getUserId(), createOrderRequest);
                order.setStatus("COMPLETED");
                futuresAdvancedOrderRepository.save(order);
            } else {
                if ("PENDING".equals(order.getStatus())) {
                    order.setStatus("ACTIVE");
                    futuresAdvancedOrderRepository.save(order);
                }
            }

        } catch (Exception e) {
            System.err.println("执行追逐限价单失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
                futuresAdvancedOrderRepository.save(order);
            }
        }
    }

    @Override
    @Transactional
    public void executeIcebergOrder(FuturesAdvancedOrder order) {
        if (!"ICEBERG".equals(order.getOrderType())) {
            return;
        }

        BigDecimal icebergFilledQuantity = order.getIcebergFilledQuantity() != null ?
                order.getIcebergFilledQuantity() : BigDecimal.ZERO;
        BigDecimal remainingQuantity = order.getIcebergTotalQuantity().subtract(icebergFilledQuantity);

        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("COMPLETED");
            futuresAdvancedOrderRepository.save(order);
            return;
        }

        // 每次显示的数量
        BigDecimal displayQuantity = order.getIcebergDisplayQuantity().min(remainingQuantity);

        CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(displayQuantity);
        createOrderRequest.setPrice(order.getPrice());
        createOrderRequest.setOrderType("LIMIT");

        try {
            futuresOrderService.createOrder(order.getUserId(), createOrderRequest);

            // 更新已下单数量（实际应该等订单成交后再更新）
            BigDecimal newFilledQuantity = icebergFilledQuantity.add(displayQuantity);
            order.setIcebergFilledQuantity(newFilledQuantity);

            if (newFilledQuantity.compareTo(order.getIcebergTotalQuantity()) >= 0) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("冰山策略执行失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
        }
        futuresAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeSegmentedOrder(FuturesAdvancedOrder order) {
        if (!"SEGMENTED".equals(order.getOrderType())) {
            return;
        }

        Integer segmentedFilledCount = order.getSegmentedFilledCount() != null ?
                order.getSegmentedFilledCount() : 0;
        Integer segmentedCount = order.getSegmentedCount();

        if (segmentedFilledCount >= segmentedCount) {
            order.setStatus("COMPLETED");
            futuresAdvancedOrderRepository.save(order);
            return;
        }

        // 计算每次下单的数量
        BigDecimal quantityPerSegment = order.getSegmentedTotalQuantity()
                .divide(new BigDecimal(segmentedCount), 8, RoundingMode.DOWN);

        if (quantityPerSegment.compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("FAILED");
            futuresAdvancedOrderRepository.save(order);
            throw new RuntimeException("分段委托数量计算错误，数量为零或负数");
        }

        CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(quantityPerSegment);
        createOrderRequest.setPrice(order.getPrice()); // 分段委托通常使用限价
        createOrderRequest.setOrderType("LIMIT");

        try {
            futuresOrderService.createOrder(order.getUserId(), createOrderRequest);

            order.setSegmentedFilledCount(segmentedFilledCount + 1);
            if (order.getSegmentedFilledCount() >= segmentedCount) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("分段委托执行失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
        }
        futuresAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTimeWeightedOrder(FuturesAdvancedOrder order) {
        if (!"TIME_WEIGHTED".equals(order.getOrderType())) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // 检查是否在时间范围内
        if (order.getStartTime() != null && now.isBefore(order.getStartTime())) {
            return; // 还未到开始时间
        }

        if (order.getEndTime() != null && now.isAfter(order.getEndTime())) {
            order.setStatus("EXPIRED");
            futuresAdvancedOrderRepository.save(order);
            return;
        }

        // 计算剩余数量
        BigDecimal filledQuantity = order.getIcebergFilledQuantity() != null ? order.getIcebergFilledQuantity() : BigDecimal.ZERO; // 复用icebergFilledQuantity字段
        BigDecimal totalQuantity = order.getQuantity();
        BigDecimal remainingQuantity = totalQuantity.subtract(filledQuantity);

        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus("COMPLETED");
            futuresAdvancedOrderRepository.save(order);
            return;
        }

        // 检查是否需要执行（根据时间间隔判断）
        if ("ACTIVE".equals(order.getStatus())) {
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
            long totalSeconds = Duration.between(order.getStartTime(), order.getEndTime()).getSeconds();
            long elapsedSeconds = Duration.between(order.getStartTime(), now).getSeconds();
            
            if (totalSeconds <= 0) { // 避免除以零或负数时间
                orderQuantity = remainingQuantity;
            } else {
                // 剩余时间内的平均分配
                BigDecimal remainingTimeRatio = new BigDecimal(totalSeconds - elapsedSeconds)
                                                .divide(new BigDecimal(totalSeconds), 8, RoundingMode.HALF_UP);
                orderQuantity = remainingQuantity.multiply(remainingTimeRatio)
                                                .divide(new BigDecimal(order.getTimeInterval()), 8, RoundingMode.DOWN); // 简化处理，按时间间隔平均分配
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

        CreateFuturesOrderRequest createOrderRequest = new CreateFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(orderQuantity);
        createOrderRequest.setPrice(order.getPrice());
        createOrderRequest.setOrderType("LIMIT");

        try {
            futuresOrderService.createOrder(order.getUserId(), createOrderRequest);

            // 更新已执行数量
            BigDecimal newFilledQuantity = filledQuantity.add(orderQuantity);
            order.setIcebergFilledQuantity(newFilledQuantity); // 复用字段

            if (newFilledQuantity.compareTo(totalQuantity) >= 0) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("分时委托执行失败: " + e.getMessage());
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("FAILED");
            }
        }
        futuresAdvancedOrderRepository.save(order);
    }

    /**
     * 获取当前价格
     */
    private BigDecimal getCurrentPrice(String pairName) {
        try {
            FuturesMarketDataResponse marketData = futuresMarketDataService.getMarketData(pairName);
            return marketData.getCurrentPrice();
        } catch (Exception e) {
            return null;
        }
    }
}
