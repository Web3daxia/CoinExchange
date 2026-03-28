/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedAccount;

/**
 * 杠杆设置服务接口
 */
public interface LeverageService {
    /**
     * 调整用户账户的杠杆倍数
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param leverage 杠杆倍数
     * @return 杠杆账户
     */
    LeveragedAccount adjustLeverage(Long userId, String pairName, Integer leverage);

    /**
     * 查询用户当前杠杆倍数
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @return 杠杆账户
     */
    LeveragedAccount getLeverage(Long userId, String pairName);

    /**
     * 动态调整杠杆（根据市场波动情况）
     * @param accountId 账户ID
     * @return 是否调整成功
     */
    boolean autoAdjustLeverage(Long accountId);

    /**
     * 设置最大杠杆限制
     * @param userId 用户ID
     * @param pairName 交易对名称
     * @param maxLeverage 最大杠杆倍数
     * @return 杠杆账户
     */
    LeveragedAccount setMaxLeverage(Long userId, String pairName, Integer maxLeverage);

    /**
     * 检查杠杆是否可以调整
     * @param accountId 账户ID
     * @return 是否可以调整
     */
    boolean canAdjustLeverage(Long accountId);
}















