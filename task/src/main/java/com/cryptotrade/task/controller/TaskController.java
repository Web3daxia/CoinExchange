/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.task.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.task.entity.Task;
import com.cryptotrade.task.entity.TaskProgress;
import com.cryptotrade.task.entity.TaskReward;
import com.cryptotrade.task.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/task")
@Api(tags = "任务模块")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/list")
    @ApiOperation(value = "获取任务列表", notes = "获取所有任务列表，支持类型和状态筛选")
    public Result<List<Task>> getTaskList(
            @ApiParam(value = "任务类型", example = "INVITE") @RequestParam(required = false) String taskType,
            @ApiParam(value = "状态", example = "ACTIVE") @RequestParam(required = false) String status) {
        try {
            List<Task> tasks = taskService.getTaskList(taskType, status);
            return Result.success(tasks);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取用户任务状态", notes = "获取用户当前完成的任务状态和奖励发放情况")
    public Result<Map<String, Object>> getUserTaskStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, Object> status = taskService.getUserTaskStatus(userId);
            return Result.success(status);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/complete")
    @ApiOperation(value = "提交任务完成", notes = "用户提交任务完成情况，获取奖励")
    public Result<TaskProgress> completeTask(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "任务ID", required = true) @RequestParam Long taskId,
            @RequestBody Map<String, Object> completionData) {
        try {
            TaskProgress progress = taskService.completeTask(userId, taskId, completionData);
            return Result.success("任务完成成功", progress);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/claim-reward")
    @ApiOperation(value = "领取任务奖励", notes = "用户领取任务奖励")
    public Result<TaskReward> claimReward(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "奖励ID", required = true) @RequestParam Long rewardId) {
        try {
            TaskReward reward = taskService.claimReward(userId, rewardId);
            return Result.success("奖励领取成功", reward);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















