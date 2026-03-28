/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.entity.OptionOrder;
import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.entity.OptionStrategy;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.repository.OptionOrderRepository;
import com.cryptotrade.options.repository.OptionPositionRepository;
import com.cryptotrade.options.repository.OptionStrategyRepository;
import com.cryptotrade.options.service.OptionOrderService;
import com.cryptotrade.options.service.OptionStrategyService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 期权策略服务实现类
 */
@Service
public class OptionStrategyServiceImpl implements OptionStrategyService {

    @Autowired
    private OptionStrategyRepository optionStrategyRepository;

    @Autowired
    private OptionContractRepository optionContractRepository;

    @Autowired
    private OptionOrderRepository optionOrderRepository;

    @Autowired
    private OptionPositionRepository optionPositionRepository;

    @Autowired
    private OptionOrderService optionOrderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public OptionStrategy createStraddleStrategy(Long userId, String pairName, BigDecimal strikePrice,
                                                LocalDateTime expiryDate, BigDecimal quantity) {
        // 跨式策略：同时买入相同执行价格、相同到期日的看涨和看跌期权
        // 需要创建两个期权合约（如果不存在）
        OptionContract callContract = findOrCreateContract(pairName, "CALL", strikePrice, expiryDate);
        OptionContract putContract = findOrCreateContract(pairName, "PUT", strikePrice, expiryDate);

        // 创建看涨期权订单
        OptionOrder callOrder = optionOrderService.createOrder(userId, callContract.getId(), "OPEN", "BUY",
                quantity, callContract.getCurrentPrice());

        // 创建看跌期权订单
        OptionOrder putOrder = optionOrderService.createOrder(userId, putContract.getId(), "OPEN", "BUY",
                quantity, putContract.getCurrentPrice());

        // 执行订单
        optionOrderService.executeOrder(callOrder.getId());
        optionOrderService.executeOrder(putOrder.getId());

        // 创建策略记录
        OptionStrategy strategy = new OptionStrategy();
        strategy.setUserId(userId);
        strategy.setStrategyName("跨式期权策略");
        strategy.setStrategyType("STRADDLE");
        strategy.setPairName(pairName);
        strategy.setStatus("ACTIVE");
        strategy.setExpiryDate(expiryDate);

        Map<String, Object> params = new HashMap<>();
        params.put("strikePrice", strikePrice);
        params.put("quantity", quantity);
        params.put("callContractId", callContract.getId());
        params.put("putContractId", putContract.getId());
        params.put("callOrderId", callOrder.getId());
        params.put("putOrderId", putOrder.getId());

        try {
            strategy.setStrategyParams(objectMapper.writeValueAsString(params));
        } catch (Exception e) {
            throw new RuntimeException("序列化策略参数失败", e);
        }

        BigDecimal totalCost = callContract.getCurrentPrice().add(putContract.getCurrentPrice()).multiply(quantity);
        strategy.setTotalCost(totalCost);
        strategy.setCurrentValue(totalCost);
        strategy.setUnrealizedPnl(BigDecimal.ZERO);
        strategy.setRealizedPnl(BigDecimal.ZERO);

        return optionStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public OptionStrategy createButterflyStrategy(Long userId, String pairName, BigDecimal lowerStrike,
                                                 BigDecimal middleStrike, BigDecimal upperStrike,
                                                 LocalDateTime expiryDate, BigDecimal quantity, String optionType) {
        // 蝶式策略：买入一个较低执行价格的期权，卖出两个中间执行价格的期权，买入一个较高执行价格的期权
        OptionContract lowerContract = findOrCreateContract(pairName, optionType, lowerStrike, expiryDate);
        OptionContract middleContract = findOrCreateContract(pairName, optionType, middleStrike, expiryDate);
        OptionContract upperContract = findOrCreateContract(pairName, optionType, upperStrike, expiryDate);

        // 买入较低执行价格期权
        OptionOrder lowerOrder = optionOrderService.createOrder(userId, lowerContract.getId(), "OPEN", "BUY",
                quantity, lowerContract.getCurrentPrice());
        optionOrderService.executeOrder(lowerOrder.getId());

        // 卖出两个中间执行价格期权
        OptionOrder middleOrder1 = optionOrderService.createOrder(userId, middleContract.getId(), "OPEN", "SELL",
                quantity, middleContract.getCurrentPrice());
        OptionOrder middleOrder2 = optionOrderService.createOrder(userId, middleContract.getId(), "OPEN", "SELL",
                quantity, middleContract.getCurrentPrice());
        optionOrderService.executeOrder(middleOrder1.getId());
        optionOrderService.executeOrder(middleOrder2.getId());

        // 买入较高执行价格期权
        OptionOrder upperOrder = optionOrderService.createOrder(userId, upperContract.getId(), "OPEN", "BUY",
                quantity, upperContract.getCurrentPrice());
        optionOrderService.executeOrder(upperOrder.getId());

        // 创建策略记录
        OptionStrategy strategy = new OptionStrategy();
        strategy.setUserId(userId);
        strategy.setStrategyName("蝶式期权策略");
        strategy.setStrategyType("BUTTERFLY");
        strategy.setPairName(pairName);
        strategy.setStatus("ACTIVE");
        strategy.setExpiryDate(expiryDate);

        Map<String, Object> params = new HashMap<>();
        params.put("lowerStrike", lowerStrike);
        params.put("middleStrike", middleStrike);
        params.put("upperStrike", upperStrike);
        params.put("quantity", quantity);
        params.put("optionType", optionType);
        params.put("lowerContractId", lowerContract.getId());
        params.put("middleContractId", middleContract.getId());
        params.put("upperContractId", upperContract.getId());

        try {
            strategy.setStrategyParams(objectMapper.writeValueAsString(params));
        } catch (Exception e) {
            throw new RuntimeException("序列化策略参数失败", e);
        }

        BigDecimal totalCost = lowerContract.getCurrentPrice()
                .subtract(middleContract.getCurrentPrice().multiply(new BigDecimal("2")))
                .add(upperContract.getCurrentPrice())
                .multiply(quantity);
        strategy.setTotalCost(totalCost);
        strategy.setCurrentValue(totalCost);
        strategy.setUnrealizedPnl(BigDecimal.ZERO);
        strategy.setRealizedPnl(BigDecimal.ZERO);

        return optionStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public OptionStrategy createVerticalSpreadStrategy(Long userId, String pairName, BigDecimal buyStrike,
                                                      BigDecimal sellStrike, LocalDateTime expiryDate,
                                                      BigDecimal quantity, String optionType, String direction) {
        // 价差策略：买入一个执行价格的期权，卖出另一个执行价格的期权
        OptionContract buyContract = findOrCreateContract(pairName, optionType, buyStrike, expiryDate);
        OptionContract sellContract = findOrCreateContract(pairName, optionType, sellStrike, expiryDate);

        // 买入期权
        OptionOrder buyOrder = optionOrderService.createOrder(userId, buyContract.getId(), "OPEN", "BUY",
                quantity, buyContract.getCurrentPrice());
        optionOrderService.executeOrder(buyOrder.getId());

        // 卖出期权
        OptionOrder sellOrder = optionOrderService.createOrder(userId, sellContract.getId(), "OPEN", "SELL",
                quantity, sellContract.getCurrentPrice());
        optionOrderService.executeOrder(sellOrder.getId());

        // 创建策略记录
        OptionStrategy strategy = new OptionStrategy();
        strategy.setUserId(userId);
        strategy.setStrategyName(direction.equals("BULLISH") ? "牛市价差策略" : "熊市价差策略");
        strategy.setStrategyType("VERTICAL");
        strategy.setPairName(pairName);
        strategy.setStatus("ACTIVE");
        strategy.setExpiryDate(expiryDate);

        Map<String, Object> params = new HashMap<>();
        params.put("buyStrike", buyStrike);
        params.put("sellStrike", sellStrike);
        params.put("quantity", quantity);
        params.put("optionType", optionType);
        params.put("direction", direction);
        params.put("buyContractId", buyContract.getId());
        params.put("sellContractId", sellContract.getId());

        try {
            strategy.setStrategyParams(objectMapper.writeValueAsString(params));
        } catch (Exception e) {
            throw new RuntimeException("序列化策略参数失败", e);
        }

        BigDecimal totalCost = buyContract.getCurrentPrice().subtract(sellContract.getCurrentPrice()).multiply(quantity);
        strategy.setTotalCost(totalCost);
        strategy.setCurrentValue(totalCost);
        strategy.setUnrealizedPnl(BigDecimal.ZERO);
        strategy.setRealizedPnl(BigDecimal.ZERO);

        return optionStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public OptionStrategy createCalendarSpreadStrategy(Long userId, String pairName, BigDecimal strikePrice,
                                                      LocalDateTime nearExpiryDate, LocalDateTime farExpiryDate,
                                                      BigDecimal quantity, String optionType) {
        // 日历价差策略：卖出近期到期期权，买入远期到期期权
        OptionContract nearContract = findOrCreateContract(pairName, optionType, strikePrice, nearExpiryDate);
        OptionContract farContract = findOrCreateContract(pairName, optionType, strikePrice, farExpiryDate);

        // 卖出近期期权
        OptionOrder nearOrder = optionOrderService.createOrder(userId, nearContract.getId(), "OPEN", "SELL",
                quantity, nearContract.getCurrentPrice());
        optionOrderService.executeOrder(nearOrder.getId());

        // 买入远期期权
        OptionOrder farOrder = optionOrderService.createOrder(userId, farContract.getId(), "OPEN", "BUY",
                quantity, farContract.getCurrentPrice());
        optionOrderService.executeOrder(farOrder.getId());

        // 创建策略记录
        OptionStrategy strategy = new OptionStrategy();
        strategy.setUserId(userId);
        strategy.setStrategyName("日历价差策略");
        strategy.setStrategyType("CALENDAR");
        strategy.setPairName(pairName);
        strategy.setStatus("ACTIVE");
        strategy.setExpiryDate(farExpiryDate);

        Map<String, Object> params = new HashMap<>();
        params.put("strikePrice", strikePrice);
        params.put("quantity", quantity);
        params.put("optionType", optionType);
        params.put("nearExpiryDate", nearExpiryDate.toString());
        params.put("farExpiryDate", farExpiryDate.toString());
        params.put("nearContractId", nearContract.getId());
        params.put("farContractId", farContract.getId());

        try {
            strategy.setStrategyParams(objectMapper.writeValueAsString(params));
        } catch (Exception e) {
            throw new RuntimeException("序列化策略参数失败", e);
        }

        BigDecimal totalCost = farContract.getCurrentPrice().subtract(nearContract.getCurrentPrice()).multiply(quantity);
        strategy.setTotalCost(totalCost);
        strategy.setCurrentValue(totalCost);
        strategy.setUnrealizedPnl(BigDecimal.ZERO);
        strategy.setRealizedPnl(BigDecimal.ZERO);

        return optionStrategyRepository.save(strategy);
    }

    @Override
    public List<OptionStrategy> getUserStrategies(Long userId) {
        return optionStrategyRepository.findByUserId(userId);
    }

    @Override
    public OptionStrategy getStrategy(Long userId, Long strategyId) {
        OptionStrategy strategy = optionStrategyRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("策略不存在"));

        if (!strategy.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此策略");
        }

        return strategy;
    }

    @Override
    @Transactional
    public void closeStrategy(Long userId, Long strategyId) {
        OptionStrategy strategy = getStrategy(userId, strategyId);

        if (!"ACTIVE".equals(strategy.getStatus())) {
            throw new RuntimeException("策略状态不允许关闭");
        }

        // TODO: 平仓策略中的所有持仓
        // 这里需要根据策略类型和参数，找到所有相关持仓并平仓

        strategy.setStatus("CLOSED");
        optionStrategyRepository.save(strategy);
    }

    @Override
    public void updateStrategyValue(Long strategyId) {
        OptionStrategy strategy = optionStrategyRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("策略不存在"));

        // TODO: 根据当前市场价格更新策略价值
        // 需要从策略参数中获取所有相关合约，计算当前总价值

        optionStrategyRepository.save(strategy);
    }

    /**
     * 查找或创建期权合约
     */
    private OptionContract findOrCreateContract(String pairName, String optionType, BigDecimal strikePrice,
                                               LocalDateTime expiryDate) {
        Optional<OptionContract> contractOpt = optionContractRepository.findByPairNameAndOptionTypeAndStrikePriceAndExpiryDate(
                pairName, optionType, strikePrice, expiryDate);

        if (contractOpt.isPresent()) {
            return contractOpt.get();
        }

        // 创建新合约
        String[] parts = pairName.split("/");
        OptionContract contract = new OptionContract();
        contract.setPairName(pairName);
        contract.setBaseCurrency(parts[0]);
        contract.setQuoteCurrency(parts[1]);
        contract.setOptionType(optionType);
        contract.setExerciseType("EUROPEAN"); // 默认欧式期权
        contract.setStrikePrice(strikePrice);
        contract.setExpiryDate(expiryDate);
        contract.setStatus("ACTIVE");
        contract.setCurrentPrice(BigDecimal.ZERO); // 需要从市场获取
        contract.setUnderlyingPrice(BigDecimal.ZERO); // 需要从市场获取

        return optionContractRepository.save(contract);
    }
}















