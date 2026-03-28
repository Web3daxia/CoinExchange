/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.service;

import com.cryptotrade.agent.entity.Agent;
import com.cryptotrade.agent.entity.AgentRebateConfig;
import com.cryptotrade.agent.entity.AgentRebateRecord;
import com.cryptotrade.agent.entity.AgentUserRelation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 代理商服务接口
 */
public interface AgentService {
    /**
     * 申请成为代理商
     */
    Agent applyAgent(Long userId, String agentName);

    /**
     * 生成代理商邀请链接和二维码
     */
    Map<String, String> generateInviteLink(Long agentId);

    /**
     * 根据邀请码注册用户（关联到代理商）
     */
    AgentUserRelation registerUserByInviteCode(Long userId, String inviteCode);

    /**
     * 获取代理商邀请的所有用户信息
     */
    List<Map<String, Object>> getAgentUsers(Long agentId, String userGroup);

    /**
     * 查询代理商的返佣情况
     */
    Map<String, Object> getAgentCommission(Long agentId, String status, String period);

    /**
     * 获取代理商业绩报告
     */
    Map<String, Object> getAgentPerformance(Long agentId, String period);

    /**
     * 计算并记录代理商返佣
     */
    AgentRebateRecord calculateAgentRebate(Long userId, String tradeType, Long orderId, BigDecimal feeAmount, String feeCurrency);

    /**
     * 获取代理商的返佣配置
     */
    AgentRebateConfig getAgentRebateConfig(Long agentId);

    /**
     * 用户分组
     */
    void groupUser(Long agentId, Long userId, String userGroup);
}















