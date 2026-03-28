/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.entity.GridRobotBacktest;
import com.cryptotrade.robot.entity.GridRobotSettlement;
import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 网格机器人服务接口
 */
public interface GridRobotService {
    /**
     * 创建现货网格机器人
     */
    TradingRobot createSpotGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                     BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                     BigDecimal initialCapital, BigDecimal investmentRatio,
                                     BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                     BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage);

    /**
     * 创建USDT本位合约网格机器人
     */
    TradingRobot createFuturesUsdtGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                            BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                            BigDecimal initialCapital, BigDecimal investmentRatio,
                                            Integer leverage, String marginMode,
                                            BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                            BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage);

    /**
     * 创建币本位合约网格机器人
     */
    TradingRobot createFuturesCoinGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                            BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                            BigDecimal initialCapital, BigDecimal investmentRatio,
                                            Integer leverage, String marginMode,
                                            BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                            BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage);

    /**
     * 启动网格机器人
     */
    TradingRobot startRobot(Long userId, Long robotId);

    /**
     * 暂停网格机器人
     */
    TradingRobot pauseRobot(Long userId, Long robotId);

    /**
     * 恢复网格机器人
     */
    TradingRobot resumeRobot(Long userId, Long robotId);

    /**
     * 停止网格机器人
     */
    TradingRobot stopRobot(Long userId, Long robotId, Boolean closePositions, Boolean cancelOrders);

    /**
     * 更新网格机器人参数
     */
    TradingRobot updateRobotSettings(Long userId, Long robotId, String robotName, Integer gridCount,
                                     BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                     BigDecimal initialCapital, BigDecimal investmentRatio,
                                     BigDecimal stopLossPrice, BigDecimal takeProfitPrice);

    /**
     * 智能调整网格参数
     */
    GridRobotAdjustmentHistory autoAdjustRobot(Long userId, Long robotId, String adjustType,
                                                Integer volatilityPeriod, Integer targetGridCount,
                                                Boolean adjustPriceRange);

    /**
     * 结算网格机器人
     */
    GridRobotSettlement settleRobot(Long userId, Long robotId);

    /**
     * 删除网格机器人
     */
    void deleteRobot(Long userId, Long robotId);

    /**
     * 创建回测任务
     */
    GridRobotBacktest createBacktest(Long userId, Long robotId, String pairName, String marketType,
                                     Integer gridCount, BigDecimal upperPrice, BigDecimal lowerPrice,
                                     BigDecimal startPrice, BigDecimal initialCapital, BigDecimal investmentRatio,
                                     String startTime, String endTime, BigDecimal stopLossPrice,
                                     BigDecimal takeProfitPrice, Integer leverage, String marginMode);

    /**
     * 查询回测任务状态
     */
    GridRobotBacktest getBacktestStatus(String backtestId);

    /**
     * 查询回测结果
     */
    GridRobotBacktest getBacktestResult(String backtestId);
}













