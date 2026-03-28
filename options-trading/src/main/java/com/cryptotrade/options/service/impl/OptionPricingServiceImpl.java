/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.service.OptionPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 期权定价服务实现类
 */
@Service
public class OptionPricingServiceImpl implements OptionPricingService {

    @Autowired
    private OptionContractRepository optionContractRepository;

    private static final MathContext MC = new MathContext(10);

    @Override
    public BigDecimal calculateBlackScholesPrice(
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            double volatility,
            String optionType) {

        if (timeToExpiry <= 0) {
            // 已到期，返回内在价值
            if ("CALL".equals(optionType)) {
                return underlyingPrice.subtract(strikePrice).max(BigDecimal.ZERO);
            } else {
                return strikePrice.subtract(underlyingPrice).max(BigDecimal.ZERO);
            }
        }

        double S = underlyingPrice.doubleValue();
        double K = strikePrice.doubleValue();
        double T = timeToExpiry;
        double r = riskFreeRate;
        double sigma = volatility;

        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        double price;
        if ("CALL".equals(optionType)) {
            price = S * normalCDF(d1) - K * Math.exp(-r * T) * normalCDF(d2);
        } else {
            price = K * Math.exp(-r * T) * normalCDF(-d2) - S * normalCDF(-d1);
        }

        return BigDecimal.valueOf(price).setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public BigDecimal calculateBinomialPrice(
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            double volatility,
            String optionType,
            int steps) {

        if (timeToExpiry <= 0) {
            // 已到期，返回内在价值
            if ("CALL".equals(optionType)) {
                return underlyingPrice.subtract(strikePrice).max(BigDecimal.ZERO);
            } else {
                return strikePrice.subtract(underlyingPrice).max(BigDecimal.ZERO);
            }
        }

        double S = underlyingPrice.doubleValue();
        double K = strikePrice.doubleValue();
        double T = timeToExpiry;
        double r = riskFreeRate;
        double sigma = volatility;
        int n = steps;

        double dt = T / n;
        double u = Math.exp(sigma * Math.sqrt(dt));
        double d = 1 / u;
        double p = (Math.exp(r * dt) - d) / (u - d);

        // 构建二项式树
        double[][] stockPrices = new double[n + 1][n + 1];
        double[][] optionValues = new double[n + 1][n + 1];

        // 计算股票价格树
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                stockPrices[i][j] = S * Math.pow(u, j) * Math.pow(d, i - j);
            }
        }

        // 计算期权价值（从到期日向前回溯）
        for (int j = 0; j <= n; j++) {
            if ("CALL".equals(optionType)) {
                optionValues[n][j] = Math.max(stockPrices[n][j] - K, 0);
            } else {
                optionValues[n][j] = Math.max(K - stockPrices[n][j], 0);
            }
        }

        // 向后回溯计算期权价值
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                double exerciseValue;
                if ("CALL".equals(optionType)) {
                    exerciseValue = Math.max(stockPrices[i][j] - K, 0);
                } else {
                    exerciseValue = Math.max(K - stockPrices[i][j], 0);
                }

                double holdValue = Math.exp(-r * dt) * (p * optionValues[i + 1][j + 1] + (1 - p) * optionValues[i + 1][j]);

                // 美式期权可以选择立即行使或继续持有
                optionValues[i][j] = Math.max(exerciseValue, holdValue);
            }
        }

        return BigDecimal.valueOf(optionValues[0][0]).setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public double calculateImpliedVolatility(
            BigDecimal marketPrice,
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            String optionType) {

        // 使用二分法计算隐含波动率
        double low = 0.001;
        double high = 5.0;
        double tolerance = 0.0001;
        double targetPrice = marketPrice.doubleValue();

        while (high - low > tolerance) {
            double mid = (low + high) / 2;
            BigDecimal calculatedPrice = calculateBlackScholesPrice(
                    underlyingPrice, strikePrice, timeToExpiry, riskFreeRate, mid, optionType);

            if (calculatedPrice.doubleValue() > targetPrice) {
                high = mid;
            } else {
                low = mid;
            }
        }

        return (low + high) / 2;
    }

    @Override
    public Map<String, Object> getPricingInfo(Long contractId) {
        Optional<OptionContract> contractOpt = optionContractRepository.findById(contractId);
        if (!contractOpt.isPresent()) {
            throw new RuntimeException("期权合约不存在");
        }

        OptionContract contract = contractOpt.get();
        Map<String, Object> result = new HashMap<>();

        // 计算到期时间（年）
        double timeToExpiry = calculateTimeToExpiry(contract.getExpiryDate());
        double riskFreeRate = 0.05; // 默认无风险利率5%
        double volatility = contract.getImpliedVolatility() != null ?
                contract.getImpliedVolatility().doubleValue() : 0.5; // 默认波动率50%

        // 计算理论价格
        BigDecimal theoreticalPrice;
        if ("EUROPEAN".equals(contract.getExerciseType())) {
            theoreticalPrice = calculateBlackScholesPrice(
                    contract.getUnderlyingPrice(),
                    contract.getStrikePrice(),
                    timeToExpiry,
                    riskFreeRate,
                    volatility,
                    contract.getOptionType()
            );
        } else {
            theoreticalPrice = calculateBinomialPrice(
                    contract.getUnderlyingPrice(),
                    contract.getStrikePrice(),
                    timeToExpiry,
                    riskFreeRate,
                    volatility,
                    contract.getOptionType(),
                    100 // 二项式树步数
            );
        }

        result.put("contractId", contractId);
        result.put("marketPrice", contract.getCurrentPrice());
        result.put("theoreticalPrice", theoreticalPrice);
        result.put("underlyingPrice", contract.getUnderlyingPrice());
        result.put("strikePrice", contract.getStrikePrice());
        result.put("timeToExpiry", timeToExpiry);
        result.put("impliedVolatility", contract.getImpliedVolatility());
        result.put("intrinsicValue", calculateIntrinsicValue(
                contract.getUnderlyingPrice(),
                contract.getStrikePrice(),
                contract.getOptionType()
        ));
        result.put("timeValue", theoreticalPrice.subtract(calculateIntrinsicValue(
                contract.getUnderlyingPrice(),
                contract.getStrikePrice(),
                contract.getOptionType()
        )));

        return result;
    }

    @Override
    public double calculateHistoricalVolatility(String pairName, int days) {
        // TODO: 实现历史波动率计算，需要从市场数据服务获取历史价格
        // 这里返回一个默认值，实际应该从市场数据计算
        return 0.5;
    }

    /**
     * 计算标准正态分布的累积分布函数（CDF）
     */
    private double normalCDF(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    /**
     * 误差函数近似
     */
    private double erf(double x) {
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        int sign = x < 0 ? -1 : 1;
        x = Math.abs(x);

        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }

    /**
     * 计算到期时间（年）
     */
    private double calculateTimeToExpiry(LocalDateTime expiryDate) {
        LocalDateTime now = LocalDateTime.now();
        if (expiryDate.isBefore(now)) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(now, expiryDate);
        return days / 365.0;
    }

    /**
     * 计算内在价值
     */
    private BigDecimal calculateIntrinsicValue(BigDecimal underlyingPrice, BigDecimal strikePrice, String optionType) {
        if ("CALL".equals(optionType)) {
            return underlyingPrice.subtract(strikePrice).max(BigDecimal.ZERO);
        } else {
            return strikePrice.subtract(underlyingPrice).max(BigDecimal.ZERO);
        }
    }
}















