/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.dto.request.CreateFuturesOrderRequest;
import com.cryptotrade.futures.usdt.dto.response.FuturesOrderResponse;
import com.cryptotrade.futures.usdt.entity.FuturesContract;
import com.cryptotrade.futures.usdt.entity.FuturesOrder;
import com.cryptotrade.futures.usdt.entity.FuturesPosition;
import com.cryptotrade.futures.usdt.repository.FuturesContractRepository;
import com.cryptotrade.futures.usdt.repository.FuturesOrderRepository;
import com.cryptotrade.futures.usdt.repository.FuturesPositionRepository;
import com.cryptotrade.futures.usdt.service.FuturesOrderService;
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
public class FuturesOrderServiceImpl implements FuturesOrderService {

    @Autowired
    private FuturesOrderRepository futuresOrderRepository;

    @Autowired
    private FuturesContractRepository futuresContractRepository;

    @Autowired
    private FuturesPositionRepository futuresPositionRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TradingFeeService tradingFeeService;

    private static final String ACCOUNT_TYPE = "FUTURES"; // 永续合约账户类型
    private static final String SETTLEMENT_CURRENCY = "USDT"; // USDT本位合约结算货币

    @Override
    @Transactional
    public FuturesOrder createOrder(Long userId, CreateFuturesOrderRequest request) {
        // 1. 验证合约是否存在
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(request.getPairName(), "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("USDT本位合约不存在: " + request.getPairName()));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new RuntimeException("合约已停用");
        }

        // 2. 验证订单参数
        validateOrderRequest(request, contract);

        // 3. 验证杠杆倍数
        if (request.getLeverage() < contract.getMinLeverage() || 
            request.getLeverage() > contract.getMaxLeverage()) {
            throw new RuntimeException("杠杆倍数不在允许范围内，最小: " + contract.getMinLeverage() + 
                    ", 最大: " + contract.getMaxLeverage());
        }

        // 4. 获取当前价格（用于计算保证金）
        BigDecimal currentPrice = contract.getMarkPrice() != null ? 
                contract.getMarkPrice() : contract.getCurrentPrice();
        if (currentPrice == null) {
            throw new RuntimeException("无法获取当前价格，请稍后重试");
        }

        // 5. 计算保证金
        // 保证金 = 持仓价值 / 杠杆倍数
        // 持仓价值 = 数量 × 价格
        BigDecimal orderPrice = "MARKET".equals(request.getOrderType()) ? 
                currentPrice : request.getPrice();
        if (orderPrice == null) {
            orderPrice = currentPrice;
        }

        BigDecimal positionValue = request.getQuantity().multiply(orderPrice);
        BigDecimal margin = positionValue.divide(new BigDecimal(request.getLeverage()), 8, RoundingMode.UP);

        // 6. 验证用户余额（USDT余额）
        if (!walletService.checkBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, margin)) {
            BigDecimal available = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY);
            throw new RuntimeException("保证金不足，需要: " + margin + " USDT, 可用: " + available + " USDT");
        }

        // 7. 冻结保证金
        walletService.freezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, margin);

        // 8. 创建订单记录
        FuturesOrder order = new FuturesOrder();
        order.setUserId(userId);
        order.setPairName(request.getPairName());
        order.setContractType("USDT_MARGINED");
        order.setOrderType(request.getOrderType());
        order.setSide(request.getSide());
        order.setPositionSide(request.getPositionSide());
        order.setMarginMode(request.getMarginMode());
        order.setLeverage(request.getLeverage());
        order.setPrice(request.getPrice());
        order.setQuantity(request.getQuantity());
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setStatus("PENDING");
        order.setMargin(margin);
        order.setFee(BigDecimal.ZERO);
        order.setStopPrice(request.getStopPrice());
        order.setTakeProfitPrice(request.getTakeProfitPrice());
        order.setLimitPrice(request.getLimitPrice());
        order.setConditionPrice(request.getConditionPrice());

        order = futuresOrderRepository.save(order);

        // 9. 订单撮合处理
        try {
            matchOrders(order);
        } catch (Exception e) {
            // 如果撮合失败，解冻保证金
            walletService.unfreezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, margin);
            order.setStatus("CANCELLED");
            futuresOrderRepository.save(order);
            throw new RuntimeException("订单创建失败: " + e.getMessage(), e);
        }

        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        FuturesOrder order = futuresOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("只能取消待成交状态的订单，当前状态: " + order.getStatus());
        }

        // 计算需要解冻的保证金（未成交部分）
        BigDecimal remainingQuantity = order.getQuantity().subtract(order.getFilledQuantity());
        BigDecimal frozenMargin = order.getMargin()
                .multiply(remainingQuantity)
                .divide(order.getQuantity(), 8, RoundingMode.DOWN);

        // 解冻保证金
        walletService.unfreezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, frozenMargin);

        // 更新订单状态
        order.setStatus("CANCELLED");
        futuresOrderRepository.save(order);
    }

    @Override
    public FuturesOrderResponse getOrderStatus(Long userId, Long orderId) {
        FuturesOrder order = futuresOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此订单");
        }

        return convertToFuturesOrderResponse(order);
    }

    @Override
    public Page<FuturesOrderResponse> getOrderHistory(Long userId, Pageable pageable) {
        List<FuturesOrder> orders = futuresOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), orders.size());
        List<FuturesOrderResponse> FuturesOrderResponses = orders.subList(start, end).stream()
                .map(this::convertToFuturesOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(FuturesOrderResponses, pageable, orders.size());
    }

    @Override
    @Transactional
    public void matchOrders(FuturesOrder order) {
        // 获取合约信息
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(order.getPairName(), "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        BigDecimal currentPrice = contract.getMarkPrice() != null ? 
                contract.getMarkPrice() : contract.getCurrentPrice();

        if ("MARKET".equals(order.getOrderType())) {
            // 市价单：立即按当前价格撮合
            BigDecimal filledQuantity = order.getQuantity();
            settleOrder(order, filledQuantity, currentPrice);
        } else if ("LIMIT".equals(order.getOrderType())) {
            // 限价单：检查价格是否匹配
            if (order.getPrice() != null) {
                boolean canMatch = false;
                
                if ("BUY".equals(order.getSide()) && "LONG".equals(order.getPositionSide())) {
                    // 买入开多：当前价格 <= 限价
                    canMatch = currentPrice.compareTo(order.getPrice()) <= 0;
                } else if ("SELL".equals(order.getSide()) && "SHORT".equals(order.getPositionSide())) {
                    // 卖出开空：当前价格 >= 限价
                    canMatch = currentPrice.compareTo(order.getPrice()) >= 0;
                } else if ("SELL".equals(order.getSide()) && "LONG".equals(order.getPositionSide())) {
                    // 卖出平多：当前价格 >= 限价
                    canMatch = currentPrice.compareTo(order.getPrice()) >= 0;
                } else if ("BUY".equals(order.getSide()) && "SHORT".equals(order.getPositionSide())) {
                    // 买入平空：当前价格 <= 限价
                    canMatch = currentPrice.compareTo(order.getPrice()) <= 0;
                }

                if (canMatch) {
                    BigDecimal filledQuantity = order.getQuantity();
                    settleOrder(order, filledQuantity, order.getPrice());
                }
                // 如果价格不匹配，订单保持PENDING状态，等待后续撮合
            }
        } else if ("STOP_LOSS".equals(order.getOrderType())) {
            // 止损单：需要检查价格是否触发
            checkStopLossOrder(order, currentPrice);
        } else if ("TAKE_PROFIT".equals(order.getOrderType())) {
            // 止盈单：需要检查价格是否触发
            checkTakeProfitOrder(order, currentPrice);
        } else if ("STOP_LIMIT".equals(order.getOrderType())) {
            // 止损限价单：先触发止损价格，再以限价成交
            checkStopLimitOrder(order, currentPrice);
        } else if ("CONDITIONAL".equals(order.getOrderType())) {
            // 条件单：检查条件价格
            checkConditionalOrder(order, currentPrice);
        }
    }

    @Override
    @Transactional
    public void settleOrder(FuturesOrder order, BigDecimal filledQuantity, BigDecimal avgPrice) {
        // 1. 计算手续费
        BigDecimal fee = tradingFeeService.calculateFee(order.getUserId(), order.getPairName(), 
                filledQuantity, avgPrice, order.getSide());

        // 2. 计算实际使用的保证金（按成交比例）
        BigDecimal actualMargin = order.getMargin()
                .multiply(filledQuantity)
                .divide(order.getQuantity(), 8, RoundingMode.DOWN);

        // 3. 判断是开仓还是平仓
        boolean isOpenPosition = isOpenPosition(order);
        
        // 4. 更新或创建仓位
        FuturesPosition position = updateOrCreatePosition(order, filledQuantity, avgPrice, actualMargin, fee);

        // 5. 处理保证金和手续费
        
        // 解冻未成交部分的保证金
        BigDecimal remainingQuantity = order.getQuantity().subtract(filledQuantity);
        if (remainingQuantity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal unfrozenMargin = order.getMargin()
                    .multiply(remainingQuantity)
                    .divide(order.getQuantity(), 8, RoundingMode.DOWN);
            walletService.unfreezeBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, unfrozenMargin);
        }

        if (isOpenPosition) {
            // 开仓：已成交部分的保证金保留（转为仓位保证金），只需要扣除手续费
            walletService.deductBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, fee);
        } else {
            // 平仓：保证金已在closeOrReducePosition中处理
            walletService.deductBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, fee);
        }

        // 6. 更新订单
        order.setFilledQuantity(filledQuantity);
        order.setAvgPrice(avgPrice);
        order.setFee(fee);
        order.setPositionId(position != null ? position.getId() : null);

        if (filledQuantity.compareTo(order.getQuantity()) >= 0) {
            order.setStatus("FILLED");
        } else {
            order.setStatus("PARTIAL_FILLED");
        }

        futuresOrderRepository.save(order);
    }

    /**
     * 验证订单请求
     */
    private void validateOrderRequest(CreateFuturesOrderRequest request, FuturesContract contract) {
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("订单数量必须大于0");
        }

        if ("LIMIT".equals(request.getOrderType()) && request.getPrice() == null) {
            throw new RuntimeException("限价单必须指定价格");
        }

        if ("STOP_LOSS".equals(request.getOrderType()) && request.getStopPrice() == null) {
            throw new RuntimeException("止损单必须指定止损价格");
        }

        if ("TAKE_PROFIT".equals(request.getOrderType()) && request.getTakeProfitPrice() == null) {
            throw new RuntimeException("止盈单必须指定止盈价格");
        }

        if ("STOP_LIMIT".equals(request.getOrderType()) && 
            (request.getStopPrice() == null || request.getLimitPrice() == null)) {
            throw new RuntimeException("止损限价单必须指定止损价格和限价");
        }

        if ("CONDITIONAL".equals(request.getOrderType()) && request.getConditionPrice() == null) {
            throw new RuntimeException("条件单必须指定条件价格");
        }
    }

    /**
     * 更新或创建仓位
     */
    private FuturesPosition updateOrCreatePosition(FuturesOrder order, BigDecimal filledQuantity, 
                                                  BigDecimal avgPrice, BigDecimal margin, BigDecimal fee) {
        // 判断是开仓还是平仓
        boolean isOpenPosition = isOpenPosition(order);
        
        if (isOpenPosition) {
            // 开仓：查找是否已有相同方向的仓位
            Optional<FuturesPosition> existingPositionOpt = futuresPositionRepository
                    .findByUserIdAndPairNameAndPositionSideAndStatus(
                            order.getUserId(), order.getPairName(), order.getPositionSide(), "OPEN");

            FuturesPosition position;
            if (existingPositionOpt.isPresent()) {
                // 已有仓位：加仓
                position = existingPositionOpt.get();
                updateExistingPosition(position, order, filledQuantity, avgPrice, margin, fee);
            } else {
                // 新开仓：创建仓位
                position = createNewPosition(order, filledQuantity, avgPrice, margin, fee);
            }
            return futuresPositionRepository.save(position);
        } else {
            // 平仓：减少或关闭仓位
            return closeOrReducePosition(order, filledQuantity, avgPrice, fee);
        }
    }

    /**
     * 判断是开仓还是平仓
     */
    private boolean isOpenPosition(FuturesOrder order) {
        // 如果side和positionSide方向一致，则是开仓
        // LONG + BUY = 开多，SHORT + SELL = 开空
        // LONG + SELL = 平多，SHORT + BUY = 平空
        if ("LONG".equals(order.getPositionSide()) && "BUY".equals(order.getSide())) {
            return true; // 买入开多
        } else if ("SHORT".equals(order.getPositionSide()) && "SELL".equals(order.getSide())) {
            return true; // 卖出开空
        }
        return false; // 平仓
    }

    /**
     * 平仓或减仓
     */
    private FuturesPosition closeOrReducePosition(FuturesOrder order, BigDecimal filledQuantity, 
                                                  BigDecimal avgPrice, BigDecimal fee) {
        // 查找对应方向的仓位（平多找多，平空找空）
        String positionSide = order.getPositionSide(); // LONG 或 SHORT
        Optional<FuturesPosition> positionOpt = futuresPositionRepository
                .findByUserIdAndPairNameAndPositionSideAndStatus(
                        order.getUserId(), order.getPairName(), positionSide, "OPEN");

        if (!positionOpt.isPresent()) {
            throw new RuntimeException("没有对应的持仓仓位");
        }

        FuturesPosition position = positionOpt.get();

        // 计算盈亏
        BigDecimal pnl;
        if ("LONG".equals(positionSide)) {
            // 多头平仓：盈亏 = (平仓价格 - 开仓价格) × 数量
            pnl = avgPrice.subtract(position.getEntryPrice()).multiply(filledQuantity);
        } else {
            // 空头平仓：盈亏 = (开仓价格 - 平仓价格) × 数量
            pnl = position.getEntryPrice().subtract(avgPrice).multiply(filledQuantity);
        }

        // 计算按比例使用的保证金
        BigDecimal ratio = filledQuantity.divide(position.getQuantity(), 8, RoundingMode.HALF_UP);
        BigDecimal usedMargin = position.getMargin().multiply(ratio).setScale(8, RoundingMode.DOWN);

        // 减少仓位数量
        BigDecimal remainingQuantity = position.getQuantity().subtract(filledQuantity);

        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            // 完全平仓
            position.setStatus("CLOSED");
            position.setQuantity(BigDecimal.ZERO);
            
            // 计算返还金额 = 保证金 + 盈亏 - 手续费
            BigDecimal marginToReturn = position.getMargin().add(pnl).subtract(fee);
            if (marginToReturn.compareTo(BigDecimal.ZERO) < 0) {
                marginToReturn = BigDecimal.ZERO; // 不能为负数
            }
            
            // 更新已实现盈亏
            position.setRealizedPnl(position.getRealizedPnl().add(pnl).subtract(fee));
            
            // 返还保证金（包含盈亏，扣除手续费）
            walletService.addBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, marginToReturn);
            
            // 清空保证金（已全部返还）
            position.setMargin(BigDecimal.ZERO);
        } else {
            // 部分平仓
            // 计算返还金额 = 使用的保证金 + 盈亏 - 手续费
            BigDecimal marginToReturn = usedMargin.add(pnl).subtract(fee);
            if (marginToReturn.compareTo(BigDecimal.ZERO) < 0) {
                marginToReturn = BigDecimal.ZERO; // 不能为负数
            }
            
            // 更新仓位数量
            position.setQuantity(remainingQuantity);
            // 减少保证金（按比例）
            position.setMargin(position.getMargin().subtract(usedMargin));
            
            // 更新已实现盈亏
            position.setRealizedPnl(position.getRealizedPnl().add(pnl).subtract(fee));
            
            // 返还部分保证金（包含盈亏，扣除手续费）
            walletService.addBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, marginToReturn);
        }

        return futuresPositionRepository.save(position);
    }

    /**
     * 创建新仓位
     */
    private FuturesPosition createNewPosition(FuturesOrder order, BigDecimal quantity, 
                                             BigDecimal entryPrice, BigDecimal margin, BigDecimal fee) {
        FuturesPosition position = new FuturesPosition();
        position.setUserId(order.getUserId());
        position.setPairName(order.getPairName());
        position.setContractType("USDT_MARGINED");
        position.setPositionSide(order.getPositionSide());
        position.setMarginMode(order.getMarginMode());
        position.setLeverage(order.getLeverage());
        position.setQuantity(quantity);
        position.setEntryPrice(entryPrice);
        position.setMargin(margin);
        position.setStatus("OPEN");
        position.setUnrealizedPnl(BigDecimal.ZERO);
        position.setRealizedPnl(BigDecimal.ZERO);

        // 计算强平价格（简化处理）
        // 强平价格 = 开仓价格 × (1 - 1/杠杆倍数) （多头）
        // 或 强平价格 = 开仓价格 × (1 + 1/杠杆倍数) （空头）
        if ("LONG".equals(order.getPositionSide())) {
            BigDecimal liquidationPrice = entryPrice.multiply(
                    BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
                            new BigDecimal(order.getLeverage()), 8, RoundingMode.DOWN)));
            position.setLiquidationPrice(liquidationPrice);
        } else {
            BigDecimal liquidationPrice = entryPrice.multiply(
                    BigDecimal.ONE.add(BigDecimal.ONE.divide(
                            new BigDecimal(order.getLeverage()), 8, RoundingMode.DOWN)));
            position.setLiquidationPrice(liquidationPrice);
        }

        return position;
    }

    /**
     * 更新已有仓位（加仓）
     */
    private void updateExistingPosition(FuturesPosition position, FuturesOrder order, 
                                       BigDecimal filledQuantity, BigDecimal avgPrice, 
                                       BigDecimal margin, BigDecimal fee) {
        // 计算新的平均开仓价格
        // 新平均价格 = (原持仓价值 + 新持仓价值) / (原数量 + 新数量)
        BigDecimal oldValue = position.getQuantity().multiply(position.getEntryPrice());
        BigDecimal newValue = filledQuantity.multiply(avgPrice);
        BigDecimal totalQuantity = position.getQuantity().add(filledQuantity);
        BigDecimal newEntryPrice = oldValue.add(newValue).divide(totalQuantity, 8, RoundingMode.HALF_UP);

        position.setQuantity(totalQuantity);
        position.setEntryPrice(newEntryPrice);
        position.setMargin(position.getMargin().add(margin));

        // 重新计算强平价格
        if ("LONG".equals(position.getPositionSide())) {
            BigDecimal liquidationPrice = newEntryPrice.multiply(
                    BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
                            new BigDecimal(position.getLeverage()), 8, RoundingMode.DOWN)));
            position.setLiquidationPrice(liquidationPrice);
        } else {
            BigDecimal liquidationPrice = newEntryPrice.multiply(
                    BigDecimal.ONE.add(BigDecimal.ONE.divide(
                            new BigDecimal(position.getLeverage()), 8, RoundingMode.DOWN)));
            position.setLiquidationPrice(liquidationPrice);
        }
    }

    /**
     * 检查止损单
     */
    private void checkStopLossOrder(FuturesOrder order, BigDecimal currentPrice) {
        if (order.getStopPrice() == null) {
            return;
        }

        boolean triggered = false;
        if ("LONG".equals(order.getPositionSide())) {
            // 多头止损：当前价格 <= 止损价格
            triggered = currentPrice.compareTo(order.getStopPrice()) <= 0;
        } else {
            // 空头止损：当前价格 >= 止损价格
            triggered = currentPrice.compareTo(order.getStopPrice()) >= 0;
        }

        if (triggered) {
            BigDecimal filledQuantity = order.getQuantity();
            settleOrder(order, filledQuantity, currentPrice);
        }
    }

    /**
     * 检查止盈单
     */
    private void checkTakeProfitOrder(FuturesOrder order, BigDecimal currentPrice) {
        if (order.getTakeProfitPrice() == null) {
            return;
        }

        boolean triggered = false;
        if ("LONG".equals(order.getPositionSide())) {
            // 多头止盈：当前价格 >= 止盈价格
            triggered = currentPrice.compareTo(order.getTakeProfitPrice()) >= 0;
        } else {
            // 空头止盈：当前价格 <= 止盈价格
            triggered = currentPrice.compareTo(order.getTakeProfitPrice()) <= 0;
        }

        if (triggered) {
            BigDecimal filledQuantity = order.getQuantity();
            settleOrder(order, filledQuantity, currentPrice);
        }
    }

    /**
     * 检查止损限价单
     */
    private void checkStopLimitOrder(FuturesOrder order, BigDecimal currentPrice) {
        if (order.getStopPrice() == null || order.getLimitPrice() == null) {
            return;
        }

        // 先检查是否触发止损价格
        boolean stopTriggered = false;
        if ("LONG".equals(order.getPositionSide())) {
            stopTriggered = currentPrice.compareTo(order.getStopPrice()) <= 0;
        } else {
            stopTriggered = currentPrice.compareTo(order.getStopPrice()) >= 0;
        }

        if (stopTriggered) {
            // 止损价格触发，按限价成交
            BigDecimal filledQuantity = order.getQuantity();
            settleOrder(order, filledQuantity, order.getLimitPrice());
        }
    }

    /**
     * 检查条件单
     */
    private void checkConditionalOrder(FuturesOrder order, BigDecimal currentPrice) {
        if (order.getConditionPrice() == null) {
            return;
        }

        boolean triggered = false;
        if ("BUY".equals(order.getSide()) && "LONG".equals(order.getPositionSide())) {
            // 买入开多：当前价格 <= 条件价格
            triggered = currentPrice.compareTo(order.getConditionPrice()) <= 0;
        } else if ("SELL".equals(order.getSide()) && "SHORT".equals(order.getPositionSide())) {
            // 卖出开空：当前价格 >= 条件价格
            triggered = currentPrice.compareTo(order.getConditionPrice()) >= 0;
        } else if ("SELL".equals(order.getSide()) && "LONG".equals(order.getPositionSide())) {
            // 卖出平多：当前价格 >= 条件价格
            triggered = currentPrice.compareTo(order.getConditionPrice()) >= 0;
        } else if ("BUY".equals(order.getSide()) && "SHORT".equals(order.getPositionSide())) {
            // 买入平空：当前价格 <= 条件价格
            triggered = currentPrice.compareTo(order.getConditionPrice()) <= 0;
        }

        if (triggered) {
            // 条件满足，转为限价单撮合
            order.setOrderType("LIMIT");
            order.setPrice(order.getConditionPrice());
            matchOrders(order);
        }
    }

    /**
     * 转换为FuturesOrderResponse
     */
    private FuturesOrderResponse convertToFuturesOrderResponse(FuturesOrder order) {
        FuturesOrderResponse response = new FuturesOrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setPairName(order.getPairName());
        response.setContractType(order.getContractType());
        response.setOrderType(order.getOrderType());
        response.setSide(order.getSide());
        response.setPositionSide(order.getPositionSide());
        response.setMarginMode(order.getMarginMode());
        response.setLeverage(order.getLeverage());
        response.setPrice(order.getPrice());
        response.setQuantity(order.getQuantity());
        response.setFilledQuantity(order.getFilledQuantity());
        response.setAvgPrice(order.getAvgPrice());
        response.setStatus(order.getStatus());
        response.setFee(order.getFee());
        response.setMargin(order.getMargin());
        response.setStopPrice(order.getStopPrice());
        response.setTakeProfitPrice(order.getTakeProfitPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}




