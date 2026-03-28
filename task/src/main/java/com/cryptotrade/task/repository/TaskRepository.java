/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.repository;

import com.cryptotrade.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务Repository
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * 查询启用的任务
     */
    List<Task> findByStatus(String status);

    /**
     * 查询指定时间范围内的任务
     */
    List<Task> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndStatus(
            LocalDateTime startTime, LocalDateTime endTime, String status);
}















