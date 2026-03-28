/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedInterestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 杠杆利息记录Repository
 */
@Repository
public interface LeveragedInterestRecordRepository extends JpaRepository<LeveragedInterestRecord, Long> {
    /**
     * 根据用户ID查询利息记录
     */
    List<LeveragedInterestRecord> findByUserId(Long userId);

    /**
     * 根据账户ID查询利息记录
     */
    List<LeveragedInterestRecord> findByAccountId(Long accountId);

    /**
     * 根据用户ID和交易对查询利息记录
     */
    List<LeveragedInterestRecord> findByUserIdAndPairName(Long userId, String pairName);

    /**
     * 根据结算周期查询利息记录
     */
    List<LeveragedInterestRecord> findBySettlementCycle(String settlementCycle);

    /**
     * 根据状态查询利息记录
     */
    List<LeveragedInterestRecord> findByStatus(String status);

    /**
     * 根据结算时间范围查询利息记录
     */
    @Query("SELECT r FROM LeveragedInterestRecord r WHERE r.settlementTime BETWEEN :startTime AND :endTime")
    List<LeveragedInterestRecord> findBySettlementTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}













