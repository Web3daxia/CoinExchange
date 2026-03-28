/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedStrategy;
import com.cryptotrade.leveraged.repository.LeveragedStrategyRepository;
import com.cryptotrade.leveraged.service.LeveragedOrderService;
import com.cryptotrade.leveraged.service.LeveragedStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 杠杆交易策略服务实现类
 */
@Service
public class LeveragedStrategyServiceImpl implements LeveragedStrategyService {

    @Autowired
    private LeveragedStrategyRepository leveragedStrategyRepository;

    @Autowired
    private LeveragedOrderService leveragedOrderService;

    @Override
    @Transactional
    public LeveragedStrategy configureStrategy(Long userId, Long accountId, String strategyName,
                                              String strategyType, String pairName, String strategyParams,
                                              Integer leverage) {
        LeveragedStrategy strategy = new LeveragedStrategy();
        strategy.setUserId(userId);
        strategy.setAccountId(accountId);
        strategy.setStrategyName(strategyName);
        strategy.setStrategyType(strategyType);
        strategy.setPairName(pairName);
        strategy.setStrategyParams(strategyParams);
        strategy.setLeverage(leverage);
        strategy.setStatus("ACTIVE");
        strategy.setTotalProfit(BigDecimal.ZERO);
        strategy.setTotalLoss(BigDecimal.ZERO);

        return leveragedStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public void executeStrategy(Long strategyId) {
        Optional<LeveragedStrategy> strategyOpt = leveragedStrategyRepository.findById(strategyId);
        if (!strategyOpt.isPresent()) {
            return;
        }

        LeveragedStrategy strategy = strategyOpt.get();
        if (!"ACTIVE".equals(strategy.getStatus())) {
            return;
        }

        // TODO: 根据策略类型执行相应的交易逻辑
        // GRID: 网格交易
        // TREND_FOLLOWING: 趋势跟踪
        // REVERSE: 反转策略

        // 更新最后执行时间
        strategy.setLastExecutionTime(LocalDateTime.now());
        leveragedStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public void stopStrategy(Long userId, Long strategyId) {
        LeveragedStrategy strategy = leveragedStrategyRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("策略不存在"));

        if (!strategy.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此策略");
        }

        strategy.setStatus("STOPPED");
        leveragedStrategyRepository.save(strategy);
    }

    @Override
    public List<LeveragedStrategy> getUserStrategies(Long userId) {
        return leveragedStrategyRepository.findByUserId(userId);
    }

    @Override
    @Scheduled(fixedRate = 30000) // 每30秒执行一次
    public void executeAllStrategies() {
        List<LeveragedStrategy> activeStrategies = leveragedStrategyRepository.findByStatus("ACTIVE");
        for (LeveragedStrategy strategy : activeStrategies) {
            try {
                executeStrategy(strategy.getId());
            } catch (Exception e) {
                System.err.println("执行策略失败: " + strategy.getId() + ", " + e.getMessage());
            }
        }
    }
}

