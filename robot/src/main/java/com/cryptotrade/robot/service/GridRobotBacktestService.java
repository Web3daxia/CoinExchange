/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.entity.GridRobotBacktest;
import com.cryptotrade.robot.entity.RobotTradeRecord;

import java.util.List;

/**
 * 网格机器人回测服务接口
 */
public interface GridRobotBacktestService {
    /**
     * 创建回测任务
     */
    GridRobotBacktest createBacktest(Long userId, Long robotId, GridRobotBacktest backtest);

    /**
     * 查询回测任务状态
     */
    GridRobotBacktest getBacktestStatus(String backtestId);

    /**
     * 查询回测结果
     */
    GridRobotBacktest getBacktestResult(String backtestId);

    /**
     * 查询回测交易记录
     */
    List<RobotTradeRecord> getBacktestTrades(String backtestId);

    /**
     * 查询用户回测任务列表
     */
    List<GridRobotBacktest> getUserBacktests(Long userId, Long robotId, String status);

    /**
     * 执行回测
     */
    void executeBacktest(String backtestId);
}













