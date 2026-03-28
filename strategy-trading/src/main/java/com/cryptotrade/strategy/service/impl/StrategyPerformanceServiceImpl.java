/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.service.impl;

import com.cryptotrade.strategy.entity.StrategyTradeRecord;
import com.cryptotrade.strategy.entity.TradingStrategy;
import com.cryptotrade.strategy.repository.StrategyTradeRecordRepository;
import com.cryptotrade.strategy.repository.TradingStrategyRepository;
import com.cryptotrade.strategy.service.StrategyPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 策略表现服务实现类
 */
@Service
public class StrategyPerformanceServiceImpl implements StrategyPerformanceService {

    @Autowired
    private TradingStrategyRepository strategyRepository;

    @Autowired
    private StrategyTradeRecordRepository tradeRecordRepository;

    @Override
    public Map<String, Object> getPerformance(Long strategyId, String period) {
        Optional<TradingStrategy> strategyOpt = strategyRepository.findById(strategyId);
        TradingStrategy strategy = strategyOpt.orElseThrow(() -> new RuntimeException("策略不存在"));
        
        // 计算时间范围
        LocalDateTime startTime = calculateStartTime(period);
        List<StrategyTradeRecord> records = tradeRecordRepository.findByCreatedAtBetween(
                startTime, LocalDateTime.now());
        records = records.stream()
                .filter(r -> r.getStrategyId().equals(strategyId))
                .collect(Collectors.toList());
        
        Map<String, Object> performance = new HashMap<>();
        performance.put("strategyId", strategyId);
        performance.put("period", period);
        performance.put("initialCapital", strategy.getInitialCapital());
        performance.put("currentCapital", strategy.getCurrentCapital());
        performance.put("totalProfit", strategy.getTotalProfit());
        performance.put("totalLoss", strategy.getTotalLoss());
        
        BigDecimal netProfit = strategy.getTotalProfit().subtract(strategy.getTotalLoss());
        performance.put("netProfit", netProfit);
        
        if (strategy.getInitialCapital().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitRate = netProfit.divide(strategy.getInitialCapital(), 4, RoundingMode.HALF_UP);
            performance.put("profitRate", profitRate);
        }
        
        performance.put("totalTrades", records.size());
        
        long winningTrades = records.stream()
                .filter(r -> r.getProfitLoss() != null && r.getProfitLoss().compareTo(BigDecimal.ZERO) > 0)
                .count();
        performance.put("winningTrades", winningTrades);
        performance.put("losingTrades", records.size() - winningTrades);
        
        if (records.size() > 0) {
            BigDecimal winRate = new BigDecimal(winningTrades)
                    .divide(new BigDecimal(records.size()), 4, RoundingMode.HALF_UP);
            performance.put("winRate", winRate);
        }
        
        return performance;
    }

    @Override
    public Map<String, Object> getProfitTrend(Long strategyId, String period, String interval) {
        // TODO: 实现收益趋势查询
        // 需要根据period和interval计算收益趋势数据
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> optimizeStrategy(Long strategyId, String backtestId, String optimizeTarget) {
        // TODO: 实现策略优化建议
        // 需要根据回测结果分析并给出优化建议
        return new HashMap<>();
    }

    private LocalDateTime calculateStartTime(String period) {
        LocalDateTime now = LocalDateTime.now();
        switch (period != null ? period.toUpperCase() : "ALL") {
            case "TODAY":
                return now.withHour(0).withMinute(0).withSecond(0);
            case "WEEK":
                return now.minusDays(7);
            case "MONTH":
                return now.minusMonths(1);
            case "YEAR":
                return now.minusYears(1);
            default:
                return LocalDateTime.of(2000, 1, 1, 0, 0);
        }
    }
}













