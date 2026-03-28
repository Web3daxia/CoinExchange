/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;
import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.repository.GridRobotAdjustmentHistoryRepository;
import com.cryptotrade.robot.repository.TradingRobotRepository;
import com.cryptotrade.robot.service.GridRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 网格机器人服务实现类
 */
@Service
public class GridRobotServiceImpl implements GridRobotService {

    @Autowired
    private TradingRobotRepository robotRepository;

    @Autowired
    private GridRobotAdjustmentHistoryRepository adjustmentHistoryRepository;

    @Autowired
    private com.cryptotrade.robot.service.GridRobotSettlementService gridRobotSettlementService;

    @Autowired
    private com.cryptotrade.robot.service.GridRobotBacktestService gridRobotBacktestService;

    @Override
    @Transactional
    public TradingRobot createSpotGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                            BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                            BigDecimal initialCapital, BigDecimal investmentRatio,
                                            BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                            BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage) {
        TradingRobot robot = new TradingRobot();
        robot.setUserId(userId);
        robot.setRobotName(robotName != null ? robotName : generateRobotName(pairName, "SPOT"));
        robot.setPairName(pairName);
        robot.setMarketType("SPOT");
        robot.setStrategyType("GRID");
        robot.setStatus("STOPPED");
        robot.setGridCount(gridCount);
        robot.setUpperPrice(upperPrice);
        robot.setLowerPrice(lowerPrice);
        robot.setStartPrice(startPrice);
        robot.setInitialCapital(initialCapital);
        robot.setCurrentCapital(initialCapital);
        robot.setAvailableCapital(initialCapital);
        robot.setInvestmentRatio(investmentRatio != null ? investmentRatio : new BigDecimal("0.5"));
        robot.setStopLossPrice(stopLossPrice);
        robot.setTakeProfitPrice(takeProfitPrice);
        robot.setStopLossPercentage(stopLossPercentage);
        robot.setTakeProfitPercentage(takeProfitPercentage);
        robot.setTotalProfit(BigDecimal.ZERO);
        robot.setTotalLoss(BigDecimal.ZERO);
        robot.setTotalTrades(0);
        robot.setStartTime(LocalDateTime.now());

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot createFuturesUsdtGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                                    BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                                    BigDecimal initialCapital, BigDecimal investmentRatio,
                                                    Integer leverage, String marginMode,
                                                    BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                                    BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage) {
        TradingRobot robot = new TradingRobot();
        robot.setUserId(userId);
        robot.setRobotName(robotName != null ? robotName : generateRobotName(pairName, "FUTURES_USDT"));
        robot.setPairName(pairName);
        robot.setMarketType("FUTURES_USDT");
        robot.setStrategyType("GRID");
        robot.setStatus("STOPPED");
        robot.setGridCount(gridCount);
        robot.setUpperPrice(upperPrice);
        robot.setLowerPrice(lowerPrice);
        robot.setStartPrice(startPrice);
        robot.setInitialCapital(initialCapital);
        robot.setCurrentCapital(initialCapital);
        robot.setAvailableCapital(initialCapital);
        robot.setInvestmentRatio(investmentRatio != null ? investmentRatio : new BigDecimal("0.5"));
        robot.setLeverage(leverage);
        robot.setMarginMode(marginMode);
        robot.setStopLossPrice(stopLossPrice);
        robot.setTakeProfitPrice(takeProfitPrice);
        robot.setStopLossPercentage(stopLossPercentage);
        robot.setTakeProfitPercentage(takeProfitPercentage);
        robot.setTotalProfit(BigDecimal.ZERO);
        robot.setTotalLoss(BigDecimal.ZERO);
        robot.setTotalTrades(0);
        robot.setStartTime(LocalDateTime.now());

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot createFuturesCoinGridRobot(Long userId, String robotName, String pairName, Integer gridCount,
                                                    BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                                    BigDecimal initialCapital, BigDecimal investmentRatio,
                                                    Integer leverage, String marginMode,
                                                    BigDecimal stopLossPrice, BigDecimal takeProfitPrice,
                                                    BigDecimal stopLossPercentage, BigDecimal takeProfitPercentage) {
        TradingRobot robot = new TradingRobot();
        robot.setUserId(userId);
        robot.setRobotName(robotName != null ? robotName : generateRobotName(pairName, "FUTURES_COIN"));
        robot.setPairName(pairName);
        robot.setMarketType("FUTURES_COIN");
        robot.setStrategyType("GRID");
        robot.setStatus("STOPPED");
        robot.setGridCount(gridCount);
        robot.setUpperPrice(upperPrice);
        robot.setLowerPrice(lowerPrice);
        robot.setStartPrice(startPrice);
        robot.setInitialCapital(initialCapital);
        robot.setCurrentCapital(initialCapital);
        robot.setAvailableCapital(initialCapital);
        robot.setInvestmentRatio(investmentRatio != null ? investmentRatio : new BigDecimal("0.5"));
        robot.setLeverage(leverage);
        robot.setMarginMode(marginMode);
        robot.setStopLossPrice(stopLossPrice);
        robot.setTakeProfitPrice(takeProfitPrice);
        robot.setStopLossPercentage(stopLossPercentage);
        robot.setTakeProfitPercentage(takeProfitPercentage);
        robot.setTotalProfit(BigDecimal.ZERO);
        robot.setTotalLoss(BigDecimal.ZERO);
        robot.setTotalTrades(0);
        robot.setStartTime(LocalDateTime.now());

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot startRobot(Long userId, Long robotId) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if (!"STOPPED".equals(robot.getStatus()) && !"PAUSED".equals(robot.getStatus())) {
            throw new RuntimeException("机器人状态不允许启动");
        }

        robot.setStatus("RUNNING");
        robot.setStartTime(LocalDateTime.now());
        if (robot.getPausedAt() != null) {
            robot.setResumedAt(LocalDateTime.now());
        }

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot pauseRobot(Long userId, Long robotId) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if (!"RUNNING".equals(robot.getStatus())) {
            throw new RuntimeException("机器人状态不允许暂停");
        }

        robot.setStatus("PAUSED");
        robot.setPausedAt(LocalDateTime.now());

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot resumeRobot(Long userId, Long robotId) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if (!"PAUSED".equals(robot.getStatus())) {
            throw new RuntimeException("机器人状态不允许恢复");
        }

        robot.setStatus("RUNNING");
        robot.setResumedAt(LocalDateTime.now());

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot stopRobot(Long userId, Long robotId, Boolean closePositions, Boolean cancelOrders) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if ("STOPPED".equals(robot.getStatus())) {
            throw new RuntimeException("机器人已经停止");
        }

        robot.setStatus("STOPPED");
        robot.setStoppedAt(LocalDateTime.now());

        // TODO: 如果需要，取消所有未成交订单
        if (cancelOrders != null && cancelOrders) {
            // 调用订单服务取消订单
        }

        // TODO: 如果需要，平掉所有持仓（合约市场）
        if (closePositions != null && closePositions) {
            // 调用持仓服务平仓
        }

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public TradingRobot updateRobotSettings(Long userId, Long robotId, String robotName, Integer gridCount,
                                            BigDecimal upperPrice, BigDecimal lowerPrice, BigDecimal startPrice,
                                            BigDecimal initialCapital, BigDecimal investmentRatio,
                                            BigDecimal stopLossPrice, BigDecimal takeProfitPrice) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if ("RUNNING".equals(robot.getStatus())) {
            throw new RuntimeException("运行中的机器人无法修改参数，请先停止或暂停");
        }

        if (robotName != null) {
            robot.setRobotName(robotName);
        }
        if (gridCount != null) {
            robot.setGridCount(gridCount);
        }
        if (upperPrice != null) {
            robot.setUpperPrice(upperPrice);
        }
        if (lowerPrice != null) {
            robot.setLowerPrice(lowerPrice);
        }
        if (startPrice != null) {
            robot.setStartPrice(startPrice);
        }
        if (initialCapital != null) {
            robot.setInitialCapital(initialCapital);
            robot.setCurrentCapital(initialCapital);
            robot.setAvailableCapital(initialCapital);
        }
        if (investmentRatio != null) {
            robot.setInvestmentRatio(investmentRatio);
        }
        if (stopLossPrice != null) {
            robot.setStopLossPrice(stopLossPrice);
        }
        if (takeProfitPrice != null) {
            robot.setTakeProfitPrice(takeProfitPrice);
        }

        return robotRepository.save(robot);
    }

    @Override
    @Transactional
    public GridRobotAdjustmentHistory autoAdjustRobot(Long userId, Long robotId, String adjustType,
                                                       Integer volatilityPeriod, Integer targetGridCount,
                                                       Boolean adjustPriceRange) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if ("RUNNING".equals(robot.getStatus())) {
            throw new RuntimeException("运行中的机器人无法调整参数，请先停止或暂停");
        }

        GridRobotAdjustmentHistory adjustment = new GridRobotAdjustmentHistory();
        adjustment.setRobotId(robotId);
        adjustment.setAdjustType(adjustType);
        adjustment.setOldGridCount(robot.getGridCount());
        adjustment.setOldUpperPrice(robot.getUpperPrice());
        adjustment.setOldLowerPrice(robot.getLowerPrice());
        adjustment.setOldStartPrice(robot.getStartPrice());

        // TODO: 根据市场波动率智能调整参数
        // 这里简化处理，实际应该根据市场数据计算
        if ("AUTO".equals(adjustType)) {
            // 自动调整逻辑
            if (targetGridCount != null) {
                robot.setGridCount(targetGridCount);
            }
            // 可以根据波动率调整价格区间
        } else if ("MANUAL".equals(adjustType) && targetGridCount != null) {
            robot.setGridCount(targetGridCount);
        }

        adjustment.setNewGridCount(robot.getGridCount());
        adjustment.setNewUpperPrice(robot.getUpperPrice());
        adjustment.setNewLowerPrice(robot.getLowerPrice());
        adjustment.setNewStartPrice(robot.getStartPrice());
        adjustment.setAdjustmentReason("智能调整");

        robotRepository.save(robot);
        return adjustmentHistoryRepository.save(adjustment);
    }

    @Override
    @Transactional
    public com.cryptotrade.robot.entity.GridRobotSettlement settleRobot(Long userId, Long robotId) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if (!"STOPPED".equals(robot.getStatus())) {
            throw new RuntimeException("只能结算已停止的机器人");
        }

        return gridRobotSettlementService.settleRobot(userId, robotId);
    }

    @Override
    @Transactional
    public void deleteRobot(Long userId, Long robotId) {
        TradingRobot robot = getRobotAndCheckPermission(userId, robotId);
        
        if (!"STOPPED".equals(robot.getStatus())) {
            throw new RuntimeException("只能删除已停止的机器人");
        }

        robotRepository.delete(robot);
    }

    @Override
    public com.cryptotrade.robot.entity.GridRobotBacktest createBacktest(Long userId, Long robotId, String pairName,
                                                                          String marketType, Integer gridCount,
                                                                          BigDecimal upperPrice, BigDecimal lowerPrice,
                                                                          BigDecimal startPrice, BigDecimal initialCapital,
                                                                          BigDecimal investmentRatio, String startTime,
                                                                          String endTime, BigDecimal stopLossPrice,
                                                                          BigDecimal takeProfitPrice, Integer leverage,
                                                                          String marginMode) {
        com.cryptotrade.robot.entity.GridRobotBacktest backtest = new com.cryptotrade.robot.entity.GridRobotBacktest();
        backtest.setUserId(userId);
        backtest.setRobotId(robotId);
        backtest.setPairName(pairName);
        backtest.setMarketType(marketType);
        backtest.setStrategyType("GRID");
        backtest.setGridCount(gridCount);
        backtest.setInitialCapital(initialCapital);
        
        // 解析时间
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            backtest.setStartTime(java.time.LocalDateTime.parse(startTime, formatter));
            backtest.setEndTime(java.time.LocalDateTime.parse(endTime, formatter));
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误，应为: yyyy-MM-dd HH:mm:ss");
        }
        
        // 设置参数（JSON格式）
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("upperPrice", upperPrice);
        params.put("lowerPrice", lowerPrice);
        params.put("startPrice", startPrice);
        params.put("investmentRatio", investmentRatio);
        params.put("stopLossPrice", stopLossPrice);
        params.put("takeProfitPrice", takeProfitPrice);
        params.put("leverage", leverage);
        params.put("marginMode", marginMode);
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            backtest.setBacktestParams(objectMapper.writeValueAsString(params));
        } catch (Exception e) {
            backtest.setBacktestParams("{}");
        }
        
        return gridRobotBacktestService.createBacktest(userId, robotId, backtest);
    }

    @Override
    public com.cryptotrade.robot.entity.GridRobotBacktest getBacktestStatus(String backtestId) {
        return gridRobotBacktestService.getBacktestStatus(backtestId);
    }

    @Override
    public com.cryptotrade.robot.entity.GridRobotBacktest getBacktestResult(String backtestId) {
        return gridRobotBacktestService.getBacktestResult(backtestId);
    }

    private TradingRobot getRobotAndCheckPermission(Long userId, Long robotId) {
        Optional<TradingRobot> robotOpt = robotRepository.findById(robotId);
        TradingRobot robot = robotOpt.orElseThrow(() -> new RuntimeException("机器人不存在"));
        
        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }
        
        return robot;
    }

    private String generateRobotName(String pairName, String marketType) {
        return pairName + marketType + "网格机器人" + System.currentTimeMillis();
    }
}

