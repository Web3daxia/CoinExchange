/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.robot.dto.request.CreateFuturesGridRobotRequest;
import com.cryptotrade.robot.dto.request.CreateSpotGridRobotRequest;
import com.cryptotrade.robot.dto.request.CreateBacktestRequest;
import com.cryptotrade.robot.dto.request.UpdateGridRobotRequest;
import com.cryptotrade.robot.dto.request.AutoAdjustRequest;
import com.cryptotrade.robot.dto.response.GridRobotResponse;
import com.cryptotrade.robot.entity.GridRobotAdjustmentHistory;
import com.cryptotrade.robot.entity.GridRobotBacktest;
import com.cryptotrade.robot.entity.GridRobotDefaultStrategy;
import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.service.GridRobotService;
import com.cryptotrade.robot.service.GridRobotBacktestService;
import com.cryptotrade.robot.service.GridRobotDefaultStrategyService;
import com.cryptotrade.robot.service.GridRobotAdjustmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网格机器人控制器
 */
@RestController
@RequestMapping("/robot/grid")
@Api(tags = "网格交易机器人模块")
public class GridRobotController {

    @Autowired
    private GridRobotService gridRobotService;

    @Autowired
    private GridRobotBacktestService gridRobotBacktestService;

    @Autowired
    private GridRobotDefaultStrategyService gridRobotDefaultStrategyService;

    @Autowired
    private GridRobotAdjustmentService gridRobotAdjustmentService;

    @Autowired
    private com.cryptotrade.robot.service.GridRobotSettlementService gridRobotSettlementService;

    @Autowired
    private com.cryptotrade.robot.repository.TradingRobotRepository tradingRobotRepository;

    @PostMapping("/spot/create")
    @ApiOperation(value = "创建现货网格机器人", notes = "创建现货市场的网格交易机器人")
    public Result<GridRobotResponse> createSpotGridRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateSpotGridRobotRequest request) {
        try {
            TradingRobot robot = gridRobotService.createSpotGridRobot(
                    userId, request.getRobotName(), request.getPairName(),
                    request.getGridCount(), request.getUpperPrice(), request.getLowerPrice(),
                    request.getStartPrice(), request.getInitialCapital(), request.getInvestmentRatio(),
                    request.getStopLossPrice(), request.getTakeProfitPrice(),
                    request.getStopLossPercentage(), request.getTakeProfitPercentage());
            
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("网格机器人创建成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/futures-usdt/create")
    @ApiOperation(value = "创建USDT本位合约网格机器人", notes = "创建USDT本位合约市场的网格交易机器人")
    public Result<GridRobotResponse> createFuturesUsdtGridRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesGridRobotRequest request) {
        try {
            TradingRobot robot = gridRobotService.createFuturesUsdtGridRobot(
                    userId, request.getRobotName(), request.getPairName(),
                    request.getGridCount(), request.getUpperPrice(), request.getLowerPrice(),
                    request.getStartPrice(), request.getInitialCapital(), request.getInvestmentRatio(),
                    request.getLeverage(), request.getMarginMode(),
                    request.getStopLossPrice(), request.getTakeProfitPrice(),
                    request.getStopLossPercentage(), request.getTakeProfitPercentage());
            
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("合约网格机器人创建成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/futures-coin/create")
    @ApiOperation(value = "创建币本位合约网格机器人", notes = "创建币本位合约市场的网格交易机器人")
    public Result<GridRobotResponse> createFuturesCoinGridRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesGridRobotRequest request) {
        try {
            TradingRobot robot = gridRobotService.createFuturesCoinGridRobot(
                    userId, request.getRobotName(), request.getPairName(),
                    request.getGridCount(), request.getUpperPrice(), request.getLowerPrice(),
                    request.getStartPrice(), request.getInitialCapital(), request.getInvestmentRatio(),
                    request.getLeverage(), request.getMarginMode(),
                    request.getStopLossPrice(), request.getTakeProfitPrice(),
                    request.getStopLossPercentage(), request.getTakeProfitPercentage());
            
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("币本位合约网格机器人创建成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/default-strategies")
    @ApiOperation(value = "查询默认策略列表", notes = "查询平台提供的默认网格策略列表")
    public Result<List<GridRobotDefaultStrategy>> getDefaultStrategies(
            @ApiParam(value = "市场类型筛选: SPOT, FUTURES_USDT, FUTURES_COIN") @RequestParam(required = false) String marketType) {
        try {
            List<GridRobotDefaultStrategy> strategies = gridRobotDefaultStrategyService.getDefaultStrategies(marketType);
            return Result.success(strategies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/start")
    @ApiOperation(value = "启动网格机器人", notes = "启动指定的网格机器人")
    public Result<GridRobotResponse> startRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            TradingRobot robot = gridRobotService.startRobot(userId, robotId);
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("网格机器人启动成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/pause")
    @ApiOperation(value = "暂停网格机器人", notes = "暂停指定的网格机器人")
    public Result<GridRobotResponse> pauseRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            TradingRobot robot = gridRobotService.pauseRobot(userId, robotId);
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("网格机器人暂停成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/resume")
    @ApiOperation(value = "恢复网格机器人", notes = "恢复已暂停的网格机器人")
    public Result<GridRobotResponse> resumeRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            TradingRobot robot = gridRobotService.resumeRobot(userId, robotId);
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("网格机器人恢复成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/stop")
    @ApiOperation(value = "停止网格机器人", notes = "停止网格机器人，停止时会取消所有未成交订单，并平掉所有持仓")
    public Result<GridRobotResponse> stopRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId,
            @ApiParam(value = "是否平掉所有持仓") @RequestParam(required = false, defaultValue = "true") Boolean closePositions,
            @ApiParam(value = "是否取消所有未成交订单") @RequestParam(required = false, defaultValue = "true") Boolean cancelOrders) {
        try {
            TradingRobot robot = gridRobotService.stopRobot(userId, robotId, closePositions, cancelOrders);
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("网格机器人停止成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询机器人列表", notes = "查询用户的所有网格机器人列表")
    public Result<List<GridRobotResponse>> getRobotList(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "市场类型筛选") @RequestParam(required = false) String marketType,
            @ApiParam(value = "状态筛选") @RequestParam(required = false) String status,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<com.cryptotrade.robot.entity.TradingRobot> robots = tradingRobotRepository.findByUserId(userId);
            
            // 应用筛选条件
            if (marketType != null) {
                robots = robots.stream().filter(r -> marketType.equals(r.getMarketType())).collect(Collectors.toList());
            }
            if (status != null) {
                robots = robots.stream().filter(r -> status.equals(r.getStatus())).collect(Collectors.toList());
            }
            
            List<GridRobotResponse> responses = robots.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{robotId}")
    @ApiOperation(value = "查询机器人详情", notes = "查询指定网格机器人的详细信息")
    public Result<GridRobotResponse> getRobotDetail(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            java.util.Optional<com.cryptotrade.robot.entity.TradingRobot> robotOpt = tradingRobotRepository.findById(robotId);
            com.cryptotrade.robot.entity.TradingRobot robot = robotOpt.orElseThrow(() -> new RuntimeException("机器人不存在"));
            
            if (!robot.getUserId().equals(userId)) {
                throw new RuntimeException("无权访问该机器人");
            }
            
            GridRobotResponse response = convertToResponse(robot);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{robotId}/settings")
    @ApiOperation(value = "更新网格机器人参数", notes = "更新网格机器人的参数（仅停止或暂停状态可更新）")
    public Result<GridRobotResponse> updateRobotSettings(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId,
            @Valid @RequestBody UpdateGridRobotRequest request) {
        try {
            com.cryptotrade.robot.entity.TradingRobot robot = gridRobotService.updateRobotSettings(
                    userId, robotId, request.getRobotName(), request.getGridCount(),
                    request.getUpperPrice(), request.getLowerPrice(), request.getStartPrice(),
                    request.getInitialCapital(), request.getInvestmentRatio(),
                    request.getStopLossPrice(), request.getTakeProfitPrice());
            
            GridRobotResponse response = convertToResponse(robot);
            return Result.success("参数更新成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/auto-adjust")
    @ApiOperation(value = "智能调整网格参数", notes = "根据市场波动情况，智能调整网格参数")
    public Result<GridRobotAdjustmentHistory> autoAdjustRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId,
            @Valid @RequestBody AutoAdjustRequest request) {
        try {
            GridRobotAdjustmentHistory adjustment = gridRobotService.autoAdjustRobot(
                    userId, robotId, request.getAdjustType(),
                    request.getVolatilityPeriod(), request.getTargetGridCount(),
                    request.getAdjustPriceRange());
            return Result.success("智能调整成功", adjustment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{robotId}/settle")
    @ApiOperation(value = "结算网格机器人", notes = "结算已停止的网格机器人，将盈亏转入用户账户")
    public Result<com.cryptotrade.robot.entity.GridRobotSettlement> settleRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            com.cryptotrade.robot.entity.GridRobotSettlement settlement = gridRobotSettlementService.settleRobot(userId, robotId);
            return Result.success("结算成功", settlement);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{robotId}")
    @ApiOperation(value = "删除网格机器人", notes = "删除指定的网格机器人")
    public Result<Void> deleteRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId) {
        try {
            gridRobotService.deleteRobot(userId, robotId);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{robotId}/adjustment-history")
    @ApiOperation(value = "查询参数调整历史", notes = "查询网格机器人的参数调整历史记录")
    public Result<List<GridRobotAdjustmentHistory>> getAdjustmentHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @PathVariable Long robotId,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<GridRobotAdjustmentHistory> history = gridRobotAdjustmentService.getAdjustmentHistory(robotId);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/backtest/create")
    @ApiOperation(value = "创建回测任务", notes = "创建网格策略回测任务")
    public Result<GridRobotBacktest> createBacktest(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateBacktestRequest request) {
        try {
            com.cryptotrade.robot.entity.GridRobotBacktest backtest = new com.cryptotrade.robot.entity.GridRobotBacktest();
            backtest.setUserId(userId);
            backtest.setRobotId(request.getRobotId());
            backtest.setPairName(request.getPairName());
            backtest.setMarketType(request.getMarketType());
            backtest.setGridCount(request.getGridCount());
            backtest.setInitialCapital(request.getInitialCapital());
            
            // 解析时间
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            backtest.setStartTime(java.time.LocalDateTime.parse(request.getStartTime(), formatter));
            backtest.setEndTime(java.time.LocalDateTime.parse(request.getEndTime(), formatter));
            
            GridRobotBacktest result = gridRobotBacktestService.createBacktest(userId, request.getRobotId(), backtest);
            return Result.success("回测任务创建成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/backtest/{backtestId}/status")
    @ApiOperation(value = "查询回测任务状态", notes = "查询回测任务的执行状态")
    public Result<GridRobotBacktest> getBacktestStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "回测任务ID", required = true) @PathVariable String backtestId) {
        try {
            GridRobotBacktest backtest = gridRobotBacktestService.getBacktestStatus(backtestId);
            return Result.success(backtest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/backtest/{backtestId}/result")
    @ApiOperation(value = "查询回测结果", notes = "查询回测任务的详细结果")
    public Result<GridRobotBacktest> getBacktestResult(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "回测任务ID", required = true) @PathVariable String backtestId) {
        try {
            GridRobotBacktest backtest = gridRobotBacktestService.getBacktestResult(backtestId);
            return Result.success(backtest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    private GridRobotResponse convertToResponse(TradingRobot robot) {
        GridRobotResponse response = new GridRobotResponse();
        BeanUtils.copyProperties(robot, response);
        response.setNetProfit(robot.getTotalProfit().subtract(robot.getTotalLoss()));
        return response;
    }
}

