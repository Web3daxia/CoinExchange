/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 期权定价服务接口
 */
public interface OptionPricingService {
    /**
     * 使用Black-Scholes模型计算欧式期权价格
     * @param underlyingPrice 标的资产当前价格
     * @param strikePrice 执行价格
     * @param timeToExpiry 到期时间（年）
     * @param riskFreeRate 无风险利率
     * @param volatility 波动率
     * @param optionType CALL（看涨期权）或 PUT（看跌期权）
     * @return 期权理论价格
     */
    BigDecimal calculateBlackScholesPrice(
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            double volatility,
            String optionType
    );

    /**
     * 使用二项式模型计算美式期权价格
     * @param underlyingPrice 标的资产当前价格
     * @param strikePrice 执行价格
     * @param timeToExpiry 到期时间（年）
     * @param riskFreeRate 无风险利率
     * @param volatility 波动率
     * @param optionType CALL（看涨期权）或 PUT（看跌期权）
     * @param steps 二项式树步数
     * @return 期权理论价格
     */
    BigDecimal calculateBinomialPrice(
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            double volatility,
            String optionType,
            int steps
    );

    /**
     * 计算隐含波动率
     * @param marketPrice 市场价格
     * @param underlyingPrice 标的资产当前价格
     * @param strikePrice 执行价格
     * @param timeToExpiry 到期时间（年）
     * @param riskFreeRate 无风险利率
     * @param optionType CALL（看涨期权）或 PUT（看跌期权）
     * @return 隐含波动率
     */
    double calculateImpliedVolatility(
            BigDecimal marketPrice,
            BigDecimal underlyingPrice,
            BigDecimal strikePrice,
            double timeToExpiry,
            double riskFreeRate,
            String optionType
    );

    /**
     * 获取期权定价信息（包括市场价格、理论价格、隐含波动率等）
     * @param contractId 期权合约ID
     * @return 定价信息
     */
    Map<String, Object> getPricingInfo(Long contractId);

    /**
     * 计算历史波动率
     * @param pairName 交易对名称
     * @param days 天数
     * @return 历史波动率
     */
    double calculateHistoricalVolatility(String pairName, int days);
}















