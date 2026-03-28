/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.dto.request.TradingRobotRequest;
import com.cryptotrade.robot.entity.RobotTradeRecord;
import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.repository.RobotTradeRecordRepository;
import com.cryptotrade.robot.repository.TradingRobotRepository;
import com.cryptotrade.robot.service.TradingRobotService;
import com.cryptotrade.robot.service.strategy.GridStrategy;
import com.cryptotrade.robot.service.strategy.ReverseStrategy;
import com.cryptotrade.robot.service.strategy.TrendFollowingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TradingRobotServiceImpl implements TradingRobotService {

    @Autowired
    private TradingRobotRepository tradingRobotRepository;

    @Autowired
    private RobotTradeRecordRepository robotTradeRecordRepository;

    @Autowired
    private GridStrategy gridStrategy;

    @Autowired
    private TrendFollowingStrategy trendFollowingStrategy;

    @Autowired
    private ReverseStrategy reverseStrategy;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public TradingRobot configureRobot(Long userId, TradingRobotRequest request) {
        TradingRobot robot = new TradingRobot();
        robot.setUserId(userId);
        robot.setRobotName(request.getRobotName());
        robot.setPairName(request.getPairName());
        robot.setMarketType(request.getMarketType() != null ? request.getMarketType() : "SPOT");
        robot.setStrategyType(request.getStrategyType());
        robot.setStatus("STOPPED");
        robot.setMaxLoss(request.getMaxLoss());
        robot.setMaxPosition(request.getMaxPosition());
        robot.setStopLossPrice(request.getStopLossPrice());
        robot.setTakeProfitPrice(request.getTakeProfitPrice());
        robot.setOrderAmount(request.getOrderAmount());
        robot.setOrderQuantity(request.getOrderQuantity());
        robot.setTotalProfit(BigDecimal.ZERO);
        robot.setTotalLoss(BigDecimal.ZERO);

        // 保存策略参数（JSON格式）
        try {
            Map<String, Object> strategyParams = request.getStrategyParams() != null ? 
                    request.getStrategyParams() : new HashMap<>();
            robot.setStrategyParams(objectMapper.writeValueAsString(strategyParams));
        } catch (Exception e) {
            robot.setStrategyParams("{}");
        }

        return tradingRobotRepository.save(robot);
    }

    @Override
    @Transactional
    public void startRobot(Long userId, Long robotId) {
        TradingRobot robot = tradingRobotRepository.findById(robotId)
                .orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }

        robot.setStatus("RUNNING");
        robot.setLastExecutionTime(LocalDateTime.now());
        tradingRobotRepository.save(robot);
    }

    @Override
    @Transactional
    public void stopRobot(Long userId, Long robotId) {
        TradingRobot robot = tradingRobotRepository.findById(robotId)
                .orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }

        robot.setStatus("STOPPED");
        tradingRobotRepository.save(robot);
    }

    @Override
    public List<TradingRobot> getUserRobots(Long userId) {
        return tradingRobotRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public TradingRobot getRobotStatus(Long userId, Long robotId) {
        TradingRobot robot = tradingRobotRepository.findById(robotId)
                .orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此机器人");
        }

        return robot;
    }

    @Override
    @Transactional
    public void executeRobotStrategies() {
        // 查找所有运行中的机器人
        List<TradingRobot> runningRobots = tradingRobotRepository.findByStatus("RUNNING");

        for (TradingRobot robot : runningRobots) {
            try {
                // 检查风险控制
                if (!checkRiskControl(robot)) {
                    robot.setStatus("STOPPED");
                    tradingRobotRepository.save(robot);
                    continue;
                }

                // 根据策略类型执行相应策略
                switch (robot.getStrategyType()) {
                    case "GRID":
                        executeGridStrategy(robot);
                        break;
                    case "TREND_FOLLOWING":
                        executeTrendFollowingStrategy(robot);
                        break;
                    case "REVERSE":
                        executeReverseStrategy(robot);
                        break;
                    default:
                        System.err.println("未知的策略类型: " + robot.getStrategyType());
                }

                // 更新最后执行时间
                robot.setLastExecutionTime(LocalDateTime.now());
                tradingRobotRepository.save(robot);
            } catch (Exception e) {
                // 记录错误，继续处理下一个机器人
                System.err.println("执行机器人策略失败 (ID: " + robot.getId() + "): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void executeGridStrategy(TradingRobot robot) {
        try {
            gridStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行网格交易策略失败: " + e.getMessage());
            // 策略执行失败时，可以选择暂停机器人或保持运行状态
            // robot.setStatus("PAUSED");
        }
    }

    @Override
    @Transactional
    public void executeTrendFollowingStrategy(TradingRobot robot) {
        try {
            trendFollowingStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行趋势跟踪策略失败: " + e.getMessage());
            // robot.setStatus("PAUSED");
        }
    }

    @Override
    @Transactional
    public void executeReverseStrategy(TradingRobot robot) {
        try {
            reverseStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行反向策略失败: " + e.getMessage());
            // robot.setStatus("PAUSED");
        }
    }

    /**
     * 检查风险控制
     */
    private boolean checkRiskControl(TradingRobot robot) {
        // 检查最大亏损
        if (robot.getMaxLoss() != null && robot.getTotalLoss().compareTo(robot.getMaxLoss()) >= 0) {
            System.out.println("机器人达到最大亏损限制，自动停止: " + robot.getId());
            return false;
        }

        // TODO: 可以添加其他风险控制检查
        // 例如：检查最大持仓、检查止损价格等

        return true;
    }

    @Override
    public List<RobotTradeRecord> getRobotHistory(Long userId, Long robotId,
                                                   LocalDateTime startTime, LocalDateTime endTime) {
        if (robotId != null) {
            // 验证机器人属于该用户
            TradingRobot robot = tradingRobotRepository.findById(robotId)
                    .orElseThrow(() -> new RuntimeException("机器人不存在"));
            if (!robot.getUserId().equals(userId)) {
                throw new RuntimeException("无权查看此机器人的交易记录");
            }

            if (startTime != null && endTime != null) {
                return robotTradeRecordRepository.findByRobotIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        robotId, startTime, endTime);
            } else {
                return robotTradeRecordRepository.findByRobotIdOrderByCreatedAtDesc(robotId);
            }
        } else {
            // 查询用户所有机器人的交易记录
            if (startTime != null && endTime != null) {
                return robotTradeRecordRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        userId, startTime, endTime);
            } else {
                return robotTradeRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
            }
        }
    }

    @Override
    @Transactional
    public void recordTrade(TradingRobot robot, String marketType, Long orderId, String action, String side,
                           BigDecimal quantity, BigDecimal price, BigDecimal amount, BigDecimal fee,
                           BigDecimal profitLoss) {
        RobotTradeRecord record = new RobotTradeRecord();
        record.setRobotId(robot.getId());
        record.setUserId(robot.getUserId());
        record.setMarketType(marketType);
        record.setOrderId(orderId);
        record.setPairName(robot.getPairName());
        record.setAction(action);
        record.setSide(side);
        record.setQuantity(quantity);
        record.setPrice(price);
        record.setAmount(amount);
        record.setFee(fee);
        record.setProfitLoss(profitLoss);
        record.setStrategyType(robot.getStrategyType());

        robotTradeRecordRepository.save(record);

        // 更新机器人的总盈利和总亏损
        if (profitLoss != null) {
            if (profitLoss.compareTo(BigDecimal.ZERO) > 0) {
                robot.setTotalProfit(robot.getTotalProfit().add(profitLoss));
            } else {
                robot.setTotalLoss(robot.getTotalLoss().add(profitLoss.abs()));
            }
            tradingRobotRepository.save(robot);
        }
    }
}

