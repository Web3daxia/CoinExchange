/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesMarketDataResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesAdvancedOrder;
import com.cryptotrade.futures.coin.entity.CoinFuturesContract;
import com.cryptotrade.futures.coin.repository.CoinFuturesAdvancedOrderRepository;
import com.cryptotrade.futures.coin.repository.CoinFuturesContractRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesAdvancedOrderService;
import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataService;
import com.cryptotrade.futures.coin.service.CoinFuturesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoinFuturesAdvancedOrderServiceImpl implements CoinFuturesAdvancedOrderService {

    @Autowired
    private CoinFuturesAdvancedOrderRepository coinFuturesAdvancedOrderRepository;

    @Autowired
    private CoinFuturesOrderService coinFuturesOrderService;

    @Autowired
    private CoinFuturesMarketDataService coinFuturesMarketDataService;

    @Autowired
    private CoinFuturesContractRepository coinFuturesContractRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public CoinFuturesAdvancedOrder createAdvancedOrder(Long userId, CreateCoinFuturesAdvancedOrderRequest request) {
        // 1. 验证合约是否存在
        CoinFuturesContract contract = coinFuturesContractRepository
                .findByPairNameAndContractType(request.getPairName(), "COIN_MARGINED")
                .orElseThrow(() -> new RuntimeException("币本位合约不存在: " + request.getPairName()));

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
        CoinFuturesAdvancedOrder order = new CoinFuturesAdvancedOrder();
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

        Map<String, Object> params = request.getParams();

        // 根据订单类型设置特定参数
        switch (request.getOrderType()) {
            case "ADVANCED_LIMIT":
                if (params != null) {
                    order.setTimeInForce((String) params.get("timeInForce"));
                    order.setExpireTime(params.containsKey("expireTime") ? LocalDateTime.parse((String) params.get("expireTime")) : null);
                    order.setPriceRangeMin(params.containsKey("priceRangeMin") ? new BigDecimal(params.get("priceRangeMin").toString()) : null);
                    order.setPriceRangeMax(params.containsKey("priceRangeMax") ? new BigDecimal(params.get("priceRangeMax").toString()) : null);
                }
                break;
            case "TRAILING":
                if (params != null && params.containsKey("trailingDistance")) {
                    order.setTrailingDistance(new BigDecimal(params.get("trailingDistance").toString()));
                }
                break;
            case "TRAILING_LIMIT":
                if (params != null) {
                    if (params.containsKey("trailingDistance")) {
                        order.setTrailingDistance(new BigDecimal(params.get("trailingDistance").toString()));
                    }
                    if (params.containsKey("trailingLimitPrice")) {
                        order.setTrailingLimitPrice(new BigDecimal(params.get("trailingLimitPrice").toString()));
                    }
                }
                break;
            case "ICEBERG":
                if (params != null) {
                    if (params.containsKey("icebergTotalQuantity")) {
                        order.setIcebergTotalQuantity(new BigDecimal(params.get("icebergTotalQuantity").toString()));
                    }
                    if (params.containsKey("icebergDisplayQuantity")) {
                        order.setIcebergDisplayQuantity(new BigDecimal(params.get("icebergDisplayQuantity").toString()));
                    }
                }
                order.setIcebergFilledQuantity(BigDecimal.ZERO);
                break;
            case "SEGMENTED":
                if (params != null) {
                    if (params.containsKey("segmentedTotalQuantity")) {
                        order.setSegmentedTotalQuantity(new BigDecimal(params.get("segmentedTotalQuantity").toString()));
                    }
                    if (params.containsKey("segmentedCount")) {
                        order.setSegmentedCount(Integer.parseInt(params.get("segmentedCount").toString()));
                    }
                }
                order.setSegmentedFilledCount(0);
                break;
            case "TIME_WEIGHTED":
                if (params != null) {
                    if (params.containsKey("timeInterval")) {
                        order.setTimeInterval(Integer.parseInt(params.get("timeInterval").toString()));
                    }
                    if (params.containsKey("startTime")) {
                        order.setStartTime(LocalDateTime.parse((String) params.get("startTime")));
                    }
                    if (params.containsKey("endTime")) {
                        order.setEndTime(LocalDateTime.parse((String) params.get("endTime")));
                    }
                }
                break;
        }

        return coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelAdvancedOrder(Long userId, Long orderId) {
        CoinFuturesAdvancedOrder order = coinFuturesAdvancedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("高级订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus()) && !"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("只能取消PENDING或ACTIVE状态的订单");
        }

        order.setStatus("CANCELLED");
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    public List<CoinFuturesAdvancedOrder> getUserAdvancedOrders(Long userId) {
        return coinFuturesAdvancedOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void executeAdvancedOrders() {
        // 查询所有PENDING或ACTIVE状态的高级订单
        List<String> statuses = Arrays.asList("PENDING", "ACTIVE");
        List<CoinFuturesAdvancedOrder> orders = coinFuturesAdvancedOrderRepository.findByStatusIn(statuses);

        for (CoinFuturesAdvancedOrder order : orders) {
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
    public void executeAdvancedLimitOrder(CoinFuturesAdvancedOrder order) {
        // 检查订单是否过期
        if (order.getExpireTime() != null && LocalDateTime.now().isAfter(order.getExpireTime())) {
            order.setStatus("EXPIRED");
            coinFuturesAdvancedOrderRepository.save(order);
            return;
        }

        // 获取当前市场价格
        BigDecimal currentPrice = getCurrentPrice(order.getPairName());
        if (currentPrice == null) {
            return;
        }

        // 检查价格是否在范围内
        if (order.getPriceRangeMin() != null && currentPrice.compareTo(order.getPriceRangeMin()) < 0) {
            return;
        }
        if (order.getPriceRangeMax() != null && currentPrice.compareTo(order.getPriceRangeMax()) > 0) {
            return;
        }

        CreateCoinFuturesOrderRequest createOrderRequest = new CreateCoinFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(order.getQuantity());
        createOrderRequest.setPrice(order.getPrice());

        String timeInForce = order.getTimeInForce() != null ? order.getTimeInForce() : "GTC";
        switch (timeInForce) {
            case "IOC":
            case "FOK":
                createOrderRequest.setOrderType("MARKET");
                break;
            case "GTC":
            default:
                createOrderRequest.setOrderType("LIMIT");
                break;
        }

        try {
            coinFuturesOrderService.createOrder(order.getUserId(), createOrderRequest);
            if ("IOC".equals(timeInForce) || "FOK".equals(timeInForce)) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("执行高级限价单失败: " + e.getMessage());
            order.setStatus("FAILED");
        }
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTrailingOrder(CoinFuturesAdvancedOrder order) {
        // 追踪委托实现（简化版本）
        BigDecimal currentPrice = getCurrentPrice(order.getPairName());
        if (currentPrice == null) {
            return;
        }

        // 实现追踪逻辑（参考USDT本位的实现）
        // 这里简化处理，实际应该实现完整的追踪逻辑
        order.setStatus("ACTIVE");
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTrailingLimitOrder(CoinFuturesAdvancedOrder order) {
        // 追逐限价单实现（简化版本）
        BigDecimal currentPrice = getCurrentPrice(order.getPairName());
        if (currentPrice == null) {
            return;
        }

        // 实现追逐限价逻辑
        order.setStatus("ACTIVE");
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeIcebergOrder(CoinFuturesAdvancedOrder order) {
        // 冰山策略实现
        if (order.getIcebergFilledQuantity().compareTo(order.getIcebergTotalQuantity()) >= 0) {
            order.setStatus("COMPLETED");
            coinFuturesAdvancedOrderRepository.save(order);
            return;
        }

        BigDecimal remainingQuantity = order.getIcebergTotalQuantity().subtract(order.getIcebergFilledQuantity());
        BigDecimal displayQuantity = order.getIcebergDisplayQuantity();
        BigDecimal quantityToExecute = remainingQuantity.compareTo(displayQuantity) < 0 ? remainingQuantity : displayQuantity;

        CreateCoinFuturesOrderRequest createOrderRequest = new CreateCoinFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(quantityToExecute);
        createOrderRequest.setPrice(order.getPrice());
        createOrderRequest.setOrderType("LIMIT");

        try {
            coinFuturesOrderService.createOrder(order.getUserId(), createOrderRequest);
            order.setIcebergFilledQuantity(order.getIcebergFilledQuantity().add(quantityToExecute));
            if (order.getIcebergFilledQuantity().compareTo(order.getIcebergTotalQuantity()) >= 0) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("执行冰山订单失败: " + e.getMessage());
        }
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeSegmentedOrder(CoinFuturesAdvancedOrder order) {
        // 分段委托实现
        if (order.getSegmentedFilledCount() >= order.getSegmentedCount()) {
            order.setStatus("COMPLETED");
            coinFuturesAdvancedOrderRepository.save(order);
            return;
        }

        BigDecimal segmentQuantity = order.getSegmentedTotalQuantity()
                .divide(new BigDecimal(order.getSegmentedCount()), 8, RoundingMode.DOWN);

        CreateCoinFuturesOrderRequest createOrderRequest = new CreateCoinFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(segmentQuantity);
        createOrderRequest.setPrice(order.getPrice());
        createOrderRequest.setOrderType("LIMIT");

        try {
            coinFuturesOrderService.createOrder(order.getUserId(), createOrderRequest);
            order.setSegmentedFilledCount(order.getSegmentedFilledCount() + 1);
            if (order.getSegmentedFilledCount() >= order.getSegmentedCount()) {
                order.setStatus("COMPLETED");
            } else {
                order.setStatus("ACTIVE");
            }
        } catch (Exception e) {
            System.err.println("执行分段订单失败: " + e.getMessage());
        }
        coinFuturesAdvancedOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void executeTimeWeightedOrder(CoinFuturesAdvancedOrder order) {
        // 分时委托实现
        LocalDateTime now = LocalDateTime.now();
        if (order.getStartTime() != null && now.isBefore(order.getStartTime())) {
            return; // 还未到开始时间
        }
        if (order.getEndTime() != null && now.isAfter(order.getEndTime())) {
            order.setStatus("COMPLETED");
            coinFuturesAdvancedOrderRepository.save(order);
            return;
        }

        // 检查是否到了执行时间间隔
        // 简化处理：每次执行一小部分
        BigDecimal timeInterval = order.getTimeInterval() != null ? 
                new BigDecimal(order.getTimeInterval()) : new BigDecimal("60"); // 默认60秒
        // 这里需要记录上次执行时间，简化处理
        BigDecimal segmentQuantity = order.getQuantity().divide(new BigDecimal("10"), 8, RoundingMode.DOWN);

        CreateCoinFuturesOrderRequest createOrderRequest = new CreateCoinFuturesOrderRequest();
        createOrderRequest.setPairName(order.getPairName());
        createOrderRequest.setSide(order.getSide());
        createOrderRequest.setPositionSide(order.getPositionSide());
        createOrderRequest.setMarginMode(order.getMarginMode());
        createOrderRequest.setLeverage(order.getLeverage());
        createOrderRequest.setQuantity(segmentQuantity);
        createOrderRequest.setPrice(order.getPrice());
        createOrderRequest.setOrderType("LIMIT");

        try {
            coinFuturesOrderService.createOrder(order.getUserId(), createOrderRequest);
            order.setStatus("ACTIVE");
        } catch (Exception e) {
            System.err.println("执行分时订单失败: " + e.getMessage());
        }
        coinFuturesAdvancedOrderRepository.save(order);
    }

    /**
     * 获取当前价格
     */
    private BigDecimal getCurrentPrice(String pairName) {
        try {
            CoinFuturesMarketDataResponse marketData = 
                    coinFuturesMarketDataService.getMarketData(pairName);
            return marketData.getCurrentPrice();
        } catch (Exception e) {
            System.err.println("获取当前价格失败: " + e.getMessage());
            return null;
        }
    }
}

