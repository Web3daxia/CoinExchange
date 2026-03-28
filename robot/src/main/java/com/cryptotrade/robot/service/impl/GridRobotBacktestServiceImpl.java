/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotBacktest;
import com.cryptotrade.robot.entity.RobotTradeRecord;
import com.cryptotrade.robot.repository.GridRobotBacktestRepository;
import com.cryptotrade.robot.repository.RobotTradeRecordRepository;
import com.cryptotrade.robot.service.GridRobotBacktestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 网格机器人回测服务实现类
 */
@Service
public class GridRobotBacktestServiceImpl implements GridRobotBacktestService {

    @Autowired
    private GridRobotBacktestRepository backtestRepository;

    @Autowired(required = false)
    private RobotTradeRecordRepository tradeRecordRepository;

    @Autowired(required = false)
    private com.cryptotrade.robot.service.impl.BacktestEngine backtestEngine;

    @Override
    @Transactional
    public GridRobotBacktest createBacktest(Long userId, Long robotId, GridRobotBacktest backtest) {
        // 生成回测ID
        String backtestId = "BT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        backtest.setBacktestId(backtestId);
        backtest.setUserId(userId);
        backtest.setRobotId(robotId);
        backtest.setStatus("PENDING");
        backtest.setProgress(java.math.BigDecimal.ZERO);
        
        return backtestRepository.save(backtest);
    }

    @Override
    public GridRobotBacktest getBacktestStatus(String backtestId) {
        Optional<GridRobotBacktest> backtestOpt = backtestRepository.findByBacktestId(backtestId);
        return backtestOpt.orElseThrow(() -> new RuntimeException("回测任务不存在"));
    }

    @Override
    public GridRobotBacktest getBacktestResult(String backtestId) {
        GridRobotBacktest backtest = getBacktestStatus(backtestId);
        if (!"COMPLETED".equals(backtest.getStatus())) {
            throw new RuntimeException("回测任务尚未完成");
        }
        return backtest;
    }

    @Override
    public List<RobotTradeRecord> getBacktestTrades(String backtestId) {
        GridRobotBacktest backtest = getBacktestStatus(backtestId);
        // TODO: 实现回测交易记录查询
        // 应该查询与回测相关的交易记录
        return tradeRecordRepository.findByRobotId(backtest.getRobotId());
    }

    @Override
    public List<GridRobotBacktest> getUserBacktests(Long userId, Long robotId, String status) {
        List<GridRobotBacktest> backtests;
        if (robotId != null) {
            backtests = backtestRepository.findByRobotId(robotId);
        } else {
            backtests = backtestRepository.findByUserId(userId);
        }
        
        if (status != null) {
            backtests = backtests.stream()
                    .filter(b -> status.equals(b.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return backtests;
    }

    @Override
    @Transactional
    public void executeBacktest(String backtestId) {
        GridRobotBacktest backtest = getBacktestStatus(backtestId);
        
        if (!"PENDING".equals(backtest.getStatus()) && !"RUNNING".equals(backtest.getStatus())) {
            throw new RuntimeException("回测任务状态不允许执行");
        }
        
        backtest.setStatus("RUNNING");
        backtest.setStartedAt(LocalDateTime.now());
        backtestRepository.save(backtest);
        
        // 执行回测逻辑
        if (backtestEngine != null) {
            try {
                com.cryptotrade.robot.service.impl.BacktestEngine.BacktestResult result = backtestEngine.executeBacktest(backtest);
                
                // 更新回测结果
                backtest.setFinalCapital(result.getFinalCapital());
                backtest.setTotalProfit(result.getTotalProfit());
                backtest.setTotalLoss(result.getTotalLoss());
                backtest.setNetProfit(result.getNetProfit());
                backtest.setProfitRate(result.getProfitRate());
                backtest.setTotalTrades(result.getTotalTrades());
                backtest.setWinningTrades(result.getWinningTrades());
                backtest.setLosingTrades(result.getLosingTrades());
                backtest.setMaxDrawdown(result.getMaxDrawdown());
                backtest.setSharpeRatio(result.getSharpeRatio());
                
                // 保存权益曲线（JSON格式）
                try {
                    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    backtest.setEquityCurveData(objectMapper.writeValueAsString(result.getEquityCurve()));
                } catch (Exception e) {
                    // 忽略JSON序列化错误
                }
                
                backtest.setStatus("COMPLETED");
                backtest.setProgress(java.math.BigDecimal.valueOf(100));
                backtest.setCompletedAt(LocalDateTime.now());
            } catch (Exception e) {
                backtest.setStatus("FAILED");
                backtest.setErrorMessage(e.getMessage());
            }
        } else {
            // 回测引擎未配置，标记为失败
            backtest.setStatus("FAILED");
            backtest.setErrorMessage("回测引擎未配置");
        }
        
        backtestRepository.save(backtest);
    }
}

