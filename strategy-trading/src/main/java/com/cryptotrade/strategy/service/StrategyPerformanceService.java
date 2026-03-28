/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 策略表现服务接口
 */
public interface StrategyPerformanceService {
    /**
     * 查询策略历史表现
     */
    Map<String, Object> getPerformance(Long strategyId, String period);

    /**
     * 查询策略收益趋势
     */
    Map<String, Object> getProfitTrend(Long strategyId, String period, String interval);

    /**
     * 策略优化建议
     */
    Map<String, Object> optimizeStrategy(Long strategyId, String backtestId, String optimizeTarget);
}













