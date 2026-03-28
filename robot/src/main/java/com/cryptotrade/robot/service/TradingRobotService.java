/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import com.cryptotrade.robot.dto.request.TradingRobotRequest;
import com.cryptotrade.robot.entity.TradingRobot;

import java.util.List;

public interface TradingRobotService {
    /**
     * 配置交易机器人
     */
    TradingRobot configureRobot(Long userId, TradingRobotRequest request);

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
    List<TradingRobot> getUserRobots(Long userId);

    /**
     * 查询机器人状态
     */
    TradingRobot getRobotStatus(Long userId, Long robotId);

    /**
     * 执行机器人策略
     */
    void executeRobotStrategies();

    /**
     * 执行网格交易策略
     */
    void executeGridStrategy(TradingRobot robot);

    /**
     * 执行趋势跟踪策略
     */
    void executeTrendFollowingStrategy(TradingRobot robot);

    /**
     * 执行反向策略
     */
    void executeReverseStrategy(TradingRobot robot);

    /**
     * 查询交易机器人的历史交易记录
     * @param userId 用户ID
     * @param robotId 机器人ID（可选，如果为空则查询用户所有机器人的记录）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 交易记录列表
     */
    List<com.cryptotrade.robot.entity.RobotTradeRecord> getRobotHistory(
            Long userId, Long robotId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 记录机器人交易
     * @param robot 机器人
     * @param marketType 市场类型
     * @param orderId 订单ID
     * @param action 操作
     * @param side 方向
     * @param quantity 数量
     * @param price 价格
     * @param amount 金额
     * @param fee 手续费
     * @param profitLoss 盈亏
     */
    void recordTrade(com.cryptotrade.robot.entity.TradingRobot robot, String marketType, Long orderId,
                    String action, String side, java.math.BigDecimal quantity, java.math.BigDecimal price,
                    java.math.BigDecimal amount, java.math.BigDecimal fee, java.math.BigDecimal profitLoss);
}

