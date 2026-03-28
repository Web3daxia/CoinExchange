/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.SystemLogSearchRequest;
import com.cryptotrade.systemmanagement.entity.SystemLog;
import com.cryptotrade.systemmanagement.service.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志管理Controller
 */
@RestController
@RequestMapping("/admin/system/log")
@Api(tags = "系统日志管理")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @PostMapping("/search")
    @ApiOperation(value = "搜索系统日志", notes = "根据条件搜索系统日志")
    public Result<Page<SystemLog>> searchLogs(
            @ApiParam(value = "搜索条件", required = true) @RequestBody SystemLogSearchRequest request) {
        try {
            Page<SystemLog> logs = systemLogService.searchLogs(request);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取日志详情", notes = "根据ID获取日志详情")
    public Result<SystemLog> getLogById(
            @ApiParam(value = "日志ID", required = true) @PathVariable Long id) {
        try {
            SystemLog log = systemLogService.getLogById(id);
            return Result.success(log);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "根据用户ID获取日志", notes = "根据用户ID获取操作日志列表")
    public Result<Page<SystemLog>> getLogsByUserId(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<SystemLog> logs = systemLogService.getLogsByUserId(userId, page, size);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/module/{module}")
    @ApiOperation(value = "根据模块获取日志", notes = "根据操作模块获取日志列表")
    public Result<Page<SystemLog>> getLogsByModule(
            @ApiParam(value = "模块名称", required = true) @PathVariable String module,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<SystemLog> logs = systemLogService.getLogsByModule(module, page, size);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/cleanup/{daysToKeep}")
    @ApiOperation(value = "清理旧日志", notes = "删除指定天数之前的旧日志")
    public Result<Void> cleanupOldLogs(
            @ApiParam(value = "保留天数", required = true) @PathVariable int daysToKeep) {
        try {
            systemLogService.deleteOldLogs(daysToKeep);
            return Result.success("旧日志清理成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














