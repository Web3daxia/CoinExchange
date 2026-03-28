/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.service.RobotWalletService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 机器人钱包服务实现类
 */
@Service
public class RobotWalletServiceImpl implements RobotWalletService {

    @Autowired(required = false)
    private WalletService walletService;

    private static final String ROBOT_ACCOUNT_TYPE = "ROBOT"; // 机器人账户类型

    @Override
    public BigDecimal getBalance(Long userId, String currency) {
        if (walletService != null) {
            try {
                return walletService.getAvailableBalance(userId, ROBOT_ACCOUNT_TYPE, currency);
            } catch (Exception e) {
                // 如果钱包服务不可用，返回0
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void freezeBalance(Long userId, String currency, BigDecimal amount) {
        if (walletService != null) {
            try {
                walletService.freezeBalance(userId, ROBOT_ACCOUNT_TYPE, currency, amount);
            } catch (Exception e) {
                throw new RuntimeException("冻结资金失败: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void unfreezeBalance(Long userId, String currency, BigDecimal amount) {
        if (walletService != null) {
            try {
                walletService.unfreezeBalance(userId, ROBOT_ACCOUNT_TYPE, currency, amount);
            } catch (Exception e) {
                throw new RuntimeException("解冻资金失败: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void addBalance(Long userId, String currency, BigDecimal amount) {
        if (walletService != null) {
            try {
                walletService.addBalance(userId, ROBOT_ACCOUNT_TYPE, currency, amount);
            } catch (Exception e) {
                throw new RuntimeException("增加余额失败: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deductBalance(Long userId, String currency, BigDecimal amount) {
        if (walletService != null) {
            try {
                walletService.deductBalance(userId, ROBOT_ACCOUNT_TYPE, currency, amount);
            } catch (Exception e) {
                throw new RuntimeException("扣减余额失败: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean checkBalance(Long userId, String currency, BigDecimal amount) {
        BigDecimal balance = getBalance(userId, currency);
        return balance.compareTo(amount) >= 0;
    }
}

