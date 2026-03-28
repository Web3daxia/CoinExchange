/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.repository;

import com.cryptotrade.robot.entity.GridRobotBacktest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 网格机器人回测Repository
 */
@Repository
public interface GridRobotBacktestRepository extends JpaRepository<GridRobotBacktest, Long> {
    /**
     * 根据回测ID查询
     */
    Optional<GridRobotBacktest> findByBacktestId(String backtestId);

    /**
     * 根据用户ID查询回测记录
     */
    List<GridRobotBacktest> findByUserId(Long userId);

    /**
     * 根据机器人ID查询回测记录
     */
    List<GridRobotBacktest> findByRobotId(Long robotId);

    /**
     * 根据状态查询回测记录
     */
    List<GridRobotBacktest> findByStatus(String status);

    /**
     * 根据市场类型查询回测记录
     */
    List<GridRobotBacktest> findByMarketType(String marketType);

    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT b FROM GridRobotBacktest b WHERE b.createdAt BETWEEN :startTime AND :endTime")
    List<GridRobotBacktest> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}













