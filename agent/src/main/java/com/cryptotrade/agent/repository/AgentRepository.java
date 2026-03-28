/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.repository;

import com.cryptotrade.agent.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 代理商Repository
 */
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    /**
     * 根据用户ID查询代理商
     */
    Optional<Agent> findByUserId(Long userId);

    /**
     * 根据代理商编码查询
     */
    Optional<Agent> findByAgentCode(String agentCode);

    /**
     * 根据状态查询代理商列表
     */
    List<Agent> findByStatus(String status);

    /**
     * 根据等级查询代理商列表
     */
    List<Agent> findByLevel(String level);
}

