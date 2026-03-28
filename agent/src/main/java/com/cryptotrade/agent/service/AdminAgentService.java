/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.service;

import com.cryptotrade.agent.entity.Agent;
import com.cryptotrade.agent.entity.AgentRebateConfig;

import java.util.List;
import java.util.Map;

/**
 * 后台代理商管理服务接口
 */
public interface AdminAgentService {
    /**
     * 审核代理商申请
     */
    Agent reviewAgentApplication(Long agentId, Long adminUserId, String status, String remark);

    /**
     * 设置代理商返佣比例
     */
    AgentRebateConfig setAgentRebateConfig(Long agentId, AgentRebateConfig config);

    /**
     * 调整代理商等级
     */
    Agent updateAgentLevel(Long agentId, String level);

    /**
     * 获取代理商业绩列表
     */
    List<Map<String, Object>> getAgentPerformanceList(String sortBy, Integer limit);

    /**
     * 获取代理商排行榜
     */
    List<Map<String, Object>> getAgentRanking(String rankType, Integer limit);
}

