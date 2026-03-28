/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.repository;

import com.cryptotrade.agent.entity.AgentUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 代理商用户关系Repository
 */
@Repository
public interface AgentUserRelationRepository extends JpaRepository<AgentUserRelation, Long> {
    /**
     * 根据代理商ID查询所有用户关系
     */
    List<AgentUserRelation> findByAgentId(Long agentId);

    /**
     * 根据用户ID查询关系
     */
    Optional<AgentUserRelation> findByUserId(Long userId);

    /**
     * 根据代理商ID和状态查询
     */
    List<AgentUserRelation> findByAgentIdAndStatus(Long agentId, String status);

    /**
     * 统计代理商邀请的用户数量
     */
    @Query("SELECT COUNT(aur) FROM AgentUserRelation aur WHERE aur.agentId = :agentId AND aur.status = 'ACTIVE'")
    Long countByAgentId(@Param("agentId") Long agentId);

    /**
     * 根据用户分组查询
     */
    List<AgentUserRelation> findByAgentIdAndUserGroup(Long agentId, String userGroup);
}















