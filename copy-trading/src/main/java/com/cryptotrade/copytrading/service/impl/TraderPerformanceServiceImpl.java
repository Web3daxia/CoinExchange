/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service.impl;

import com.cryptotrade.copytrading.entity.CopyOrder;
import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.entity.TraderPerformance;
import com.cryptotrade.copytrading.repository.CopyOrderRepository;
import com.cryptotrade.copytrading.repository.TraderPerformanceRepository;
import com.cryptotrade.copytrading.repository.TraderRepository;
import com.cryptotrade.copytrading.service.TraderPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 带单员表现数据服务实现类
 */
@Service
public class TraderPerformanceServiceImpl implements TraderPerformanceService {

    @Autowired
    private TraderPerformanceRepository traderPerformanceRepository;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private CopyOrderRepository copyOrderRepository;

    @Override
    @Transactional
    public TraderPerformance calculatePerformance(Long traderId, String periodType,
                                                 LocalDateTime periodStart, LocalDateTime periodEnd) {
        // 查询周期内的订单
        List<CopyOrder> orders = copyOrderRepository.findByTraderIdAndCreatedAtBetween(
                traderId, periodStart, periodEnd);

        // 计算统计数据
        BigDecimal totalProfit = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;
        int winningTrades = 0;
        int losingTrades = 0;
        BigDecimal sumProfit = BigDecimal.ZERO;
        BigDecimal sumLoss = BigDecimal.ZERO;

        for (CopyOrder order : orders) {
            if (order.getProfitLoss() != null) {
                if (order.getProfitLoss().compareTo(BigDecimal.ZERO) > 0) {
                    totalProfit = totalProfit.add(order.getProfitLoss());
                    winningTrades++;
                    sumProfit = sumProfit.add(order.getProfitLoss());
                } else if (order.getProfitLoss().compareTo(BigDecimal.ZERO) < 0) {
                    totalLoss = totalLoss.add(order.getProfitLoss().abs());
                    losingTrades++;
                    sumLoss = sumLoss.add(order.getProfitLoss().abs());
                }
            }
        }

        int totalTrades = orders.size();
        BigDecimal netProfit = totalProfit.subtract(totalLoss);
        BigDecimal winRate = totalTrades > 0 ?
                new BigDecimal(winningTrades).divide(new BigDecimal(totalTrades), 4, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        BigDecimal avgProfit = winningTrades > 0 ?
                sumProfit.divide(new BigDecimal(winningTrades), 8, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        BigDecimal avgLoss = losingTrades > 0 ?
                sumLoss.divide(new BigDecimal(losingTrades), 8, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        BigDecimal profitLossRatio = avgLoss.compareTo(BigDecimal.ZERO) > 0 ?
                avgProfit.divide(avgLoss, 4, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // 获取带单员信息
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        // 计算收益率
        BigDecimal returnRate = trader.getTotalAum().compareTo(BigDecimal.ZERO) > 0 ?
                netProfit.divide(trader.getTotalAum(), 4, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // 计算日均交易频次
        long days = java.time.temporal.ChronoUnit.DAYS.between(periodStart, periodEnd);
        BigDecimal dailyAvgTrades = days > 0 ?
                new BigDecimal(totalTrades).divide(new BigDecimal(days), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // 创建或更新表现数据
        TraderPerformance performance = traderPerformanceRepository
                .findByTraderIdAndPeriodTypeAndPeriodStartAndPeriodEnd(traderId, periodType, periodStart, periodEnd)
                .orElse(new TraderPerformance());

        performance.setTraderId(traderId);
        performance.setPeriodType(periodType);
        performance.setPeriodStart(periodStart);
        performance.setPeriodEnd(periodEnd);
        performance.setTotalProfit(totalProfit);
        performance.setTotalLoss(totalLoss);
        performance.setNetProfit(netProfit);
        performance.setReturnRate(returnRate);
        performance.setWinRate(winRate);
        performance.setTotalTrades(totalTrades);
        performance.setWinningTrades(winningTrades);
        performance.setLosingTrades(losingTrades);
        performance.setAvgProfit(avgProfit);
        performance.setAvgLoss(avgLoss);
        performance.setProfitLossRatio(profitLossRatio);
        performance.setSharpeRatio(trader.getSharpeRatio());
        performance.setMaxDrawdown(trader.getMaxDrawdown());
        performance.setTotalAum(trader.getTotalAum());
        performance.setTotalFollowers(trader.getTotalFollowers());
        performance.setDailyAvgTrades(dailyAvgTrades);

        return traderPerformanceRepository.save(performance);
    }

    @Override
    public List<TraderPerformance> getTraderPerformance(Long traderId, String periodType) {
        if (periodType != null && !periodType.isEmpty()) {
            return traderPerformanceRepository.findByTraderIdAndPeriodTypeOrderByPeriodStartDesc(traderId, periodType);
        }
        return traderPerformanceRepository.findByTraderIdOrderByPeriodStartDesc(traderId);
    }

    @Override
    public Map<String, Object> getTraderPerformanceDetails(Long traderId) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        Map<String, Object> details = new HashMap<>();
        details.put("traderId", trader.getId());
        details.put("userId", trader.getUserId());
        details.put("level", trader.getLevel());
        details.put("totalProfit", trader.getTotalProfit());
        details.put("totalLoss", trader.getTotalLoss());
        details.put("netProfit", trader.getTotalProfit().subtract(trader.getTotalLoss()));
        details.put("winRate", trader.getWinRate());
        details.put("totalAum", trader.getTotalAum());
        details.put("totalFollowers", trader.getTotalFollowers());
        details.put("sharpeRatio", trader.getSharpeRatio());
        details.put("maxDrawdown", trader.getMaxDrawdown());
        details.put("lastLiquidationTime", trader.getLastLiquidationTime());

        // 计算收益率
        BigDecimal returnRate = trader.getTotalAum().compareTo(BigDecimal.ZERO) > 0 ?
                trader.getTotalProfit().subtract(trader.getTotalLoss())
                        .divide(trader.getTotalAum(), 4, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        details.put("returnRate", returnRate);

        // 获取交易统计
        List<CopyOrder> allOrders = copyOrderRepository.findByTraderIdOrderByCreatedAtDesc(traderId);
        details.put("totalTrades", allOrders.size());
        long winningTrades = allOrders.stream()
                .filter(o -> o.getProfitLoss() != null && o.getProfitLoss().compareTo(BigDecimal.ZERO) > 0)
                .count();
        details.put("winningTrades", winningTrades);
        details.put("losingTrades", allOrders.size() - winningTrades);

        return details;
    }

    @Override
    public List<Map<String, Object>> getTraderLeaderboard(String sortBy, Integer limit) {
        List<Trader> traders = traderRepository.findByStatus("APPROVED");

        List<Map<String, Object>> leaderboard = traders.stream()
                .map(trader -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("traderId", trader.getId());
                    item.put("userId", trader.getUserId());
                    item.put("level", trader.getLevel());
                    item.put("totalProfit", trader.getTotalProfit());
                    item.put("totalLoss", trader.getTotalLoss());
                    item.put("netProfit", trader.getTotalProfit().subtract(trader.getTotalLoss()));
                    item.put("winRate", trader.getWinRate());
                    item.put("totalAum", trader.getTotalAum());
                    item.put("totalFollowers", trader.getTotalFollowers());
                    item.put("returnRate", trader.getTotalAum().compareTo(BigDecimal.ZERO) > 0 ?
                            trader.getTotalProfit().subtract(trader.getTotalLoss())
                                    .divide(trader.getTotalAum(), 4, RoundingMode.HALF_UP) :
                            BigDecimal.ZERO);
                    return item;
                })
                .sorted((a, b) -> {
                    BigDecimal aValue = (BigDecimal) a.get(sortBy);
                    BigDecimal bValue = (BigDecimal) b.get(sortBy);
                    return bValue.compareTo(aValue);
                })
                .limit(limit != null ? limit : 100)
                .collect(Collectors.toList());

        return leaderboard;
    }

    @Override
    @Transactional
    public void updateTraderStatistics(Long traderId) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        // 获取所有订单
        List<CopyOrder> orders = copyOrderRepository.findByTraderIdOrderByCreatedAtDesc(traderId);

        // 计算统计数据
        BigDecimal totalProfit = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;
        int winningTrades = 0;
        int totalTrades = orders.size();

        for (CopyOrder order : orders) {
            if (order.getProfitLoss() != null) {
                if (order.getProfitLoss().compareTo(BigDecimal.ZERO) > 0) {
                    totalProfit = totalProfit.add(order.getProfitLoss());
                    winningTrades++;
                } else if (order.getProfitLoss().compareTo(BigDecimal.ZERO) < 0) {
                    totalLoss = totalLoss.add(order.getProfitLoss().abs());
                }
            }
        }

        BigDecimal winRate = totalTrades > 0 ?
                new BigDecimal(winningTrades).divide(new BigDecimal(totalTrades), 4, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        // 更新带单员统计
        trader.setTotalProfit(totalProfit);
        trader.setTotalLoss(totalLoss);
        trader.setWinRate(winRate);

        traderRepository.save(trader);
    }
}















