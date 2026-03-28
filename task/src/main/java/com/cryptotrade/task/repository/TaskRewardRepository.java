/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.repository;

import com.cryptotrade.task.entity.TaskReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务奖励Repository
 */
@Repository
public interface TaskRewardRepository extends JpaRepository<TaskReward, Long> {
    /**
     * 根据用户ID查询奖励记录
     */
    List<TaskReward> findByUserId(Long userId);

    /**
     * 根据状态查询奖励记录
     */
    List<TaskReward> findByUserIdAndStatus(Long userId, String status);

    /**
     * 统计用户累计奖励金额
     */
    @Query("SELECT COALESCE(SUM(tr.rewardAmount), 0) FROM TaskReward tr WHERE tr.userId = :userId AND tr.status = 'DISTRIBUTED'")
    BigDecimal sumRewardAmountByUserId(@Param("userId") Long userId);
}















