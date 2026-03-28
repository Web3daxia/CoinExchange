/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.repository;

import com.cryptotrade.agent.entity.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 代理商业绩Repository
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Long> {
    /**
     * 根据代理商ID和统计周期查询
     */
    List<AgentPerformance> findByAgentIdAndStatPeriod(Long agentId, String statPeriod);

    /**
     * 根据代理商ID和时间范围查询
     */
    @Query("SELECT ap FROM AgentPerformance ap WHERE ap.agentId = :agentId AND ap.statDate >= :startTime AND ap.statDate <= :endTime")
    List<AgentPerformance> findByAgentIdAndTimeRange(@Param("agentId") Long agentId,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最新的业绩记录
     */
    Optional<AgentPerformance> findFirstByAgentIdOrderByStatDateDesc(Long agentId);
}















