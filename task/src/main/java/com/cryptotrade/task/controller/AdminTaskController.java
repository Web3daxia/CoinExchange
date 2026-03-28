/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.task.entity.Task;
import com.cryptotrade.task.entity.TaskReward;
import com.cryptotrade.task.repository.TaskRepository;
import com.cryptotrade.task.repository.TaskRewardRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台任务管理控制器
 */
@RestController
@RequestMapping("/admin/task")
@Api(tags = "后台任务管理")
public class AdminTaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRewardRepository taskRewardRepository;

    @PostMapping("/create")
    @ApiOperation(value = "创建任务", notes = "管理员创建新任务")
    public Result<Task> createTask(@RequestBody Task task) {
        try {
            task = taskRepository.save(task);
            return Result.success("任务创建成功", task);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "获取任务统计数据", notes = "查看任务完成情况、奖励发放情况等")
    public Result<Map<String, Object>> getTaskStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总任务数
            long totalTasks = taskRepository.count();
            statistics.put("totalTasks", totalTasks);
            
            // 总奖励金额
            List<TaskReward> allRewards = taskRewardRepository.findAll();
            BigDecimal totalReward = allRewards.stream()
                    .map(TaskReward::getRewardAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            statistics.put("totalReward", totalReward);
            
            // 已发放奖励金额
            BigDecimal distributedReward = allRewards.stream()
                    .filter(r -> "DISTRIBUTED".equals(r.getStatus()))
                    .map(TaskReward::getRewardAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            statistics.put("distributedReward", distributedReward);
            
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















