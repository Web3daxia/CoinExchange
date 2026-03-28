/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.repository;

import com.cryptotrade.agent.entity.AgentRebateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 代理商返佣配置Repository
 */
@Repository
public interface AgentRebateConfigRepository extends JpaRepository<AgentRebateConfig, Long> {
    /**
     * 根据代理商ID查询返佣配置
     */
    Optional<AgentRebateConfig> findByAgentId(Long agentId);
}















