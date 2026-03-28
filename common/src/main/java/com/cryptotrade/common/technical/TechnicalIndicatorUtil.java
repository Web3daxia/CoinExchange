/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.common.technical;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 技术指标计算工具类
 * 提供常用的技术指标计算功能
 */
public class TechnicalIndicatorUtil {

    /**
     * 计算简单移动平均线（SMA）
     */
    public static BigDecimal calculateSMA(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period) {
            return null;
        }

        BigDecimal sum = BigDecimal.ZERO;
        int startIndex = prices.size() - period;
        
        for (int i = startIndex; i < prices.size(); i++) {
            sum = sum.add(prices.get(i));
        }

        return sum.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);
    }

    /**
     * 计算指数移动平均线（EMA）
     */
    public static BigDecimal calculateEMA(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period) {
            return null;
        }

        BigDecimal multiplier = new BigDecimal(2).divide(new BigDecimal(period + 1), 8, RoundingMode.HALF_UP);
        BigDecimal ema = calculateSMA(prices.subList(0, period), period);

        for (int i = period; i < prices.size(); i++) {
            ema = prices.get(i).multiply(multiplier).add(ema.multiply(BigDecimal.ONE.subtract(multiplier)));
        }

        return ema;
    }

    /**
     * 计算RSI（相对强弱指标）
     */
    public static BigDecimal calculateRSI(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period + 1) {
            return null;
        }

        List<BigDecimal> gains = new ArrayList<>();
        List<BigDecimal> losses = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            BigDecimal change = prices.get(i).subtract(prices.get(i - 1));
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                gains.add(change);
                losses.add(BigDecimal.ZERO);
            } else {
                gains.add(BigDecimal.ZERO);
                losses.add(change.abs());
            }
        }

        if (gains.size() < period) {
            return null;
        }

        BigDecimal avgGain = calculateSMA(gains, period);
        BigDecimal avgLoss = calculateSMA(losses, period);

        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return new BigDecimal(100);
        }

        BigDecimal rs = avgGain.divide(avgLoss, 8, RoundingMode.HALF_UP);
        BigDecimal rsi = new BigDecimal(100).subtract(new BigDecimal(100).divide(BigDecimal.ONE.add(rs), 8, RoundingMode.HALF_UP));

        return rsi;
    }

    /**
     * MACD计算结果
     */
    public static class MACDResult {
        private BigDecimal dif;
        private BigDecimal dea;
        private BigDecimal macd;

        public MACDResult(BigDecimal dif, BigDecimal dea, BigDecimal macd) {
            this.dif = dif;
            this.dea = dea;
            this.macd = macd;
        }

        public BigDecimal getDif() {
            return dif;
        }

        public BigDecimal getDea() {
            return dea;
        }

        public BigDecimal getMacd() {
            return macd;
        }
    }

    /**
     * 计算MACD指标
     */
    public static MACDResult calculateMACD(List<BigDecimal> prices, int fastPeriod, int slowPeriod, int signalPeriod) {
        if (prices == null || prices.size() < slowPeriod) {
            return null;
        }

        List<BigDecimal> emaFast = new ArrayList<>();
        List<BigDecimal> emaSlow = new ArrayList<>();

        // 计算快线和慢线EMA
        for (int i = fastPeriod; i <= prices.size(); i++) {
            BigDecimal ema = calculateEMA(prices.subList(0, i), fastPeriod);
            if (ema != null) {
                emaFast.add(ema);
            }
        }

        for (int i = slowPeriod; i <= prices.size(); i++) {
            BigDecimal ema = calculateEMA(prices.subList(0, i), slowPeriod);
            if (ema != null) {
                emaSlow.add(ema);
            }
        }

        if (emaFast.size() < slowPeriod - fastPeriod + signalPeriod || emaSlow.size() < signalPeriod) {
            return null;
        }

        // 计算DIF（快线 - 慢线）
        int startIdx = emaSlow.size() - emaFast.size();
        List<BigDecimal> difs = new ArrayList<>();
        for (int i = 0; i < emaFast.size(); i++) {
            BigDecimal dif = emaFast.get(i).subtract(emaSlow.get(startIdx + i));
            difs.add(dif);
        }

        // 计算DEA（信号线，DIF的EMA）
        BigDecimal dea = calculateEMA(difs, signalPeriod);
        if (dea == null || difs.isEmpty()) {
            return null;
        }

        BigDecimal lastDif = difs.get(difs.size() - 1);
        BigDecimal macd = lastDif.subtract(dea).multiply(new BigDecimal(2));

        return new MACDResult(lastDif, dea, macd);
    }

    /**
     * 布林带计算结果
     */
    public static class BollingerBandsResult {
        private BigDecimal upperBand;
        private BigDecimal middleBand;
        private BigDecimal lowerBand;

        public BollingerBandsResult(BigDecimal upperBand, BigDecimal middleBand, BigDecimal lowerBand) {
            this.upperBand = upperBand;
            this.middleBand = middleBand;
            this.lowerBand = lowerBand;
        }

        public BigDecimal getUpperBand() {
            return upperBand;
        }

        public BigDecimal getMiddleBand() {
            return middleBand;
        }

        public BigDecimal getLowerBand() {
            return lowerBand;
        }
    }

    /**
     * 计算布林带
     */
    public static BollingerBandsResult calculateBollingerBands(List<BigDecimal> prices, int period, BigDecimal stdDevMultiplier) {
        if (prices == null || prices.size() < period) {
            return null;
        }

        BigDecimal middleBand = calculateSMA(prices, period);
        if (middleBand == null) {
            return null;
        }

        // 计算标准差
        BigDecimal variance = BigDecimal.ZERO;
        int startIndex = prices.size() - period;
        for (int i = startIndex; i < prices.size(); i++) {
            BigDecimal diff = prices.get(i).subtract(middleBand);
            variance = variance.add(diff.multiply(diff));
        }
        variance = variance.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);

        BigDecimal stdDev = sqrt(variance);
        BigDecimal upperBand = middleBand.add(stdDev.multiply(stdDevMultiplier));
        BigDecimal lowerBand = middleBand.subtract(stdDev.multiply(stdDevMultiplier));

        return new BollingerBandsResult(upperBand, middleBand, lowerBand);
    }

    /**
     * 计算波动率
     */
    public static BigDecimal calculateVolatility(List<BigDecimal> prices, int period) {
        if (prices == null || prices.size() < period + 1) {
            return null;
        }

        List<BigDecimal> returns = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i - 1).compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal returnValue = prices.get(i).subtract(prices.get(i - 1))
                        .divide(prices.get(i - 1), 8, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)); // 转换为百分比
                returns.add(returnValue);
            }
        }

        if (returns.size() < period) {
            return null;
        }

        BigDecimal meanReturn = calculateSMA(returns, period);
        if (meanReturn == null) {
            return null;
        }

        BigDecimal variance = BigDecimal.ZERO;
        int startIndex = returns.size() - period;
        for (int i = startIndex; i < returns.size(); i++) {
            BigDecimal diff = returns.get(i).subtract(meanReturn);
            variance = variance.add(diff.multiply(diff));
        }
        variance = variance.divide(new BigDecimal(period), 8, RoundingMode.HALF_UP);

        BigDecimal volatility = sqrt(variance);
        return volatility;
    }

    /**
     * 计算平方根（使用牛顿法）
     */
    private static BigDecimal sqrt(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal guess = value;
        BigDecimal precision = new BigDecimal("0.00000001");

        while (true) {
            BigDecimal nextGuess = value.divide(guess, 8, RoundingMode.HALF_UP)
                    .add(guess)
                    .divide(new BigDecimal(2), 8, RoundingMode.HALF_UP);
            
            BigDecimal diff = nextGuess.subtract(guess).abs();
            if (diff.compareTo(precision) < 0) {
                return nextGuess;
            }
            guess = nextGuess;
        }
    }
}
