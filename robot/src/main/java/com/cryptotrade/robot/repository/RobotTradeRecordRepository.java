/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.RobotTradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机器人交易记录Repository
 */
@Repository
public interface RobotTradeRecordRepository extends JpaRepository<RobotTradeRecord, Long> {
    /**
     * 根据机器人ID查询交易记录
     */
    List<RobotTradeRecord> findByRobotId(Long robotId);

    /**
     * 根据用户ID查询交易记录
     */
    List<RobotTradeRecord> findByUserId(Long userId);

    /**
     * 根据用户ID和机器人ID查询交易记录
     */
    List<RobotTradeRecord> findByUserIdAndRobotId(Long userId, Long robotId);

    /**
     * 根据创建时间范围查询交易记录
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.createdAt BETWEEN :startTime AND :endTime")
    List<RobotTradeRecord> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID和创建时间范围查询交易记录
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.userId = :userId AND r.createdAt BETWEEN :startTime AND :endTime")
    List<RobotTradeRecord> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID和创建时间范围查询交易记录（按创建时间倒序）
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.userId = :userId AND r.createdAt BETWEEN :startTime AND :endTime ORDER BY r.createdAt DESC")
    List<RobotTradeRecord> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID查询交易记录（按创建时间倒序）
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<RobotTradeRecord> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * 根据机器人ID和创建时间范围查询交易记录（按创建时间倒序）
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.robotId = :robotId AND r.createdAt BETWEEN :startTime AND :endTime ORDER BY r.createdAt DESC")
    List<RobotTradeRecord> findByRobotIdAndCreatedAtBetweenOrderByCreatedAtDesc(@Param("robotId") Long robotId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据机器人ID查询交易记录（按创建时间倒序）
     */
    @Query("SELECT r FROM RobotTradeRecord r WHERE r.robotId = :robotId ORDER BY r.createdAt DESC")
    List<RobotTradeRecord> findByRobotIdOrderByCreatedAtDesc(@Param("robotId") Long robotId);
}
