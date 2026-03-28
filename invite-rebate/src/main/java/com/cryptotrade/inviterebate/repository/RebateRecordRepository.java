/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.RebateRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 返佣记录Repository
 */
@Repository
public interface RebateRecordRepository extends JpaRepository<RebateRecord, Long> {
    /**
     * 根据邀请者ID查询返佣记录
     */
    List<RebateRecord> findByInviterId(Long inviterId);

    /**
     * 根据邀请者ID和状态查询
     */
    List<RebateRecord> findByInviterIdAndStatus(Long inviterId, String status);

    /**
     * 根据返佣周期和时间范围查询
     */
    @Query("SELECT rr FROM RebateRecord rr WHERE rr.inviterId = :inviterId AND rr.rebatePeriod = :period " +
           "AND rr.periodStart >= :startTime AND rr.periodEnd <= :endTime")
    List<RebateRecord> findByPeriodAndTimeRange(@Param("inviterId") Long inviterId,
                                                 @Param("period") String period,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 统计邀请者的累计返佣金额
     */
    @Query("SELECT COALESCE(SUM(rr.rebateAmount), 0) FROM RebateRecord rr WHERE rr.inviterId = :inviterId AND rr.status = 'SETTLED'")
    BigDecimal sumRebateAmountByInviterId(@Param("inviterId") Long inviterId);

    /**
     * 统计指定周期内的返佣金额
     */
    @Query("SELECT COALESCE(SUM(rr.rebateAmount), 0) FROM RebateRecord rr WHERE rr.inviterId = :inviterId " +
           "AND rr.rebatePeriod = :period AND rr.periodStart >= :startTime AND rr.periodEnd <= :endTime AND rr.status = 'SETTLED'")
    BigDecimal sumRebateAmountByPeriod(@Param("inviterId") Long inviterId,
                                       @Param("period") String period,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 查询待结算的返佣记录
     */
    @Query("SELECT rr FROM RebateRecord rr WHERE rr.status = 'PENDING' AND rr.periodEnd <= :currentTime")
    List<RebateRecord> findPendingSettlementRecords(@Param("currentTime") LocalDateTime currentTime);
}















