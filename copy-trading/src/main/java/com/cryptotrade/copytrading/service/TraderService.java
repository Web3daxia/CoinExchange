/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service;

import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.entity.TraderApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 带单员服务接口
 */
public interface TraderService {
    /**
     * 申请成为带单员
     * @param userId 用户ID
     * @param traderType 交易员类型
     * @param contactInfo 联系信息
     * @param assetProof 资产证明
     * @param totalAssets 总资产
     * @return 申请记录
     */
    TraderApplication applyForTrader(Long userId, String traderType, Map<String, String> contactInfo,
                                    String assetProof, BigDecimal totalAssets);

    /**
     * 审核带单员申请
     * @param applicationId 申请ID
     * @param approved 是否通过
     * @param rejectReason 拒绝原因
     * @param reviewerId 审核人ID
     * @return 带单员
     */
    Trader reviewTraderApplication(Long applicationId, Boolean approved, String rejectReason, Long reviewerId);

    /**
     * 查询带单员列表（公域）
     * @param traderType 交易员类型（可选）
     * @return 带单员列表
     */
    List<Trader> getPublicTraders(String traderType);

    /**
     * 查询带单员详情
     * @param traderId 带单员ID
     * @return 带单员
     */
    Trader getTrader(Long traderId);

    /**
     * 更新带单员等级
     * @param traderId 带单员ID
     * @return 是否更新成功
     */
    boolean updateTraderLevel(Long traderId);

    /**
     * 生成邀请码（私域跟单）
     * @param userId 用户ID
     * @return 邀请码
     */
    String generateInviteCode(Long userId);

    /**
     * 设置公域/私域跟单
     * @param userId 用户ID
     * @param publicEnabled 是否开启公域
     * @param privateEnabled 是否开启私域
     * @param subscriptionFee 订阅费（私域）
     * @return 带单员
     */
    Trader updateCopyTradingSettings(Long userId, Boolean publicEnabled, Boolean privateEnabled,
                                    BigDecimal subscriptionFee);
}















