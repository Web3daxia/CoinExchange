/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service;

import com.cryptotrade.copytrading.entity.Trader;

import java.math.BigDecimal;

/**
 * 带单员等级和分润服务接口
 */
public interface TraderLevelService {
    /**
     * 计算并更新带单员等级
     * @param traderId 带单员ID
     * @return 是否更新了等级
     */
    boolean calculateAndUpdateLevel(Long traderId);

    /**
     * 计算分润
     * @param traderId 带单员ID
     * @param followerId 跟单员ID
     * @param profit 盈利金额
     * @return 分润金额
     */
    BigDecimal calculateProfitShare(Long traderId, Long followerId, BigDecimal profit);

    /**
     * 结算分润
     * @param traderId 带单员ID
     * @param followerId 跟单员ID
     * @param profit 盈利金额
     * @param commission 佣金金额
     */
    void settleProfitShare(Long traderId, Long followerId, BigDecimal profit, BigDecimal commission);
}















