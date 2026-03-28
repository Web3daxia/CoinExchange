/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import java.math.BigDecimal;

public interface FundingRateService {
    /**
     * 获取资金费率
     * @param pairName 交易对名称
     * @return 资金费率
     */
    BigDecimal getFundingRate(String pairName);

    /**
     * 计算资金费用
     * @param positionValue 持仓价值
     * @param fundingRate 资金费率
     * @return 资金费用
     */
    BigDecimal calculateFundingFee(BigDecimal positionValue, BigDecimal fundingRate);

    /**
     * 结算资金费用
     * @param pairName 交易对名称
     */
    void settleFundingFee(String pairName);

    /**
     * 结算所有交易对的资金费用
     */
    void settleAllFundingFees();

    /**
     * 定时结算资金费用（每8小时）
     */
    void scheduleFundingSettlement();
}















