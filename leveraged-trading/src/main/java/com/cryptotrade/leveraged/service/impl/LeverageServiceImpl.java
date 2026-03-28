/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedAccount;
import com.cryptotrade.leveraged.entity.LeveragedPosition;
import com.cryptotrade.leveraged.repository.LeveragedAccountRepository;
import com.cryptotrade.leveraged.repository.LeveragedPositionRepository;
import com.cryptotrade.leveraged.service.LeverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 杠杆设置服务实现类
 */
@Service
public class LeverageServiceImpl implements LeverageService {

    @Autowired
    private LeveragedAccountRepository leveragedAccountRepository;

    @Autowired
    private LeveragedPositionRepository leveragedPositionRepository;

    // 默认最大杠杆倍数
    private static final int DEFAULT_MAX_LEVERAGE = 20;
    // 系统最大杠杆倍数
    private static final int SYSTEM_MAX_LEVERAGE = 100;

    @Override
    @Transactional
    public LeveragedAccount adjustLeverage(Long userId, String pairName, Integer leverage) {
        // 验证杠杆倍数范围
        if (leverage < 1 || leverage > SYSTEM_MAX_LEVERAGE) {
            throw new RuntimeException("杠杆倍数必须在1-" + SYSTEM_MAX_LEVERAGE + "之间");
        }

        // 获取或创建杠杆账户
        LeveragedAccount account = getOrCreateAccount(userId, pairName);

        // 检查是否超过最大杠杆限制
        if (leverage > account.getMaxLeverage()) {
            throw new RuntimeException("杠杆倍数超过设定的最大杠杆限制: " + account.getMaxLeverage());
        }

        // 检查是否可以调整杠杆（只有在未开仓或平仓的情况下才能调整）
        if (!canAdjustLeverage(account.getId())) {
            throw new RuntimeException("存在未平仓的仓位，无法调整杠杆倍数");
        }

        // 更新杠杆倍数
        account.setLeverage(leverage);

        return leveragedAccountRepository.save(account);
    }

    @Override
    public LeveragedAccount getLeverage(Long userId, String pairName) {
        Optional<LeveragedAccount> accountOpt = leveragedAccountRepository.findByUserIdAndPairName(userId, pairName);
        if (accountOpt.isPresent()) {
            return accountOpt.get();
        }

        // 如果账户不存在，返回默认杠杆账户
        LeveragedAccount defaultAccount = new LeveragedAccount();
        defaultAccount.setUserId(userId);
        defaultAccount.setPairName(pairName);
        defaultAccount.setLeverage(1);
        defaultAccount.setMaxLeverage(DEFAULT_MAX_LEVERAGE);
        defaultAccount.setAvailableBalance(BigDecimal.ZERO);
        defaultAccount.setBorrowedAmount(BigDecimal.ZERO);
        defaultAccount.setMargin(BigDecimal.ZERO);
        defaultAccount.setStatus("ACTIVE");
        return defaultAccount;
    }

    @Override
    @Transactional
    public boolean autoAdjustLeverage(Long accountId) {
        Optional<LeveragedAccount> accountOpt = leveragedAccountRepository.findById(accountId);
        if (!accountOpt.isPresent()) {
            return false;
        }

        LeveragedAccount account = accountOpt.get();

        // TODO: 根据市场波动情况动态调整杠杆
        // 这里需要从市场数据服务获取波动率数据
        // 如果波动率较高，降低杠杆；如果波动率较低，可以适当提高杠杆

        // 检查是否有活跃仓位
        List<LeveragedPosition> positions = leveragedPositionRepository.findByAccountIdAndStatus(accountId, "ACTIVE");
        if (!positions.isEmpty()) {
            // 有活跃仓位时，不能自动调整杠杆
            return false;
        }

        // 根据市场情况调整杠杆（示例逻辑）
        // 这里简化处理，实际应该根据市场波动率计算
        int currentLeverage = account.getLeverage();
        int newLeverage = currentLeverage;

        // 如果当前杠杆小于最大杠杆，可以适当提高
        if (currentLeverage < account.getMaxLeverage()) {
            // 根据市场情况决定是否提高杠杆
            // 这里简化处理，实际应该根据市场数据
        }

        if (newLeverage != currentLeverage) {
            account.setLeverage(newLeverage);
            leveragedAccountRepository.save(account);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public LeveragedAccount setMaxLeverage(Long userId, String pairName, Integer maxLeverage) {
        if (maxLeverage < 1 || maxLeverage > SYSTEM_MAX_LEVERAGE) {
            throw new RuntimeException("最大杠杆倍数必须在1-" + SYSTEM_MAX_LEVERAGE + "之间");
        }

        LeveragedAccount account = getOrCreateAccount(userId, pairName);

        // 如果当前杠杆超过新的最大杠杆，需要先降低当前杠杆
        if (account.getLeverage() > maxLeverage) {
            account.setLeverage(maxLeverage);
        }

        account.setMaxLeverage(maxLeverage);

        return leveragedAccountRepository.save(account);
    }

    @Override
    public boolean canAdjustLeverage(Long accountId) {
        // 检查是否有活跃仓位
        List<LeveragedPosition> positions = leveragedPositionRepository.findByAccountIdAndStatus(accountId, "ACTIVE");
        return positions.isEmpty();
    }

    /**
     * 获取或创建杠杆账户
     */
    private LeveragedAccount getOrCreateAccount(Long userId, String pairName) {
        Optional<LeveragedAccount> accountOpt = leveragedAccountRepository.findByUserIdAndPairName(userId, pairName);
        if (accountOpt.isPresent()) {
            return accountOpt.get();
        }

        // 创建新账户
        LeveragedAccount account = new LeveragedAccount();
        account.setUserId(userId);
        account.setPairName(pairName);
        account.setLeverage(1);
        account.setMaxLeverage(DEFAULT_MAX_LEVERAGE);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setBorrowedAmount(BigDecimal.ZERO);
        account.setMargin(BigDecimal.ZERO);
        account.setInitialMargin(BigDecimal.ZERO);
        account.setMaintenanceMargin(BigDecimal.ZERO);
        account.setInterestRate(new BigDecimal("0.0001")); // 默认利率0.01%
        account.setStatus("ACTIVE");

        return leveragedAccountRepository.save(account);
    }
}















