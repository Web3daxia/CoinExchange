/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service;

import com.cryptotrade.selftrading.entity.MerchantRating;

import java.util.List;

/**
 * 商家评价服务接口
 */
public interface MerchantRatingService {
    /**
     * 创建评价
     * @param userId 用户ID
     * @param merchantId 商家ID
     * @param orderId 订单ID
     * @param rating 评分
     * @param comment 评论
     * @param isAnonymous 是否匿名
     * @return 评价
     */
    MerchantRating createRating(Long userId, Long merchantId, Long orderId,
                               Integer rating, String comment, Boolean isAnonymous);

    /**
     * 查询商家评价
     * @param merchantId 商家ID
     * @return 评价列表
     */
    List<MerchantRating> getMerchantRatings(Long merchantId);

    /**
     * 查询用户评价
     * @param userId 用户ID
     * @return 评价列表
     */
    List<MerchantRating> getUserRatings(Long userId);
}















