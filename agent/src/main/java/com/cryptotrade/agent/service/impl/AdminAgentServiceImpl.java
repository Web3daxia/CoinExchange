/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.service.impl;

import com.cryptotrade.agent.entity.Agent;
import com.cryptotrade.agent.entity.AgentRebateConfig;
import com.cryptotrade.agent.repository.*;
import com.cryptotrade.agent.service.AdminAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台代理商管理服务实现
 */
@Service
public class AdminAgentServiceImpl implements AdminAgentService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentRebateConfigRepository agentRebateConfigRepository;

    @Autowired
    private AgentRebateRecordRepository agentRebateRecordRepository;

    @Autowired
    private AgentUserRelationRepository agentUserRelationRepository;

    @Override
    @Transactional
    public Agent reviewAgentApplication(Long agentId, Long adminUserId, String status, String remark) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("代理商不存在"));

        agent.setStatus(status);
        agent.setApproveUserId(adminUserId);
        agent.setUpdatedAt(LocalDateTime.now());

        if ("APPROVED".equals(status)) {
            agent.setApproveTime(LocalDateTime.now());
        } else if ("REJECTED".equals(status)) {
            agent.setRejectReason(remark);
        }

        return agentRepository.save(agent);
    }

    @Override
    @Transactional
    public AgentRebateConfig setAgentRebateConfig(Long agentId, AgentRebateConfig config) {
        Optional<AgentRebateConfig> existingOpt = agentRebateConfigRepository.findByAgentId(agentId);
        
        if (existingOpt.isPresent()) {
            AgentRebateConfig existing = existingOpt.get();
            if (config.getSpotRebateRate() != null) {
                existing.setSpotRebateRate(config.getSpotRebateRate());
            }
            if (config.getFuturesUsdtRebateRate() != null) {
                existing.setFuturesUsdtRebateRate(config.getFuturesUsdtRebateRate());
            }
            if (config.getFuturesCoinRebateRate() != null) {
                existing.setFuturesCoinRebateRate(config.getFuturesCoinRebateRate());
            }
            if (config.getCopyTradingRebateRate() != null) {
                existing.setCopyTradingRebateRate(config.getCopyTradingRebateRate());
            }
            if (config.getOptionsRebateRate() != null) {
                existing.setOptionsRebateRate(config.getOptionsRebateRate());
            }
            if (config.getRebatePeriod() != null) {
                existing.setRebatePeriod(config.getRebatePeriod());
            }
            if (config.getStatus() != null) {
                existing.setStatus(config.getStatus());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return agentRebateConfigRepository.save(existing);
        } else {
            config.setAgentId(agentId);
            config.setStatus("ACTIVE");
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            return agentRebateConfigRepository.save(config);
        }
    }

    @Override
    @Transactional
    public Agent updateAgentLevel(Long agentId, String level) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("代理商不存在"));

        agent.setLevel(level);
        agent.setUpdatedAt(LocalDateTime.now());
        return agentRepository.save(agent);
    }

    @Override
    public List<Map<String, Object>> getAgentPerformanceList(String sortBy, Integer limit) {
        List<Agent> agents = agentRepository.findByStatus("APPROVED");
        
        List<Map<String, Object>> performanceList = agents.stream().map(agent -> {
            Map<String, Object> perf = new HashMap<>();
            perf.put("agentId", agent.getId());
            perf.put("agentName", agent.getAgentName());
            perf.put("level", agent.getLevel());
            
            // 邀请用户数
            Long inviteCount = agentUserRelationRepository.countByAgentId(agent.getId());
            perf.put("inviteUserCount", inviteCount);
            
            // 总返佣金额
            BigDecimal totalRebate = agentRebateRecordRepository.sumRebateAmountByAgentId(agent.getId());
            perf.put("totalRebate", totalRebate);
            
            return perf;
        }).collect(Collectors.toList());

        // 排序
        if ("rebate".equals(sortBy)) {
            performanceList.sort((a, b) -> ((BigDecimal) b.get("totalRebate")).compareTo((BigDecimal) a.get("totalRebate")));
        } else if ("users".equals(sortBy)) {
            performanceList.sort((a, b) -> ((Long) b.get("inviteUserCount")).compareTo((Long) a.get("inviteUserCount")));
        }

        // 限制数量
        if (limit != null && limit > 0) {
            return performanceList.stream().limit(limit).collect(Collectors.toList());
        }

        return performanceList;
    }

    @Override
    public List<Map<String, Object>> getAgentRanking(String rankType, Integer limit) {
        return getAgentPerformanceList(rankType, limit);
    }
}















