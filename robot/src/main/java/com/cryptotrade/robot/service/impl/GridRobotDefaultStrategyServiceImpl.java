/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotDefaultStrategy;
import com.cryptotrade.robot.repository.GridRobotDefaultStrategyRepository;
import com.cryptotrade.robot.service.GridRobotDefaultStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 网格机器人默认策略服务实现类
 */
@Service
public class GridRobotDefaultStrategyServiceImpl implements GridRobotDefaultStrategyService {

    @Autowired
    private GridRobotDefaultStrategyRepository defaultStrategyRepository;

    @Override
    public List<GridRobotDefaultStrategy> getDefaultStrategies(String marketType) {
        if (marketType != null) {
            return defaultStrategyRepository.findByMarketTypeAndStatus(marketType, "ACTIVE");
        }
        return defaultStrategyRepository.findByStatus("ACTIVE");
    }

    @Override
    public GridRobotDefaultStrategy getDefaultStrategy(Long strategyId) {
        Optional<GridRobotDefaultStrategy> strategyOpt = defaultStrategyRepository.findById(strategyId);
        return strategyOpt.orElseThrow(() -> new RuntimeException("默认策略不存在"));
    }

    @Override
    @Transactional
    public GridRobotDefaultStrategy createDefaultStrategy(GridRobotDefaultStrategy strategy) {
        if (strategy.getStatus() == null) {
            strategy.setStatus("ACTIVE");
        }
        return defaultStrategyRepository.save(strategy);
    }

    @Override
    @Transactional
    public GridRobotDefaultStrategy updateDefaultStrategy(Long strategyId, GridRobotDefaultStrategy strategy) {
        GridRobotDefaultStrategy existingStrategy = getDefaultStrategy(strategyId);
        
        if (strategy.getStrategyName() != null) {
            existingStrategy.setStrategyName(strategy.getStrategyName());
        }
        if (strategy.getGridCount() != null) {
            existingStrategy.setGridCount(strategy.getGridCount());
        }
        if (strategy.getStatus() != null) {
            existingStrategy.setStatus(strategy.getStatus());
        }
        
        return defaultStrategyRepository.save(existingStrategy);
    }

    @Override
    @Transactional
    public void deleteDefaultStrategy(Long strategyId) {
        GridRobotDefaultStrategy strategy = getDefaultStrategy(strategyId);
        defaultStrategyRepository.delete(strategy);
    }
}













