/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.service;

import com.cryptotrade.task.entity.Task;
import com.cryptotrade.task.entity.TaskProgress;
import com.cryptotrade.task.entity.TaskReward;

import java.util.List;
import java.util.Map;

/**
 * 任务服务接口
 */
public interface TaskService {
    /**
     * 获取任务列表
     */
    List<Task> getTaskList(String taskType, String status);

    /**
     * 获取用户任务状态
     */
    Map<String, Object> getUserTaskStatus(Long userId);

    /**
     * 完成任务
     */
    TaskProgress completeTask(Long userId, Long taskId, Map<String, Object> completionData);

    /**
     * 领取奖励
     */
    TaskReward claimReward(Long userId, Long rewardId);

    /**
     * 检查并自动完成任务
     */
    void checkAndCompleteTasks(Long userId, String actionType, Map<String, Object> actionData);
}















