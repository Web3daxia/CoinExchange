/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.repository;

import com.cryptotrade.task.entity.TaskProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 任务进度Repository
 */
@Repository
public interface TaskProgressRepository extends JpaRepository<TaskProgress, Long> {
    /**
     * 根据用户ID和任务ID查询进度
     */
    Optional<TaskProgress> findByUserIdAndTaskId(Long userId, Long taskId);

    /**
     * 根据用户ID查询所有进度
     */
    List<TaskProgress> findByUserId(Long userId);

    /**
     * 根据状态查询进度
     */
    List<TaskProgress> findByUserIdAndStatus(Long userId, String status);

    /**
     * 统计用户完成的任务数
     */
    @Query("SELECT COUNT(tp) FROM TaskProgress tp WHERE tp.userId = :userId AND tp.status = 'REWARDED'")
    Long countCompletedTasksByUserId(@Param("userId") Long userId);
}















