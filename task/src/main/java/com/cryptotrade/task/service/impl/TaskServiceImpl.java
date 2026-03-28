/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.service.impl;

import com.cryptotrade.task.entity.Task;
import com.cryptotrade.task.entity.TaskProgress;
import com.cryptotrade.task.entity.TaskReward;
import com.cryptotrade.task.repository.TaskProgressRepository;
import com.cryptotrade.task.repository.TaskRepository;
import com.cryptotrade.task.repository.TaskRewardRepository;
import com.cryptotrade.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务服务实现
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskProgressRepository taskProgressRepository;

    @Autowired
    private TaskRewardRepository taskRewardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Task> getTaskList(String taskType, String status) {
        List<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }

        if (taskType != null) {
            tasks = tasks.stream()
                    .filter(t -> taskType.equals(t.getTaskType()))
                    .collect(Collectors.toList());
        }

        // 过滤时间范围内的任务
        LocalDateTime now = LocalDateTime.now();
        return tasks.stream()
                .filter(t -> {
                    if (t.getStartTime() != null && t.getStartTime().isAfter(now)) {
                        return false;
                    }
                    if (t.getEndTime() != null && t.getEndTime().isBefore(now)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getUserTaskStatus(Long userId) {
        Map<String, Object> status = new HashMap<>();

        // 所有任务进度
        List<TaskProgress> progresses = taskProgressRepository.findByUserId(userId);
        status.put("progresses", progresses);

        // 已完成任务数
        Long completedCount = taskProgressRepository.countCompletedTasksByUserId(userId);
        status.put("completedCount", completedCount);

        // 累计奖励金额
        BigDecimal totalReward = taskRewardRepository.sumRewardAmountByUserId(userId);
        status.put("totalReward", totalReward);

        // 待领取奖励
        List<TaskReward> pendingRewards = taskRewardRepository.findByUserIdAndStatus(userId, "PENDING");
        status.put("pendingRewards", pendingRewards);

        return status;
    }

    @Override
    @Transactional
    public TaskProgress completeTask(Long userId, Long taskId, Map<String, Object> completionData) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        if (!"ACTIVE".equals(task.getStatus())) {
            throw new RuntimeException("任务未启用");
        }

        // 检查时间范围
        LocalDateTime now = LocalDateTime.now();
        if (task.getStartTime() != null && task.getStartTime().isAfter(now)) {
            throw new RuntimeException("任务尚未开始");
        }
        if (task.getEndTime() != null && task.getEndTime().isBefore(now)) {
            throw new RuntimeException("任务已结束");
        }

        // 检查是否已完成
        Optional<TaskProgress> existingOpt = taskProgressRepository.findByUserIdAndTaskId(userId, taskId);
        if (existingOpt.isPresent() && "REWARDED".equals(existingOpt.get().getStatus())) {
            throw new RuntimeException("任务已完成");
        }

        // 检查限制
        if (!checkLimits(userId, task)) {
            throw new RuntimeException("已达到任务完成限制");
        }

        // 创建或更新进度
        TaskProgress progress = existingOpt.orElse(new TaskProgress());
        progress.setUserId(userId);
        progress.setTaskId(taskId);
        try {
            progress.setProgressData(objectMapper.writeValueAsString(completionData));
        } catch (Exception e) {
            throw new RuntimeException("进度数据格式错误");
        }
        progress.setCompletionRate(new BigDecimal("100"));
        progress.setStatus("COMPLETED");
        progress.setCompletedAt(now);
        if (progress.getCreatedAt() == null) {
            progress.setCreatedAt(now);
        }
        progress.setUpdatedAt(now);

        progress = taskProgressRepository.save(progress);

        // 创建奖励记录
        if ("INSTANT".equals(task.getRewardMethod())) {
            createReward(userId, taskId, progress.getId(), task);
        } else {
            createReward(userId, taskId, progress.getId(), task);
            progress.setStatus("REWARDED");
            taskProgressRepository.save(progress);
        }

        return progress;
    }

    @Override
    @Transactional
    public TaskReward claimReward(Long userId, Long rewardId) {
        TaskReward reward = taskRewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("奖励不存在"));

        if (!reward.getUserId().equals(userId)) {
            throw new RuntimeException("无权领取该奖励");
        }

        if (!"PENDING".equals(reward.getStatus())) {
            throw new RuntimeException("奖励状态异常");
        }

        reward.setStatus("CLAIMED");
        reward.setClaimedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());

        // 发放奖励（调用钱包服务）
        // walletService.addBalance(userId, reward.getRewardCurrency(), reward.getRewardAmount());

        reward.setStatus("DISTRIBUTED");
        reward.setDistributedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());

        return taskRewardRepository.save(reward);
    }

    @Override
    @Transactional
    public void checkAndCompleteTasks(Long userId, String actionType, Map<String, Object> actionData) {
        // 根据操作类型查找相关任务
        List<Task> tasks = taskRepository.findByStatus("ACTIVE");
        
        for (Task task : tasks) {
            try {
                Map<String, Object> conditions = objectMapper.readValue(task.getTaskConditions(), Map.class);
                String requiredAction = (String) conditions.get("actionType");
                
                if (actionType.equals(requiredAction)) {
                    // 检查条件是否满足
                    if (checkTaskConditions(task, actionData)) {
                        completeTask(userId, task.getId(), actionData);
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
    }

    // ==================== 私有方法 ====================

    private boolean checkLimits(Long userId, Task task) {
        LocalDateTime now = LocalDateTime.now();
        
        // 检查每日限制
        if (task.getDailyLimit() != null) {
            LocalDateTime todayStart = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
            long todayCount = taskRewardRepository.findByUserId(userId).stream()
                    .filter(r -> r.getCreatedAt().isAfter(todayStart))
                    .count();
            if (todayCount >= task.getDailyLimit()) {
                return false;
            }
        }

        // 检查每周限制
        if (task.getWeeklyLimit() != null) {
            LocalDateTime weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            long weekCount = taskRewardRepository.findByUserId(userId).stream()
                    .filter(r -> r.getCreatedAt().isAfter(weekStart))
                    .count();
            if (weekCount >= task.getWeeklyLimit()) {
                return false;
            }
        }

        // 检查每月限制
        if (task.getMonthlyLimit() != null) {
            LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            long monthCount = taskRewardRepository.findByUserId(userId).stream()
                    .filter(r -> r.getCreatedAt().isAfter(monthStart))
                    .count();
            if (monthCount >= task.getMonthlyLimit()) {
                return false;
            }
        }

        // 检查最大奖励次数
        if (task.getMaxRewardCount() != null) {
            long totalCount = taskRewardRepository.findByUserId(userId).stream()
                    .filter(r -> r.getTaskId().equals(task.getId()))
                    .count();
            if (totalCount >= task.getMaxRewardCount()) {
                return false;
            }
        }

        return true;
    }

    private void createReward(Long userId, Long taskId, Long progressId, Task task) {
        TaskReward reward = new TaskReward();
        reward.setRewardNo(generateRewardNo());
        reward.setUserId(userId);
        reward.setTaskId(taskId);
        reward.setProgressId(progressId);
        reward.setRewardCurrency(task.getRewardCurrency());
        reward.setRewardAmount(task.getRewardAmount());
        reward.setStatus("PENDING");
        reward.setCreatedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());
        taskRewardRepository.save(reward);
    }

    private boolean checkTaskConditions(Task task, Map<String, Object> actionData) {
        // 根据任务类型检查条件
        // 这里简化处理，实际应该根据taskConditions JSON进行详细检查
        return true;
    }

    private String generateRewardNo() {
        return "TR" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}















