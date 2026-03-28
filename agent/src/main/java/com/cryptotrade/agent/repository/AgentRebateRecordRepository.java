/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.repository;

import com.cryptotrade.agent.entity.AgentRebateRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 代理商返佣记录Repository
 */
@Repository
public interface AgentRebateRecordRepository extends JpaRepository<AgentRebateRecord, Long> {
    /**
     * 根据代理商ID查询返佣记录
     */
    List<AgentRebateRecord> findByAgentId(Long agentId);

    /**
     * 根据代理商ID和状态查询
     */
    List<AgentRebateRecord> findByAgentIdAndStatus(Long agentId, String status);

    /**
     * 统计代理商的累计返佣金额
     */
    @Query("SELECT COALESCE(SUM(arr.rebateAmount), 0) FROM AgentRebateRecord arr WHERE arr.agentId = :agentId AND arr.status = 'SETTLED'")
    BigDecimal sumRebateAmountByAgentId(@Param("agentId") Long agentId);

    /**
     * 统计指定周期内的返佣金额
     */
    @Query("SELECT COALESCE(SUM(arr.rebateAmount), 0) FROM AgentRebateRecord arr WHERE arr.agentId = :agentId " +
           "AND arr.rebatePeriod = :period AND arr.periodStart >= :startTime AND arr.periodEnd <= :endTime AND arr.status = 'SETTLED'")
    BigDecimal sumRebateAmountByPeriod(@Param("agentId") Long agentId,
                                       @Param("period") String period,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);
}















