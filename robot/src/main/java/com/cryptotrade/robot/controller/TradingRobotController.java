/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.robot.dto.request.TradingRobotRequest;
import com.cryptotrade.robot.dto.response.RobotTradeRecordResponse;
import com.cryptotrade.robot.entity.RobotTradeRecord;
import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.service.TradingRobotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/robot")
@Api(tags = "交易机器人模块")
public class TradingRobotController {

    @Autowired
    private TradingRobotService tradingRobotService;

    @PostMapping("/configure")
    @ApiOperation(value = "配置交易机器人", notes = "配置网格交易、趋势跟踪、反向等策略")
    public Result<TradingRobot> configureRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TradingRobotRequest request) {
        try {
            TradingRobot robot = tradingRobotService.configureRobot(userId, request);
            return Result.success("机器人配置成功", robot);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询机器人列表", notes = "查询用户的所有交易机器人")
    public Result<List<TradingRobot>> getUserRobots(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<TradingRobot> robots = tradingRobotService.getUserRobots(userId);
            return Result.success(robots);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{robotId}/status")
    @ApiOperation(value = "查询机器人状态", notes = "根据ID查询机器人状态")
    public Result<TradingRobot> getRobotStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            TradingRobot robot = tradingRobotService.getRobotStatus(userId, robotId);
            return Result.success(robot);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/start")
    @ApiOperation(value = "启动机器人", notes = "启动指定的交易机器人")
    public Result<Void> startRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            tradingRobotService.startRobot(userId, robotId);
            return Result.success("机器人启动成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/stop")
    @ApiOperation(value = "停止机器人", notes = "停止指定的交易机器人")
    public Result<Void> stopRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            tradingRobotService.stopRobot(userId, robotId);
            return Result.success("机器人停止成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{robotId}/history")
    @ApiOperation(value = "查询交易记录", notes = "查询交易机器人的历史交易记录")
    public Result<List<RobotTradeRecordResponse>> getRobotHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId,
            @ApiParam(value = "开始时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            List<RobotTradeRecord> records = tradingRobotService.getRobotHistory(userId, robotId, startTime, endTime);
            List<RobotTradeRecordResponse> responses = records.stream()
                    .map(this::convertToRecordResponse)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询所有交易记录", notes = "查询用户所有交易机器人的历史交易记录")
    public Result<List<RobotTradeRecordResponse>> getAllRobotHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "开始时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            List<RobotTradeRecord> records = tradingRobotService.getRobotHistory(userId, null, startTime, endTime);
            List<RobotTradeRecordResponse> responses = records.stream()
                    .map(this::convertToRecordResponse)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private RobotTradeRecordResponse convertToRecordResponse(RobotTradeRecord record) {
        RobotTradeRecordResponse response = new RobotTradeRecordResponse();
        response.setId(record.getId());
        response.setRobotId(record.getRobotId());
        response.setMarketType(record.getMarketType());
        response.setOrderId(record.getOrderId());
        response.setPairName(record.getPairName());
        response.setAction(record.getAction());
        response.setSide(record.getSide());
        response.setQuantity(record.getQuantity());
        response.setPrice(record.getPrice());
        response.setAmount(record.getAmount());
        response.setFee(record.getFee());
        response.setProfitLoss(record.getProfitLoss());
        response.setStrategyType(record.getStrategyType());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}

