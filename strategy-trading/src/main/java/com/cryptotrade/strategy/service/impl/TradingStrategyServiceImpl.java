/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.impl;

import com.cryptotrade.strategy.entity.TradingStrategy;
import com.cryptotrade.strategy.repository.TradingStrategyRepository;
import com.cryptotrade.strategy.service.TradingStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 交易策略服务实现类
 */
@Service
public class TradingStrategyServiceImpl implements TradingStrategyService {

    @Autowired
    private TradingStrategyRepository strategyRepository;

    @Override
    @Transactional
    public TradingStrategy createStrategy(Long userId, TradingStrategy strategy) {
        strategy.setUserId(userId);
        strategy.setStatus("STOPPED");
        strategy.setTotalProfit(java.math.BigDecimal.ZERO);
        strategy.setTotalLoss(java.math.BigDecimal.ZERO);
        strategy.setTotalTrades(0);
        strategy.setWinningTrades(0);
        strategy.setLosingTrades(0);
        if (strategy.getCurrentCapital() == null) {
            strategy.setCurrentCapital(strategy.getInitialCapital());
        }
        
        return strategyRepository.save(strategy);
    }

    @Override
    public List<TradingStrategy> getStrategies(Long userId, String marketType, String strategyType,
                                               String pairName, String status) {
        List<TradingStrategy> strategies = strategyRepository.findByUserId(userId);
        
        return strategies.stream()
                .filter(s -> marketType == null || marketType.equals(s.getMarketType()))
                .filter(s -> strategyType == null || strategyType.equals(s.getStrategyType()))
                .filter(s -> pairName == null || pairName.equals(s.getPairName()))
                .filter(s -> status == null || status.equals(s.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public TradingStrategy getStrategy(Long userId, Long strategyId) {
        Optional<TradingStrategy> strategyOpt = strategyRepository.findById(strategyId);
        TradingStrategy strategy = strategyOpt.orElseThrow(() -> new RuntimeException("策略不存在"));
        
        if (!strategy.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问该策略");
        }
        
        return strategy;
    }

    @Override
    @Transactional
    public TradingStrategy startStrategy(Long userId, Long strategyId) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if (!"STOPPED".equals(strategy.getStatus()) && !"PAUSED".equals(strategy.getStatus())) {
            throw new RuntimeException("策略状态不允许启动");
        }
        
        strategy.setStatus("RUNNING");
        strategy.setStartTime(LocalDateTime.now());
        
        return strategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public TradingStrategy stopStrategy(Long userId, Long strategyId, Boolean closePositions, Boolean cancelOrders) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if ("STOPPED".equals(strategy.getStatus())) {
            throw new RuntimeException("策略已经停止");
        }
        
        strategy.setStatus("STOPPED");
        
        // TODO: 如果需要，取消所有未成交订单
        if (cancelOrders != null && cancelOrders) {
            // 调用订单服务取消订单
        }
        
        // TODO: 如果需要，平掉所有持仓（合约市场）
        if (closePositions != null && closePositions) {
            // 调用持仓服务平仓
        }
        
        return strategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public TradingStrategy pauseStrategy(Long userId, Long strategyId) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if (!"RUNNING".equals(strategy.getStatus())) {
            throw new RuntimeException("策略状态不允许暂停");
        }
        
        strategy.setStatus("PAUSED");
        
        return strategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public TradingStrategy resumeStrategy(Long userId, Long strategyId) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if (!"PAUSED".equals(strategy.getStatus())) {
            throw new RuntimeException("策略状态不允许恢复");
        }
        
        strategy.setStatus("RUNNING");
        
        return strategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public TradingStrategy updateStrategy(Long userId, Long strategyId, TradingStrategy updateStrategy) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if ("RUNNING".equals(strategy.getStatus())) {
            throw new RuntimeException("运行中的策略无法修改参数，请先停止或暂停");
        }
        
        if (updateStrategy.getStrategyName() != null) {
            strategy.setStrategyName(updateStrategy.getStrategyName());
        }
        if (updateStrategy.getStrategyParams() != null) {
            strategy.setStrategyParams(updateStrategy.getStrategyParams());
        }
        if (updateStrategy.getInitialCapital() != null) {
            strategy.setInitialCapital(updateStrategy.getInitialCapital());
            strategy.setCurrentCapital(updateStrategy.getInitialCapital());
        }
        
        return strategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public void deleteStrategy(Long userId, Long strategyId) {
        TradingStrategy strategy = getStrategy(userId, strategyId);
        
        if (!"STOPPED".equals(strategy.getStatus())) {
            throw new RuntimeException("只能删除已停止的策略");
        }
        
        strategyRepository.delete(strategy);
    }
}













