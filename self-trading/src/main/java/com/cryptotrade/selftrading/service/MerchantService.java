/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service;

import com.cryptotrade.selftrading.entity.Merchant;
import com.cryptotrade.selftrading.entity.MerchantApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商家服务接口
 */
public interface MerchantService {
    /**
     * 申请成为商家
     * @param userId 用户ID
     * @param merchantName 商家名称
     * @param avatar 头像
     * @param signature 签名
     * @param bio 个人简介
     * @param country 国家
     * @param region 地区
     * @param assetProof 资产证明
     * @param totalAssets 总资产
     * @return 申请记录
     */
    MerchantApplication applyForMerchant(Long userId, String merchantName, String avatar,
                                        String signature, String bio, String country, String region,
                                        String assetProof, BigDecimal totalAssets);

    /**
     * 审核商家申请
     * @param applicationId 申请ID
     * @param approved 是否通过
     * @param rejectReason 拒绝原因
     * @param reviewerId 审核人ID
     * @return 商家
     */
    Merchant reviewMerchantApplication(Long applicationId, Boolean approved, String rejectReason, Long reviewerId);

    /**
     * 更新商家信息
     * @param userId 用户ID
     * @param merchantName 商家名称
     * @param avatar 头像
     * @param signature 签名
     * @param bio 个人简介
     * @return 商家
     */
    Merchant updateMerchantInfo(Long userId, String merchantName, String avatar,
                               String signature, String bio);

    /**
     * 设置商家是否可交易
     * @param userId 用户ID
     * @param isActive 是否可交易
     * @return 商家
     */
    Merchant setMerchantActive(Long userId, Boolean isActive);

    /**
     * 查询商家列表
     * @param filters 筛选条件（国家、等级、神盾商家等）
     * @return 商家列表
     */
    List<Merchant> getMerchants(Map<String, Object> filters);

    /**
     * 查询商家详情
     * @param merchantId 商家ID
     * @return 商家
     */
    Merchant getMerchant(Long merchantId);

    /**
     * 关注/取消关注商家
     * @param userId 用户ID
     * @param merchantId 商家ID
     * @param follow 是否关注
     */
    void followMerchant(Long userId, Long merchantId, Boolean follow);

    /**
     * 更新商家等级
     * @param merchantId 商家ID
     */
    void updateMerchantLevel(Long merchantId);
}















