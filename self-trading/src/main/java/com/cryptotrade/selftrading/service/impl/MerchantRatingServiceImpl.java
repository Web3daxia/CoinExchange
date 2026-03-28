/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service.impl;

import com.cryptotrade.selftrading.entity.Merchant;
import com.cryptotrade.selftrading.entity.MerchantRating;
import com.cryptotrade.selftrading.entity.SelfTradingOrder;
import com.cryptotrade.selftrading.repository.MerchantRatingRepository;
import com.cryptotrade.selftrading.repository.MerchantRepository;
import com.cryptotrade.selftrading.repository.SelfTradingOrderRepository;
import com.cryptotrade.selftrading.service.MerchantRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 商家评价服务实现类
 */
@Service
public class MerchantRatingServiceImpl implements MerchantRatingService {

    @Autowired
    private MerchantRatingRepository merchantRatingRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private SelfTradingOrderRepository selfTradingOrderRepository;

    @Override
    @Transactional
    public MerchantRating createRating(Long userId, Long merchantId, Long orderId,
                                       Integer rating, String comment, Boolean isAnonymous) {
        // 验证订单
        SelfTradingOrder order = selfTradingOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId) || !order.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("订单信息不匹配");
        }

        if (!"COMPLETED".equals(order.getStatus())) {
            throw new RuntimeException("订单未完成，无法评价");
        }

        // 检查是否已评价
        List<MerchantRating> existing = merchantRatingRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, "ACTIVE");
        boolean alreadyRated = existing.stream()
                .anyMatch(r -> r.getOrderId().equals(orderId));
        if (alreadyRated) {
            throw new RuntimeException("该订单已评价");
        }

        // 创建评价
        MerchantRating merchantRating = new MerchantRating();
        merchantRating.setMerchantId(merchantId);
        merchantRating.setUserId(userId);
        merchantRating.setOrderId(orderId);
        merchantRating.setRating(rating);
        merchantRating.setComment(comment);
        merchantRating.setIsAnonymous(isAnonymous != null && isAnonymous);
        merchantRating.setStatus("ACTIVE");

        merchantRating = merchantRatingRepository.save(merchantRating);

        // 更新商家评分
        updateMerchantRating(merchantId);

        return merchantRating;
    }

    @Override
    public List<MerchantRating> getMerchantRatings(Long merchantId) {
        return merchantRatingRepository.findByMerchantIdAndStatusOrderByCreatedAtDesc(merchantId, "ACTIVE");
    }

    @Override
    public List<MerchantRating> getUserRatings(Long userId) {
        return merchantRatingRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, "ACTIVE");
    }

    /**
     * 更新商家评分
     */
    @Transactional
    private void updateMerchantRating(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("商家不存在"));

        List<MerchantRating> ratings = merchantRatingRepository.findByMerchantIdAndStatusOrderByCreatedAtDesc(merchantId, "ACTIVE");
        
        if (ratings.isEmpty()) {
            merchant.setAvgRating(BigDecimal.ZERO);
            merchant.setRatingCount(0);
        } else {
            int totalRating = ratings.stream().mapToInt(MerchantRating::getRating).sum();
            BigDecimal avgRating = new BigDecimal(totalRating)
                    .divide(new BigDecimal(ratings.size()), 2, RoundingMode.HALF_UP);
            
            merchant.setTotalRating(new BigDecimal(totalRating));
            merchant.setRatingCount(ratings.size());
            merchant.setAvgRating(avgRating);
        }

        merchantRepository.save(merchant);
    }
}




