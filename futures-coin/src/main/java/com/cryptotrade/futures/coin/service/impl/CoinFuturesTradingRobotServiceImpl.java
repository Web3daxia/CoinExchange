/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.dto.request.CoinFuturesTradingRobotRequest;
import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot;
import com.cryptotrade.futures.coin.repository.CoinFuturesTradingRobotRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesMarketDataService;
import com.cryptotrade.futures.coin.service.CoinFuturesOrderService;
import com.cryptotrade.futures.coin.service.CoinFuturesTradingRobotService;
import com.cryptotrade.futures.coin.service.strategy.CoinFuturesGridStrategy;
import com.cryptotrade.futures.coin.service.strategy.CoinFuturesReverseStrategy;
import com.cryptotrade.futures.coin.service.strategy.CoinFuturesTrendFollowingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoinFuturesTradingRobotServiceImpl implements CoinFuturesTradingRobotService {

    @Autowired
    private CoinFuturesTradingRobotRepository coinFuturesTradingRobotRepository;

    @Autowired
    private CoinFuturesGridStrategy gridStrategy;

    @Autowired
    private CoinFuturesTrendFollowingStrategy trendFollowingStrategy;

    @Autowired
    private CoinFuturesReverseStrategy reverseStrategy;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public CoinFuturesTradingRobot configureRobot(Long userId, CoinFuturesTradingRobotRequest request) {
        CoinFuturesTradingRobot robot = new CoinFuturesTradingRobot();
        robot.setUserId(userId);
        robot.setRobotName(request.getRobotName());
        robot.setPairName(request.getPairName());
        robot.setStrategyType(request.getStrategyType());
        robot.setStatus("STOPPED");
        robot.setMaxLoss(request.getMaxLoss());
        robot.setMaxPosition(request.getMaxPosition());
        robot.setStopLossPrice(request.getStopLossPrice());
        robot.setTakeProfitPrice(request.getTakeProfitPrice());
        robot.setOrderAmount(request.getOrderAmount());
        robot.setOrderQuantity(request.getOrderQuantity());
        robot.setLeverage(request.getLeverage() != null ? request.getLeverage() : 10);
        robot.setMarginMode(request.getMarginMode() != null ? request.getMarginMode() : "CROSS");
        robot.setPositionSide(request.getPositionSide() != null ? request.getPositionSide() : "LONG");
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

        return coinFuturesTradingRobotRepository.save(robot);
    }

    @Override
    @Transactional
    public void startRobot(Long userId, Long robotId) {
        CoinFuturesTradingRobot robot = coinFuturesTradingRobotRepository.findById(robotId)
                .orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }

        robot.setStatus("RUNNING");
        robot.setLastExecutionTime(LocalDateTime.now());
        coinFuturesTradingRobotRepository.save(robot);
    }

    @Override
    @Transactional
    public void stopRobot(Long userId, Long robotId) {
        CoinFuturesTradingRobot robot = coinFuturesTradingRobotRepository.findById(robotId)
                .orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }

        robot.setStatus("STOPPED");
        coinFuturesTradingRobotRepository.save(robot);
    }

    @Override
    public List<CoinFuturesTradingRobot> getUserRobots(Long userId) {
        return coinFuturesTradingRobotRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public CoinFuturesTradingRobot getRobotStatus(Long userId, Long robotId) {
        CoinFuturesTradingRobot robot = coinFuturesTradingRobotRepository.findById(robotId)
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
        List<CoinFuturesTradingRobot> runningRobots = coinFuturesTradingRobotRepository.findByStatus("RUNNING");

        for (CoinFuturesTradingRobot robot : runningRobots) {
            try {
                // 检查风险控制
                if (!checkRiskControl(robot)) {
                    robot.setStatus("STOPPED");
                    coinFuturesTradingRobotRepository.save(robot);
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
                coinFuturesTradingRobotRepository.save(robot);
            } catch (Exception e) {
                // 记录错误，继续处理下一个机器人
                System.err.println("执行机器人策略失败 (ID: " + robot.getId() + "): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void executeGridStrategy(CoinFuturesTradingRobot robot) {
        try {
            gridStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行网格交易策略失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void executeTrendFollowingStrategy(CoinFuturesTradingRobot robot) {
        try {
            trendFollowingStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行趋势跟踪策略失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void executeReverseStrategy(CoinFuturesTradingRobot robot) {
        try {
            reverseStrategy.execute(robot);
        } catch (Exception e) {
            System.err.println("执行反向策略失败: " + e.getMessage());
        }
    }

    /**
     * 检查风险控制
     */
    private boolean checkRiskControl(CoinFuturesTradingRobot robot) {
        // 检查最大亏损
        if (robot.getMaxLoss() != null && robot.getTotalLoss().compareTo(robot.getMaxLoss()) >= 0) {
            System.out.println("机器人达到最大亏损限制，自动停止: " + robot.getId());
            return false;
        }

        // TODO: 可以添加其他风险控制检查
        // 例如：检查最大持仓、检查止损价格等

        return true;
    }
}















