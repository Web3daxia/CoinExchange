/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.dto.request.CoinFuturesTradingRobotRequest;
import com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot;

import java.util.List;

public interface CoinFuturesTradingRobotService {
    /**
     * 配置交易机器人
     */
    CoinFuturesTradingRobot configureRobot(Long userId, CoinFuturesTradingRobotRequest request);

    /**
     * 启动机器人
     */
    void startRobot(Long userId, Long robotId);

    /**
     * 停止机器人
     */
    void stopRobot(Long userId, Long robotId);

    /**
     * 查询用户的所有机器人
     */
    List<CoinFuturesTradingRobot> getUserRobots(Long userId);

    /**
     * 查询机器人状态
     */
    CoinFuturesTradingRobot getRobotStatus(Long userId, Long robotId);

    /**
     * 执行机器人策略（定时任务调用）
     */
    void executeRobotStrategies();

    /**
     * 执行网格交易策略
     */
    void executeGridStrategy(CoinFuturesTradingRobot robot);

    /**
     * 执行趋势跟踪策略
     */
    void executeTrendFollowingStrategy(CoinFuturesTradingRobot robot);

    /**
     * 执行反向策略
     */
    void executeReverseStrategy(CoinFuturesTradingRobot robot);
}















