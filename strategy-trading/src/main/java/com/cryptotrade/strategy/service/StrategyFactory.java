/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略工厂
 * 管理所有策略实例，根据策略类型获取对应的策略服务
 */
@Component
public class StrategyFactory {

    @Autowired
    private List<StrategyService> strategyServices;

    private Map<String, StrategyService> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        // 将所有策略注册到Map中
        for (StrategyService strategy : strategyServices) {
            strategyMap.put(strategy.getStrategyType(), strategy);
        }
    }

    /**
     * 根据策略类型获取策略服务
     * @param strategyType 策略类型
     * @return 策略服务
     */
    public StrategyService getStrategy(String strategyType) {
        StrategyService strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的策略类型: " + strategyType);
        }
        return strategy;
    }

    /**
     * 获取所有支持的策略类型
     * @return 策略类型列表
     */
    public java.util.Set<String> getSupportedStrategyTypes() {
        return strategyMap.keySet();
    }

    /**
     * 检查策略类型是否支持
     * @param strategyType 策略类型
     * @return 是否支持
     */
    public boolean isStrategySupported(String strategyType) {
        return strategyMap.containsKey(strategyType);
    }
}















