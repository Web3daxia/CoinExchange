/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service;

import com.cryptotrade.strategy.entity.StrategyBacktest;

import java.util.List;

/**
 * 策略回测服务接口
 */
public interface StrategyBacktestService {
    /**
     * 创建回测任务
     */
    StrategyBacktest createBacktest(Long userId, Long strategyId, String startTime, String endTime, 
                                    java.math.BigDecimal initialCapital);

    /**
     * 查询回测任务状态
     */
    StrategyBacktest getBacktestStatus(String backtestId);

    /**
     * 查询回测结果
     */
    StrategyBacktest getBacktestResult(String backtestId);

    /**
     * 查询回测任务列表
     */
    List<StrategyBacktest> getBacktestList(Long userId, Long strategyId, String status);
}













