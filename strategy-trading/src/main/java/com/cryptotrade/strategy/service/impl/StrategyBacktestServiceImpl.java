/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.impl;

import com.cryptotrade.strategy.entity.StrategyBacktest;
import com.cryptotrade.strategy.repository.StrategyBacktestRepository;
import com.cryptotrade.strategy.service.StrategyBacktestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 策略回测服务实现类
 */
@Service
public class StrategyBacktestServiceImpl implements StrategyBacktestService {

    @Autowired
    private StrategyBacktestRepository backtestRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public StrategyBacktest createBacktest(Long userId, Long strategyId, String startTime, String endTime,
                                           BigDecimal initialCapital) {
        // 生成回测ID
        String backtestId = "BT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        StrategyBacktest backtest = new StrategyBacktest();
        backtest.setBacktestId(backtestId);
        backtest.setStrategyId(strategyId);
        backtest.setUserId(userId);
        backtest.setStatus("PENDING");
        
        // 解析时间
        try {
            backtest.setStartTime(LocalDateTime.parse(startTime, FORMATTER));
            backtest.setEndTime(LocalDateTime.parse(endTime, FORMATTER));
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误，应为: yyyy-MM-dd HH:mm:ss");
        }
        
        if (initialCapital != null) {
            backtest.setInitialCapital(initialCapital);
        }
        
        // TODO: 从策略获取其他信息
        // Strategy strategy = strategyRepository.findById(strategyId).orElseThrow();
        // backtest.setPairName(strategy.getPairName());
        // backtest.setMarketType(strategy.getMarketType());
        
        return backtestRepository.save(backtest);
    }

    @Override
    public StrategyBacktest getBacktestStatus(String backtestId) {
        Optional<StrategyBacktest> backtestOpt = backtestRepository.findByBacktestId(backtestId);
        return backtestOpt.orElseThrow(() -> new RuntimeException("回测任务不存在"));
    }

    @Override
    public StrategyBacktest getBacktestResult(String backtestId) {
        StrategyBacktest backtest = getBacktestStatus(backtestId);
        if (!"COMPLETED".equals(backtest.getStatus())) {
            throw new RuntimeException("回测任务尚未完成");
        }
        return backtest;
    }

    @Override
    public List<StrategyBacktest> getBacktestList(Long userId, Long strategyId, String status) {
        List<StrategyBacktest> backtests;
        
        if (strategyId != null) {
            backtests = backtestRepository.findByStrategyId(strategyId);
        } else {
            backtests = backtestRepository.findByUserId(userId);
        }
        
        if (status != null) {
            backtests = backtests.stream()
                    .filter(b -> status.equals(b.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return backtests;
    }
}













