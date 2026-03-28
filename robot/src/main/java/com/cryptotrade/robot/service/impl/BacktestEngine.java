/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotBacktest;
import com.cryptotrade.robot.entity.RobotTradeRecord;
import com.cryptotrade.robot.repository.RobotTradeRecordRepository;
import com.cryptotrade.robot.service.RobotMarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回测引擎
 * 使用历史K线数据模拟网格交易策略
 */
@Service
public class BacktestEngine {

    @Autowired(required = false)
    private RobotMarketDataService robotMarketDataService;

    @Autowired(required = false)
    private RobotTradeRecordRepository tradeRecordRepository;

    /**
     * 执行回测
     */
    public BacktestResult executeBacktest(GridRobotBacktest backtest) {
        BacktestResult result = new BacktestResult();
        result.setBacktestId(backtest.getBacktestId());
        result.setInitialCapital(backtest.getInitialCapital());
        result.setFinalCapital(backtest.getInitialCapital());
        result.setTotalProfit(BigDecimal.ZERO);
        result.setTotalLoss(BigDecimal.ZERO);
        result.setTotalTrades(0);
        result.setWinningTrades(0);
        result.setLosingTrades(0);
        result.setMaxDrawdown(BigDecimal.ZERO);
        result.setSharpeRatio(BigDecimal.ZERO);

        if (robotMarketDataService == null) {
            return result; // 市场数据服务未配置，返回空结果
        }

        try {
            // 获取历史K线数据
            List<Map<String, Object>> klines = robotMarketDataService.getKlineData(
                    backtest.getPairName(),
                    backtest.getMarketType(),
                    "1h", // 默认1小时K线
                    1000 // 获取最多1000根K线
            );

            if (klines == null || klines.isEmpty()) {
                return result;
            }

            // 解析回测参数
            Map<String, Object> params = parseBacktestParams(backtest.getBacktestParams());
            Integer gridCount = backtest.getGridCount() != null ? backtest.getGridCount() : 10;
            BigDecimal upperPrice = getBigDecimal(params, "upperPrice");
            BigDecimal lowerPrice = getBigDecimal(params, "lowerPrice");
            BigDecimal startPrice = getBigDecimal(params, "startPrice");

            // 如果没有提供价格区间，使用K线数据的最高最低价
            if (upperPrice == null || lowerPrice == null) {
                upperPrice = getMaxPrice(klines);
                lowerPrice = getMinPrice(klines);
            }

            if (startPrice == null) {
                startPrice = getBigDecimal(klines.get(0), "close");
            }

            // 计算网格价格
            List<BigDecimal> gridPrices = calculateGridPrices(lowerPrice, upperPrice, gridCount);

            // 模拟交易
            BigDecimal currentCapital = backtest.getInitialCapital();
            BigDecimal currentPosition = BigDecimal.ZERO; // 当前持仓数量
            BigDecimal averageBuyPrice = BigDecimal.ZERO;
            List<BigDecimal> equityCurve = new ArrayList<>(); // 权益曲线
            BigDecimal maxEquity = currentCapital;
            BigDecimal maxDrawdown = BigDecimal.ZERO;

            for (Map<String, Object> kline : klines) {
                BigDecimal closePrice = getBigDecimal(kline, "close");
                LocalDateTime klineTime = getLocalDateTime(kline, "time");

                // 如果价格超出范围，跳过
                if (closePrice.compareTo(lowerPrice) < 0 || closePrice.compareTo(upperPrice) > 0) {
                    continue;
                }

                // 检查网格价格，决定是否交易
                for (int i = 0; i < gridPrices.size() - 1; i++) {
                    BigDecimal buyPrice = gridPrices.get(i);
                    BigDecimal sellPrice = gridPrices.get(i + 1);

                    // 如果价格跌到买入网格，且当前没有持仓，则买入
                    if (closePrice.compareTo(buyPrice) <= 0 && currentPosition.compareTo(BigDecimal.ZERO) == 0) {
                        BigDecimal buyAmount = currentCapital.multiply(new BigDecimal("0.5")); // 使用50%资金买入
                        BigDecimal buyQuantity = buyAmount.divide(closePrice, 8, RoundingMode.DOWN);
                        
                        if (buyAmount.compareTo(currentCapital) <= 0) {
                            currentCapital = currentCapital.subtract(buyAmount);
                            currentPosition = buyQuantity;
                            averageBuyPrice = closePrice;
                            result.setTotalTrades(result.getTotalTrades() + 1);

                            // 记录交易
                            recordTrade(result, backtest, "BUY", closePrice, buyQuantity, buyAmount, klineTime);
                        }
                    }
                    // 如果价格涨到卖出网格，且当前有持仓，则卖出
                    else if (closePrice.compareTo(sellPrice) >= 0 && currentPosition.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal sellAmount = currentPosition.multiply(closePrice);
                        BigDecimal profit = sellAmount.subtract(currentPosition.multiply(averageBuyPrice));
                        
                        currentCapital = currentCapital.add(sellAmount);
                        
                        if (profit.compareTo(BigDecimal.ZERO) > 0) {
                            result.setTotalProfit(result.getTotalProfit().add(profit));
                            result.setWinningTrades(result.getWinningTrades() + 1);
                        } else {
                            result.setTotalLoss(result.getTotalLoss().add(profit.abs()));
                            result.setLosingTrades(result.getLosingTrades() + 1);
                        }

                        // 记录交易
                        recordTrade(result, backtest, "SELL", closePrice, currentPosition, sellAmount, klineTime);

                        currentPosition = BigDecimal.ZERO;
                        result.setTotalTrades(result.getTotalTrades() + 1);
                    }
                }

                // 计算当前权益（现金 + 持仓市值）
                BigDecimal currentEquity = currentCapital.add(currentPosition.multiply(closePrice));
                equityCurve.add(currentEquity);

                // 更新最大权益和最大回撤
                if (currentEquity.compareTo(maxEquity) > 0) {
                    maxEquity = currentEquity;
                }
                BigDecimal drawdown = maxEquity.subtract(currentEquity).divide(maxEquity, 4, RoundingMode.HALF_UP);
                if (drawdown.compareTo(maxDrawdown) > 0) {
                    maxDrawdown = drawdown;
                }
            }

            // 如果回测结束时仍有持仓，按最后价格卖出
            if (currentPosition.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal lastPrice = getBigDecimal(klines.get(klines.size() - 1), "close");
                BigDecimal sellAmount = currentPosition.multiply(lastPrice);
                BigDecimal profit = sellAmount.subtract(currentPosition.multiply(averageBuyPrice));
                
                currentCapital = currentCapital.add(sellAmount);
                
                if (profit.compareTo(BigDecimal.ZERO) > 0) {
                    result.setTotalProfit(result.getTotalProfit().add(profit));
                    result.setWinningTrades(result.getWinningTrades() + 1);
                } else {
                    result.setTotalLoss(result.getTotalLoss().add(profit.abs()));
                    result.setLosingTrades(result.getLosingTrades() + 1);
                }

                currentPosition = BigDecimal.ZERO;
            }

            // 计算最终结果
            result.setFinalCapital(currentCapital);
            result.setNetProfit(currentCapital.subtract(backtest.getInitialCapital()));
            result.setProfitRate(result.getNetProfit().divide(backtest.getInitialCapital(), 4, RoundingMode.HALF_UP));
            result.setMaxDrawdown(maxDrawdown);
            result.setEquityCurve(equityCurve);

            // 计算Sharpe比率（简化版）
            if (equityCurve.size() > 1) {
                result.setSharpeRatio(calculateSharpeRatio(equityCurve));
            }

        } catch (Exception e) {
            // 记录日志
            // log.error("回测执行失败", e);
        }

        return result;
    }

    /**
     * 计算网格价格
     */
    private List<BigDecimal> calculateGridPrices(BigDecimal lowerPrice, BigDecimal upperPrice, Integer gridCount) {
        List<BigDecimal> gridPrices = new ArrayList<>();
        BigDecimal priceRange = upperPrice.subtract(lowerPrice);
        BigDecimal gridStep = priceRange.divide(new BigDecimal(gridCount), 8, RoundingMode.HALF_UP);

        for (int i = 0; i <= gridCount; i++) {
            BigDecimal price = lowerPrice.add(gridStep.multiply(new BigDecimal(i)));
            gridPrices.add(price);
        }

        return gridPrices;
    }

    /**
     * 记录交易
     */
    private void recordTrade(BacktestResult result, GridRobotBacktest backtest, String side,
                            BigDecimal price, BigDecimal quantity, BigDecimal amount, LocalDateTime time) {
        // 这里可以创建交易记录并保存到数据库
        // RobotTradeRecord record = new RobotTradeRecord();
        // ... 设置字段
        // tradeRecordRepository.save(record);
    }

    /**
     * 解析回测参数JSON
     */
    private Map<String, Object> parseBacktestParams(String paramsJson) {
        Map<String, Object> params = new HashMap<>();
        if (paramsJson == null || paramsJson.isEmpty()) {
            return params;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            params = objectMapper.readValue(paramsJson, Map.class);
        } catch (Exception e) {
            // 解析失败，返回空Map
        }
        return params;
    }

    /**
     * 从Map中获取BigDecimal值
     */
    private BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        return null;
    }

    /**
     * 从Map中获取LocalDateTime值
     */
    private LocalDateTime getLocalDateTime(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        return LocalDateTime.now();
    }

    /**
     * 获取K线数据中的最高价
     */
    private BigDecimal getMaxPrice(List<Map<String, Object>> klines) {
        BigDecimal max = BigDecimal.ZERO;
        for (Map<String, Object> kline : klines) {
            BigDecimal high = getBigDecimal(kline, "high");
            if (high != null && high.compareTo(max) > 0) {
                max = high;
            }
        }
        return max;
    }

    /**
     * 获取K线数据中的最低价
     */
    private BigDecimal getMinPrice(List<Map<String, Object>> klines) {
        BigDecimal min = new BigDecimal("999999999");
        for (Map<String, Object> kline : klines) {
            BigDecimal low = getBigDecimal(kline, "low");
            if (low != null && low.compareTo(min) < 0) {
                min = low;
            }
        }
        return min.compareTo(new BigDecimal("999999999")) == 0 ? BigDecimal.ZERO : min;
    }

    /**
     * 计算Sharpe比率（简化版）
     */
    private BigDecimal calculateSharpeRatio(List<BigDecimal> equityCurve) {
        if (equityCurve.size() < 2) {
            return BigDecimal.ZERO;
        }

        // 计算收益率序列
        List<BigDecimal> returns = new ArrayList<>();
        for (int i = 1; i < equityCurve.size(); i++) {
            BigDecimal prevEquity = equityCurve.get(i - 1);
            BigDecimal currentEquity = equityCurve.get(i);
            if (prevEquity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal returnRate = currentEquity.subtract(prevEquity).divide(prevEquity, 8, RoundingMode.HALF_UP);
                returns.add(returnRate);
            }
        }

        if (returns.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 计算平均收益率
        BigDecimal avgReturn = returns.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(returns.size()), 8, RoundingMode.HALF_UP);

        // 计算标准差
        BigDecimal variance = returns.stream()
                .map(r -> r.subtract(avgReturn).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(returns.size()), 8, RoundingMode.HALF_UP);

        BigDecimal stdDev = new BigDecimal(Math.sqrt(variance.doubleValue()));

        // Sharpe比率 = 平均收益率 / 标准差
        if (stdDev.compareTo(BigDecimal.ZERO) > 0) {
            return avgReturn.divide(stdDev, 4, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 回测结果类
     */
    public static class BacktestResult {
        private String backtestId;
        private BigDecimal initialCapital;
        private BigDecimal finalCapital;
        private BigDecimal totalProfit;
        private BigDecimal totalLoss;
        private BigDecimal netProfit;
        private BigDecimal profitRate;
        private Integer totalTrades;
        private Integer winningTrades;
        private Integer losingTrades;
        private BigDecimal maxDrawdown;
        private BigDecimal sharpeRatio;
        private List<BigDecimal> equityCurve;

        // Getters and Setters
        public String getBacktestId() { return backtestId; }
        public void setBacktestId(String backtestId) { this.backtestId = backtestId; }
        public BigDecimal getInitialCapital() { return initialCapital; }
        public void setInitialCapital(BigDecimal initialCapital) { this.initialCapital = initialCapital; }
        public BigDecimal getFinalCapital() { return finalCapital; }
        public void setFinalCapital(BigDecimal finalCapital) { this.finalCapital = finalCapital; }
        public BigDecimal getTotalProfit() { return totalProfit; }
        public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
        public BigDecimal getTotalLoss() { return totalLoss; }
        public void setTotalLoss(BigDecimal totalLoss) { this.totalLoss = totalLoss; }
        public BigDecimal getNetProfit() { return netProfit; }
        public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
        public BigDecimal getProfitRate() { return profitRate; }
        public void setProfitRate(BigDecimal profitRate) { this.profitRate = profitRate; }
        public Integer getTotalTrades() { return totalTrades; }
        public void setTotalTrades(Integer totalTrades) { this.totalTrades = totalTrades; }
        public Integer getWinningTrades() { return winningTrades; }
        public void setWinningTrades(Integer winningTrades) { this.winningTrades = winningTrades; }
        public Integer getLosingTrades() { return losingTrades; }
        public void setLosingTrades(Integer losingTrades) { this.losingTrades = losingTrades; }
        public BigDecimal getMaxDrawdown() { return maxDrawdown; }
        public void setMaxDrawdown(BigDecimal maxDrawdown) { this.maxDrawdown = maxDrawdown; }
        public BigDecimal getSharpeRatio() { return sharpeRatio; }
        public void setSharpeRatio(BigDecimal sharpeRatio) { this.sharpeRatio = sharpeRatio; }
        public List<BigDecimal> getEquityCurve() { return equityCurve; }
        public void setEquityCurve(List<BigDecimal> equityCurve) { this.equityCurve = equityCurve; }
    }
}

