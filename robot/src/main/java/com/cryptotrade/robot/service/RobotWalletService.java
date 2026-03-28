/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import java.math.BigDecimal;

/**
 * 机器人钱包服务接口
 * 用于机器人的资金管理（与钱包模块集成）
 */
public interface RobotWalletService {
    /**
     * 获取机器人账户余额
     */
    BigDecimal getBalance(Long userId, String currency);

    /**
     * 冻结资金（用于订单保证金等）
     */
    void freezeBalance(Long userId, String currency, BigDecimal amount);

    /**
     * 解冻资金
     */
    void unfreezeBalance(Long userId, String currency, BigDecimal amount);

    /**
     * 增加余额（盈利）
     */
    void addBalance(Long userId, String currency, BigDecimal amount);

    /**
     * 扣减余额（亏损、手续费）
     */
    void deductBalance(Long userId, String currency, BigDecimal amount);

    /**
     * 检查余额是否充足
     */
    boolean checkBalance(Long userId, String currency, BigDecimal amount);
}













