/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.service;

import com.cryptotrade.inviterebate.entity.InviteRelation;
import com.cryptotrade.inviterebate.entity.InviteReward;
import com.cryptotrade.inviterebate.entity.RebateConfig;
import com.cryptotrade.inviterebate.entity.RebateRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 邀请服务接口
 */
public interface InviteService {
    /**
     * 生成邀请链接和二维码
     */
    Map<String, String> generateInviteLink(Long userId);

    /**
     * 根据邀请码注册用户
     */
    InviteRelation registerByInviteCode(Long inviteeId, String inviteCode);

    /**
     * 获取邀请状态（邀请人数、奖励金额、返佣金额等）
     */
    Map<String, Object> getInviteStatus(Long userId);

    /**
     * 处理一次性奖励（注册、KYC、首笔交易）
     */
    InviteReward processReward(Long inviteeId, String rewardType);

    /**
     * 计算并记录返佣
     */
    RebateRecord calculateRebate(Long inviteeId, String tradeType, Long orderId, BigDecimal feeAmount, String feeCurrency);

    /**
     * 获取用户的返佣配置
     */
    RebateConfig getUserRebateConfig(Long userId);

    /**
     * 设置用户的返佣配置
     */
    RebateConfig setUserRebateConfig(Long userId, RebateConfig config);

    /**
     * 获取返佣记录列表
     */
    List<RebateRecord> getRebateRecords(Long userId, String status, String period);

    /**
     * 获取累计返佣金额
     */
    BigDecimal getTotalRebateAmount(Long userId);

    /**
     * 获取邀请的好友列表
     */
    List<InviteRelation> getInvitedUsers(Long userId);
}















