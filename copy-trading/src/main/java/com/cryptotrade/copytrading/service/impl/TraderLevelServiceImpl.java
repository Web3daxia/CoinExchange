/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service.impl;

import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.repository.TraderRepository;
import com.cryptotrade.copytrading.service.TraderLevelService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 带单员等级和分润服务实现类
 */
@Service
public class TraderLevelServiceImpl implements TraderLevelService {

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public boolean calculateAndUpdateLevel(Long traderId) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        String oldLevel = trader.getLevel();
        String newLevel = calculateLevel(trader);

        if (!newLevel.equals(oldLevel)) {
            trader.setLevel(newLevel);
            trader.setProfitShareRate(getProfitShareRateByLevel(newLevel));
            traderRepository.save(trader);
            return true;
        }

        return false;
    }

    @Override
    public BigDecimal calculateProfitShare(Long traderId, Long followerId, BigDecimal profit) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        if (profit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 分润 = 盈利 * 分润比例
        BigDecimal profitShare = profit.multiply(trader.getProfitShareRate())
                .setScale(8, RoundingMode.HALF_UP);

        return profitShare;
    }

    @Override
    @Transactional
    public void settleProfitShare(Long traderId, Long followerId, BigDecimal profit, BigDecimal commission) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        // 计算分润
        BigDecimal profitShare = calculateProfitShare(traderId, followerId, profit);

        // 更新带单员统计
        if (profit.compareTo(BigDecimal.ZERO) > 0) {
            trader.setTotalProfit(trader.getTotalProfit().add(profit));
        } else {
            trader.setTotalLoss(trader.getTotalLoss().add(profit.abs()));
        }

        // 转账分润给带单员
        if (profitShare.compareTo(BigDecimal.ZERO) > 0) {
            String currency = "USDT";
            walletService.addBalance(traderId, currency, "SPOT", profitShare);
        }

        traderRepository.save(trader);
    }

    /**
     * 计算带单员等级
     */
    private String calculateLevel(Trader trader) {
        // 综合评分计算
        BigDecimal score = BigDecimal.ZERO;

        // AUM评分（最高40分）
        BigDecimal aumScore = trader.getTotalAum().divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)
                .min(new BigDecimal("40"));

        // 收益率评分（最高30分）
        BigDecimal returnRate = calculateReturnRate(trader);
        BigDecimal returnScore = returnRate.multiply(new BigDecimal("30"))
                .min(new BigDecimal("30"));

        // 胜率评分（最高20分）
        BigDecimal winRateScore = BigDecimal.ZERO;
        if (trader.getWinRate() != null) {
            winRateScore = trader.getWinRate().multiply(new BigDecimal("20"));
        }

        // 跟单人数评分（最高10分）
        BigDecimal followerScore = new BigDecimal(trader.getTotalFollowers())
                .divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP)
                .min(new BigDecimal("10"));

        score = aumScore.add(returnScore).add(winRateScore).add(followerScore);

        // 根据评分确定等级
        if (score.compareTo(new BigDecimal("80")) >= 0) {
            return "TOP";
        } else if (score.compareTo(new BigDecimal("60")) >= 0) {
            return "ADVANCED";
        } else if (score.compareTo(new BigDecimal("40")) >= 0) {
            return "INTERMEDIATE";
        } else {
            return "BEGINNER";
        }
    }

    /**
     * 计算收益率
     */
    private BigDecimal calculateReturnRate(Trader trader) {
        if (trader.getTotalAum().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal netProfit = trader.getTotalProfit().subtract(trader.getTotalLoss());
        return netProfit.divide(trader.getTotalAum(), 4, RoundingMode.HALF_UP);
    }

    /**
     * 根据等级获取分润比例
     */
    private BigDecimal getProfitShareRateByLevel(String level) {
        switch (level) {
            case "TOP":
                return new BigDecimal("0.8"); // 80%
            case "ADVANCED":
                return new BigDecimal("0.7"); // 70%
            case "INTERMEDIATE":
                return new BigDecimal("0.6"); // 60%
            default:
                return new BigDecimal("0.5"); // 50%
        }
    }
}















