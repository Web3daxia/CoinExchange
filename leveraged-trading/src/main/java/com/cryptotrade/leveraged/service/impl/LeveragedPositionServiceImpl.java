/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedAccount;
import com.cryptotrade.leveraged.entity.LeveragedPosition;
import com.cryptotrade.leveraged.repository.LeveragedAccountRepository;
import com.cryptotrade.leveraged.repository.LeveragedPositionRepository;
import com.cryptotrade.leveraged.service.LeveragedPositionService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 杠杆仓位管理服务实现类
 */
@Service
public class LeveragedPositionServiceImpl implements LeveragedPositionService {

    @Autowired
    private LeveragedPositionRepository leveragedPositionRepository;

    @Autowired
    private LeveragedAccountRepository leveragedAccountRepository;

    @Autowired
    private WalletService walletService;

    @Override
    public List<LeveragedPosition> getUserPositions(Long userId) {
        return leveragedPositionRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    @Override
    public Map<String, Object> getAccountRisk(Long userId, String pairName) {
        Optional<LeveragedAccount> accountOpt = leveragedAccountRepository.findByUserIdAndPairName(userId, pairName);
        if (!accountOpt.isPresent()) {
            throw new RuntimeException("杠杆账户不存在");
        }

        LeveragedAccount account = accountOpt.get();
        List<LeveragedPosition> positions = leveragedPositionRepository.findByAccountIdAndStatus(account.getId(), "ACTIVE");

        BigDecimal totalMargin = BigDecimal.ZERO;
        BigDecimal totalBorrowed = BigDecimal.ZERO;
        BigDecimal totalUnrealizedPnl = BigDecimal.ZERO;
        BigDecimal totalMaintenanceMargin = BigDecimal.ZERO;

        for (LeveragedPosition position : positions) {
            totalMargin = totalMargin.add(position.getMargin());
            totalBorrowed = totalBorrowed.add(position.getBorrowedAmount());
            totalUnrealizedPnl = totalUnrealizedPnl.add(
                    position.getUnrealizedPnl() != null ? position.getUnrealizedPnl() : BigDecimal.ZERO);
            totalMaintenanceMargin = totalMaintenanceMargin.add(position.getMaintenanceMargin());
        }

        // 计算账户权益
        BigDecimal accountEquity = account.getAvailableBalance().add(totalMargin).add(totalUnrealizedPnl);

        // 计算保证金率
        BigDecimal marginRatio = BigDecimal.ZERO;
        if (totalMaintenanceMargin.compareTo(BigDecimal.ZERO) > 0) {
            marginRatio = accountEquity.divide(totalMaintenanceMargin, 4, RoundingMode.HALF_UP);
        }

        // 判断风险等级
        String riskLevel = "LOW";
        if (marginRatio.compareTo(new BigDecimal("1.1")) < 0) {
            riskLevel = "CRITICAL";
        } else if (marginRatio.compareTo(new BigDecimal("1.3")) < 0) {
            riskLevel = "HIGH";
        } else if (marginRatio.compareTo(new BigDecimal("1.5")) < 0) {
            riskLevel = "MEDIUM";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("accountId", account.getId());
        result.put("availableBalance", account.getAvailableBalance());
        result.put("totalMargin", totalMargin);
        result.put("totalBorrowed", totalBorrowed);
        result.put("totalUnrealizedPnl", totalUnrealizedPnl);
        result.put("accountEquity", accountEquity);
        result.put("maintenanceMargin", totalMaintenanceMargin);
        result.put("marginRatio", marginRatio);
        result.put("riskLevel", riskLevel);
        result.put("leverage", account.getLeverage());
        result.put("maxLeverage", account.getMaxLeverage());
        result.put("needLiquidation", riskLevel.equals("CRITICAL"));
        result.put("activePositions", positions.size());

        return result;
    }

    @Override
    @Transactional
    public LeveragedPosition topUpMargin(Long userId, Long positionId, BigDecimal amount) {
        LeveragedPosition position = leveragedPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此仓位");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            throw new RuntimeException("仓位状态不允许补充保证金");
        }

        // 检查余额
        String[] parts = position.getPairName().split("/");
        String currency = parts[1];
        BigDecimal balance = walletService.getAvailableBalance(userId, "SPOT", currency);
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 扣除资金
        walletService.deductBalance(userId, currency, "SPOT", amount);

        // 更新仓位保证金
        position.setMargin(position.getMargin().add(amount));
        position.setInitialMargin(position.getInitialMargin().add(amount));
        position.setMaintenanceMargin(position.getMaintenanceMargin().add(amount.multiply(new BigDecimal("0.5"))));

        // 重新计算强平价格
        position.setLiquidationPrice(calculateLiquidationPrice(position));
        position.setMarginRatio(calculateMarginRatio(position));

        // 更新账户
        LeveragedAccount account = leveragedAccountRepository.findById(position.getAccountId())
                .orElseThrow(() -> new RuntimeException("杠杆账户不存在"));
        account.setMargin(account.getMargin().add(amount));
        account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
        leveragedAccountRepository.save(account);

        return leveragedPositionRepository.save(position);
    }

    @Override
    @Transactional
    public boolean liquidatePosition(Long userId, Long positionId) {
        LeveragedPosition position = leveragedPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此仓位");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            return false;
        }

        // 获取当前价格
        BigDecimal currentPrice = getCurrentPrice(position.getPairName());

        // 计算盈亏
        BigDecimal pnl;
        if ("LONG".equals(position.getSide())) {
            pnl = currentPrice.subtract(position.getEntryPrice()).multiply(position.getQuantity());
        } else {
            pnl = position.getEntryPrice().subtract(currentPrice).multiply(position.getQuantity());
        }

        // 更新仓位状态
        position.setStatus("LIQUIDATED");
        position.setRealizedPnl(position.getRealizedPnl().add(pnl));
        leveragedPositionRepository.save(position);

        // 更新账户
        LeveragedAccount account = leveragedAccountRepository.findById(position.getAccountId())
                .orElseThrow(() -> new RuntimeException("杠杆账户不存在"));

        account.setMargin(account.getMargin().subtract(position.getMargin()));
        account.setBorrowedAmount(account.getBorrowedAmount().subtract(position.getBorrowedAmount()));
        account.setAvailableBalance(account.getAvailableBalance().add(position.getMargin()).add(pnl));
        leveragedAccountRepository.save(account);

        return true;
    }

    @Override
    @Transactional
    public void updatePositionPnl(Long positionId) {
        Optional<LeveragedPosition> positionOpt = leveragedPositionRepository.findById(positionId);
        if (!positionOpt.isPresent()) {
            return;
        }

        LeveragedPosition position = positionOpt.get();
        if (!"ACTIVE".equals(position.getStatus())) {
            return;
        }

        // 获取当前价格
        BigDecimal currentPrice = getCurrentPrice(position.getPairName());
        position.setCurrentPrice(currentPrice);

        // 计算未实现盈亏
        BigDecimal unrealizedPnl;
        if ("LONG".equals(position.getSide())) {
            unrealizedPnl = currentPrice.subtract(position.getEntryPrice()).multiply(position.getQuantity());
        } else {
            unrealizedPnl = position.getEntryPrice().subtract(currentPrice).multiply(position.getQuantity());
        }
        position.setUnrealizedPnl(unrealizedPnl);

        // 更新保证金率
        position.setMarginRatio(calculateMarginRatio(position));

        leveragedPositionRepository.save(position);
    }

    @Override
    public void updateAllPositionsPnl() {
        List<LeveragedPosition> positions = leveragedPositionRepository.findByStatus("ACTIVE");
        for (LeveragedPosition position : positions) {
            try {
                updatePositionPnl(position.getId());
            } catch (Exception e) {
                System.err.println("更新仓位盈亏失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    public BigDecimal calculateLiquidationPrice(LeveragedPosition position) {
        BigDecimal leverage = new BigDecimal(position.getLeverage());
        BigDecimal maintenanceMarginRate = position.getMaintenanceMargin()
                .divide(position.getInitialMargin(), 8, RoundingMode.HALF_UP);

        if ("LONG".equals(position.getSide())) {
            BigDecimal factor = BigDecimal.ONE.subtract(
                    BigDecimal.ONE.divide(leverage, 8, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.subtract(maintenanceMarginRate))
            );
            return position.getEntryPrice().multiply(factor);
        } else {
            BigDecimal factor = BigDecimal.ONE.add(
                    BigDecimal.ONE.divide(leverage, 8, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.subtract(maintenanceMarginRate))
            );
            return position.getEntryPrice().multiply(factor);
        }
    }

    @Override
    public BigDecimal calculateMarginRatio(LeveragedPosition position) {
        // 保证金率 = (保证金 + 未实现盈亏) / 维持保证金
        BigDecimal numerator = position.getMargin().add(
                position.getUnrealizedPnl() != null ? position.getUnrealizedPnl() : BigDecimal.ZERO);
        if (position.getMaintenanceMargin().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(position.getMaintenanceMargin(), 4, RoundingMode.HALF_UP);
    }

    /**
     * 获取当前价格（需要从市场数据服务获取）
     */
    private BigDecimal getCurrentPrice(String pairName) {
        // TODO: 从市场数据服务获取当前价格
        return new BigDecimal("50000");
    }
}




