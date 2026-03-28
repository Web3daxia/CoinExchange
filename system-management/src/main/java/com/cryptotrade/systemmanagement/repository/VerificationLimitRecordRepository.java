/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.VerificationLimitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationLimitRecordRepository extends JpaRepository<VerificationLimitRecord, Long> {
    @Query("SELECT SUM(v.count) FROM VerificationLimitRecord v WHERE v.limitKey = :limitKey " +
           "AND v.limitType = :limitType AND v.verificationType = :verificationType " +
           "AND v.actionType = :actionType AND v.periodStartTime <= :now AND v.periodEndTime >= :now")
    Integer countByLimitKeyAndTypeAndTimeRange(
            @Param("limitKey") String limitKey,
            @Param("limitType") String limitType,
            @Param("verificationType") String verificationType,
            @Param("actionType") String actionType,
            @Param("now") LocalDateTime now);
    
    List<VerificationLimitRecord> findByLimitKeyAndLimitTypeAndVerificationTypeAndActionType(
            String limitKey, String limitType, String verificationType, String actionType);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationLimitRecord v WHERE v.periodEndTime < :expireTime")
    void deleteExpiredRecords(@Param("expireTime") LocalDateTime expireTime);
}

