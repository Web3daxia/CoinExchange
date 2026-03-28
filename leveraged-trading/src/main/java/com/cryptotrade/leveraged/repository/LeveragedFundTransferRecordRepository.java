/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedFundTransferRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 杠杆资金转账记录Repository
 */
@Repository
public interface LeveragedFundTransferRecordRepository extends JpaRepository<LeveragedFundTransferRecord, Long> {
    /**
     * 根据转账ID查询
     */
    Optional<LeveragedFundTransferRecord> findByTransferId(String transferId);

    /**
     * 根据用户ID查询转账记录
     */
    List<LeveragedFundTransferRecord> findByUserId(Long userId);

    /**
     * 根据用户ID和交易对查询转账记录
     */
    List<LeveragedFundTransferRecord> findByUserIdAndPairName(Long userId, String pairName);

    /**
     * 根据转账类型查询
     */
    List<LeveragedFundTransferRecord> findByTransferType(String transferType);

    /**
     * 根据状态查询
     */
    List<LeveragedFundTransferRecord> findByStatus(String status);

    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT r FROM LeveragedFundTransferRecord r WHERE r.createdAt BETWEEN :startTime AND :endTime")
    List<LeveragedFundTransferRecord> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}













