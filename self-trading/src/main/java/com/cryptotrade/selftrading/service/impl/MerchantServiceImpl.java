/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service.impl;

import com.cryptotrade.selftrading.entity.Merchant;
import com.cryptotrade.selftrading.entity.MerchantApplication;
import com.cryptotrade.selftrading.entity.MerchantFollow;
import com.cryptotrade.selftrading.repository.MerchantApplicationRepository;
import com.cryptotrade.selftrading.repository.MerchantFollowRepository;
import com.cryptotrade.selftrading.repository.MerchantRepository;
import com.cryptotrade.selftrading.service.MerchantService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商家服务实现类
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantApplicationRepository merchantApplicationRepository;

    @Autowired
    private MerchantFollowRepository merchantFollowRepository;

    @Autowired
    private WalletService walletService;

    @Value("${self-trading.margin-amount:5000}")
    private BigDecimal marginAmount; // 保证金金额

    @Override
    @Transactional
    public MerchantApplication applyForMerchant(Long userId, String merchantName, String avatar,
                                               String signature, String bio, String country, String region,
                                               String assetProof, BigDecimal totalAssets) {
        // 检查是否已有申请
        Optional<MerchantApplication> existingApp = merchantApplicationRepository.findByUserIdAndStatus(userId, "PENDING");
        if (existingApp.isPresent()) {
            throw new RuntimeException("您已有待审核的申请");
        }

        // 检查是否已是商家
        Optional<Merchant> existingMerchant = merchantRepository.findByUserId(userId);
        if (existingMerchant.isPresent() && "APPROVED".equals(existingMerchant.get().getStatus())) {
            throw new RuntimeException("您已经是商家");
        }

        // 创建申请
        MerchantApplication application = new MerchantApplication();
        application.setUserId(userId);
        application.setMerchantName(merchantName);
        application.setAvatar(avatar);
        application.setSignature(signature);
        application.setBio(bio);
        application.setCountry(country);
        application.setRegion(region);
        application.setAssetProof(assetProof);
        application.setTotalAssets(totalAssets);
        application.setStatus("PENDING");

        return merchantApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public Merchant reviewMerchantApplication(Long applicationId, Boolean approved, String rejectReason, Long reviewerId) {
        MerchantApplication application = merchantApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));

        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("申请已审核");
        }

        application.setStatus(approved ? "APPROVED" : "REJECTED");
        application.setRejectReason(rejectReason);
        application.setReviewedBy(reviewerId);
        application.setReviewedAt(LocalDateTime.now());
        merchantApplicationRepository.save(application);

        if (approved) {
            // 冻结保证金
            String currency = "USDT";
            BigDecimal balance = walletService.getAvailableBalance(application.getUserId(), "SPOT", currency);
            if (balance.compareTo(marginAmount) < 0) {
                throw new RuntimeException("余额不足，无法冻结保证金");
            }
            walletService.deductBalance(application.getUserId(), currency, "SPOT", marginAmount);

            // 创建商家
            Merchant merchant = new Merchant();
            merchant.setUserId(application.getUserId());
            merchant.setMerchantName(application.getMerchantName());
            merchant.setAvatar(application.getAvatar());
            merchant.setSignature(application.getSignature());
            merchant.setBio(application.getBio());
            merchant.setCountry(application.getCountry());
            merchant.setRegion(application.getRegion());
            merchant.setLevel("NORMAL"); // 默认普通商家
            merchant.setIsShieldMerchant(false);
            merchant.setMarginFrozen(marginAmount);
            merchant.setStatus("APPROVED");
            merchant.setIsActive(true);
            merchant.setCompletedOrders(0);
            merchant.setCompletionRate(BigDecimal.ZERO);
            merchant.setAvgRating(BigDecimal.ZERO);
            merchant.setRatingCount(0);
            merchant.setFollowersCount(0);

            return merchantRepository.save(merchant);
        }

        return null;
    }

    @Override
    @Transactional
    public Merchant updateMerchantInfo(Long userId, String merchantName, String avatar,
                                      String signature, String bio) {
        Merchant merchant = merchantRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("您还不是商家"));

        if (merchantName != null) {
            merchant.setMerchantName(merchantName);
        }
        if (avatar != null) {
            merchant.setAvatar(avatar);
        }
        if (signature != null) {
            merchant.setSignature(signature);
        }
        if (bio != null) {
            merchant.setBio(bio);
        }

        return merchantRepository.save(merchant);
    }

    @Override
    @Transactional
    public Merchant setMerchantActive(Long userId, Boolean isActive) {
        Merchant merchant = merchantRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("您还不是商家"));

        merchant.setIsActive(isActive);
        return merchantRepository.save(merchant);
    }

    @Override
    public List<Merchant> getMerchants(Map<String, Object> filters) {
        // TODO: 根据筛选条件查询商家
        // 这里简化处理，实际应该根据filters动态构建查询
        return merchantRepository.findByStatusAndIsActiveTrue("APPROVED");
    }

    @Override
    public Merchant getMerchant(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("商家不存在"));
    }

    @Override
    @Transactional
    public void followMerchant(Long userId, Long merchantId, Boolean follow) {
        Optional<MerchantFollow> existing = merchantFollowRepository.findByMerchantIdAndUserId(merchantId, userId);

        if (follow) {
            if (!existing.isPresent()) {
                MerchantFollow merchantFollow = new MerchantFollow();
                merchantFollow.setMerchantId(merchantId);
                merchantFollow.setUserId(userId);
                merchantFollowRepository.save(merchantFollow);

                // 更新商家关注人数
                Merchant merchant = merchantRepository.findById(merchantId)
                        .orElseThrow(() -> new RuntimeException("商家不存在"));
                merchant.setFollowersCount(merchant.getFollowersCount() + 1);
                merchantRepository.save(merchant);
            }
        } else {
            existing.ifPresent(merchantFollowRepository::delete);

            // 更新商家关注人数
            Merchant merchant = merchantRepository.findById(merchantId)
                    .orElseThrow(() -> new RuntimeException("商家不存在"));
            merchant.setFollowersCount(Math.max(0, merchant.getFollowersCount() - 1));
            merchantRepository.save(merchant);
        }
    }

    @Override
    @Transactional
    public void updateMerchantLevel(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("商家不存在"));

        // TODO: 根据商家表现计算等级
        // 这里简化处理，实际应该根据完成订单量、评分、完成率等综合计算
        String newLevel = calculateMerchantLevel(merchant);
        if (!newLevel.equals(merchant.getLevel())) {
            merchant.setLevel(newLevel);
            merchantRepository.save(merchant);
        }
    }

    /**
     * 计算商家等级
     */
    private String calculateMerchantLevel(Merchant merchant) {
        // TODO: 根据实际业务规则计算等级
        // 简化版本：根据完成订单量和评分判断
        if (merchant.getCompletedOrders() != null && merchant.getCompletedOrders() > 1000 &&
            merchant.getAvgRating() != null && merchant.getAvgRating().compareTo(new BigDecimal("4.5")) > 0) {
            return "CROWN";
        } else if (merchant.getCompletedOrders() != null && merchant.getCompletedOrders() > 100 &&
                   merchant.getAvgRating() != null && merchant.getAvgRating().compareTo(new BigDecimal("4.0")) > 0) {
            return "BLUE_V";
        }
        return "NORMAL";
    }
}




