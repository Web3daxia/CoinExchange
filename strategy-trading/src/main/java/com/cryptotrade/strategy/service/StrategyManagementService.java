/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略管理服务
 */
@Service
public class StrategyManagementService {

    @Autowired
    private StrategyFactory strategyFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行策略
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param marketType 市场类型
     * @param strategyType 策略类型
     * @param strategyParams 策略参数（JSON字符串）
     * @throws Exception 执行异常
     */
    public void executeStrategy(Long userId, String pairName, String marketType,
                               String strategyType, String strategyParams) throws Exception {
        // 获取策略服务
        StrategyService strategy = strategyFactory.getStrategy(strategyType);

        // 解析策略参数
        Map<String, Object> params = parseStrategyParams(strategyParams);

        // 执行策略
        strategy.execute(userId, pairName, marketType, params);
    }

    /**
     * 解析策略参数
     */
    private Map<String, Object> parseStrategyParams(String strategyParams) {
        try {
            if (strategyParams == null || strategyParams.isEmpty()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(strategyParams, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * 获取所有支持的策略类型
     */
    public java.util.Set<String> getSupportedStrategyTypes() {
        return strategyFactory.getSupportedStrategyTypes();
    }
}















