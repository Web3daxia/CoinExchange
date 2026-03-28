/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.RebateSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 返佣结算Repository
 */
@Repository
public interface RebateSettlementRepository extends JpaRepository<RebateSettlement, Long> {
    /**
     * 根据邀请者ID查询结算记录
     */
    List<RebateSettlement> findByInviterId(Long inviterId);

    /**
     * 根据状态查询结算记录
     */
    List<RebateSettlement> findByStatus(String status);

    /**
     * 统计邀请者的累计结算金额
     */
    @Query("SELECT COALESCE(SUM(rs.totalRebateAmount), 0) FROM RebateSettlement rs WHERE rs.inviterId = :inviterId AND rs.status = 'SETTLED'")
    BigDecimal sumSettledAmountByInviterId(@Param("inviterId") Long inviterId);
}















