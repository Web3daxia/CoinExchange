/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service.impl;

import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.entity.TraderApplication;
import com.cryptotrade.copytrading.repository.TraderApplicationRepository;
import com.cryptotrade.copytrading.repository.TraderRepository;
import com.cryptotrade.copytrading.service.TraderService;
import com.cryptotrade.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 带单员服务实现类
 */
@Service
public class TraderServiceImpl implements TraderService {

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private TraderApplicationRepository traderApplicationRepository;

    @Autowired
    private WalletService walletService;

    @Value("${copy-trading.min-assets:10000}")
    private BigDecimal minAssets; // 最小资产要求

    @Value("${copy-trading.margin-amount:1000}")
    private BigDecimal marginAmount; // 保证金金额

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public TraderApplication applyForTrader(Long userId, String traderType, Map<String, String> contactInfo,
                                            String assetProof, BigDecimal totalAssets) {
        // 检查是否已有申请
        Optional<TraderApplication> existingApp = traderApplicationRepository.findByUserIdAndStatus(userId, "PENDING");
        if (existingApp.isPresent()) {
            throw new RuntimeException("您已有待审核的申请");
        }

        // 检查是否已是带单员
        Optional<Trader> existingTrader = traderRepository.findByUserId(userId);
        if (existingTrader.isPresent() && "APPROVED".equals(existingTrader.get().getStatus())) {
            throw new RuntimeException("您已经是带单员");
        }

        // 检查资产要求
        if (totalAssets.compareTo(minAssets) < 0) {
            throw new RuntimeException("资产不足，最低要求: " + minAssets);
        }

        // 创建申请
        TraderApplication application = new TraderApplication();
        application.setUserId(userId);
        application.setTraderType(traderType);
        try {
            application.setContactInfo(objectMapper.writeValueAsString(contactInfo));
        } catch (Exception e) {
            application.setContactInfo("{}");
        }
        application.setAssetProof(assetProof);
        application.setTotalAssets(totalAssets);
        application.setStatus("PENDING");

        return traderApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public Trader reviewTraderApplication(Long applicationId, Boolean approved, String rejectReason, Long reviewerId) {
        TraderApplication application = traderApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));

        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("申请已审核");
        }

        application.setStatus(approved ? "APPROVED" : "REJECTED");
        application.setRejectReason(rejectReason);
        application.setReviewedBy(reviewerId);
        application.setReviewedAt(LocalDateTime.now());
        traderApplicationRepository.save(application);

        if (approved) {
            // 冻结保证金
            String currency = "USDT"; // 默认使用USDT
            BigDecimal balance = walletService.getAvailableBalance(application.getUserId(), "SPOT", currency);
            if (balance.compareTo(marginAmount) < 0) {
                throw new RuntimeException("余额不足，无法冻结保证金");
            }
            walletService.deductBalance(application.getUserId(), currency, "SPOT", marginAmount);

            // 创建带单员
            Trader trader = new Trader();
            trader.setUserId(application.getUserId());
            trader.setTraderType(application.getTraderType());
            trader.setLevel("BEGINNER"); // 默认初级
            trader.setMarginFrozen(marginAmount);
            trader.setStatus("APPROVED");
            trader.setPublicEnabled(false);
            trader.setPrivateEnabled(false);
            trader.setTotalFollowers(0);
            trader.setTotalAum(BigDecimal.ZERO);
            trader.setTotalProfit(BigDecimal.ZERO);
            trader.setTotalLoss(BigDecimal.ZERO);
            trader.setCommissionRate(new BigDecimal("0.1")); // 默认10%
            trader.setProfitShareRate(new BigDecimal("0.5")); // 默认50%分润

            return traderRepository.save(trader);
        }

        return null;
    }

    @Override
    public List<Trader> getPublicTraders(String traderType) {
        if (traderType != null && !traderType.isEmpty()) {
            return traderRepository.findByTraderTypeAndStatus(traderType, "APPROVED");
        }
        return traderRepository.findByPublicEnabledTrueAndStatus("APPROVED");
    }

    @Override
    public Trader getTrader(Long traderId) {
        return traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));
    }

    @Override
    @Transactional
    public boolean updateTraderLevel(Long traderId) {
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        // TODO: 根据业绩指标计算等级
        // 这里简化处理，实际应该根据收益率、AUM、跟单人数等综合计算
        String newLevel = calculateTraderLevel(trader);
        
        if (!newLevel.equals(trader.getLevel())) {
            trader.setLevel(newLevel);
            // 更新分润比例
            trader.setProfitShareRate(getProfitShareRateByLevel(newLevel));
            traderRepository.save(trader);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public String generateInviteCode(Long userId) {
        Trader trader = traderRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("您还不是带单员"));

        if (trader.getInviteCode() == null || trader.getInviteCode().isEmpty()) {
            String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            trader.setInviteCode(inviteCode);
            traderRepository.save(trader);
            return inviteCode;
        }

        return trader.getInviteCode();
    }

    @Override
    @Transactional
    public Trader updateCopyTradingSettings(Long userId, Boolean publicEnabled, Boolean privateEnabled,
                                           BigDecimal subscriptionFee) {
        Trader trader = traderRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("您还不是带单员"));

        if (publicEnabled != null) {
            trader.setPublicEnabled(publicEnabled);
        }
        if (privateEnabled != null) {
            trader.setPrivateEnabled(privateEnabled);
        }
        if (subscriptionFee != null) {
            trader.setSubscriptionFee(subscriptionFee);
        }

        return traderRepository.save(trader);
    }

    /**
     * 计算带单员等级
     */
    private String calculateTraderLevel(Trader trader) {
        // TODO: 根据实际业务规则计算等级
        // 简化版本：根据AUM和收益率判断
        if (trader.getTotalAum().compareTo(new BigDecimal("1000000")) > 0 &&
            trader.getWinRate() != null && trader.getWinRate().compareTo(new BigDecimal("0.7")) > 0) {
            return "TOP";
        } else if (trader.getTotalAum().compareTo(new BigDecimal("500000")) > 0) {
            return "ADVANCED";
        } else if (trader.getTotalAum().compareTo(new BigDecimal("100000")) > 0) {
            return "INTERMEDIATE";
        }
        return "BEGINNER";
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




