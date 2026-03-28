/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.leveraged.dto.request.AdjustLeverageRequest;
import com.cryptotrade.leveraged.dto.request.CreateLeveragedOrderRequest;
import com.cryptotrade.leveraged.dto.request.TopUpMarginRequest;
import com.cryptotrade.leveraged.dto.response.LeveragedMarketDataResponse;
import com.cryptotrade.leveraged.dto.response.LeveragedPositionResponse;
import com.cryptotrade.leveraged.entity.LeveragedAccount;
import com.cryptotrade.leveraged.entity.LeveragedOrder;
import com.cryptotrade.leveraged.entity.LeveragedPosition;
import com.cryptotrade.leveraged.entity.LeveragedRiskAlert;
import com.cryptotrade.leveraged.entity.LeveragedStrategy;
import com.cryptotrade.leveraged.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 杠杆交易控制器
 */
@RestController
@RequestMapping("/leveraged")
@Api(tags = "杠杆交易")
public class LeveragedTradingController {

    @Autowired
    private LeverageService leverageService;

    @Autowired
    private LeveragedOrderService leveragedOrderService;

    @Autowired
    private LeveragedPositionService leveragedPositionService;

    @Autowired
    private LeveragedStrategyService leveragedStrategyService;

    @Autowired
    private LeveragedRiskManagementService leveragedRiskManagementService;

    // ==================== 市场数据接口 ====================

    @GetMapping("/market/{pair}")
    @ApiOperation(value = "获取杠杆交易市场数据", notes = "获取指定交易对的杠杆交易市场数据")
    public Result<LeveragedMarketDataResponse> getMarketData(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pair) {
        try {
            // TODO: 从市场数据服务获取数据
            LeveragedMarketDataResponse response = new LeveragedMarketDataResponse();
            response.setPairName(pair);
            response.setAvailableLeverage(java.util.Arrays.asList(2, 5, 10, 20, 50, 100));
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 订单接口 ====================

    @PostMapping("/order")
    @ApiOperation(value = "创建杠杆订单", notes = "提交杠杆交易的开仓或平仓订单")
    public Result<LeveragedOrder> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateLeveragedOrderRequest request) {
        try {
            LeveragedOrder order;
            switch (request.getOrderType()) {
                case "MARKET":
                    order = leveragedOrderService.createMarketOrder(
                            userId, request.getPairName(), request.getSide(), request.getAction(),
                            request.getQuantity(), request.getLeverage());
                    break;
                case "LIMIT":
                    order = leveragedOrderService.createLimitOrder(
                            userId, request.getPairName(), request.getSide(), request.getAction(),
                            request.getQuantity(), request.getPrice(), request.getLeverage());
                    break;
                case "STOP_LOSS":
                    // 止损单需要从仓位创建
                    return Result.error("请使用 /leveraged/order/stop-loss 接口创建止损单");
                case "TAKE_PROFIT":
                    // 止盈单需要从仓位创建
                    return Result.error("请使用 /leveraged/order/take-profit 接口创建止盈单");
                case "STOP_LIMIT":
                    order = leveragedOrderService.createStopLimitOrder(
                            userId, request.getPairName(), request.getSide(), request.getAction(),
                            request.getQuantity(), request.getStopPrice(), request.getPrice(), request.getLeverage());
                    break;
                case "CONDITIONAL":
                    order = leveragedOrderService.createConditionalOrder(
                            userId, request.getPairName(), request.getSide(), request.getAction(),
                            request.getQuantity(), request.getTriggerPrice(), request.getConditionType(),
                            request.getPrice(), request.getLeverage());
                    break;
                default:
                    return Result.error("不支持的订单类型");
            }
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/history")
    @ApiOperation(value = "查询订单历史", notes = "查询用户的杠杆交易订单历史")
    public Result<List<LeveragedOrder>> getOrderHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<LeveragedOrder> orders = leveragedOrderService.getUserOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/market")
    @ApiOperation(value = "提交市价单", notes = "提交市价单")
    public Result<LeveragedOrder> createMarketOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateLeveragedOrderRequest request) {
        try {
            LeveragedOrder order = leveragedOrderService.createMarketOrder(
                    userId, request.getPairName(), request.getSide(), request.getAction(),
                    request.getQuantity(), request.getLeverage());
            return Result.success("市价单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/limit")
    @ApiOperation(value = "提交限价单", notes = "提交限价单")
    public Result<LeveragedOrder> createLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateLeveragedOrderRequest request) {
        try {
            LeveragedOrder order = leveragedOrderService.createLimitOrder(
                    userId, request.getPairName(), request.getSide(), request.getAction(),
                    request.getQuantity(), request.getPrice(), request.getLeverage());
            return Result.success("限价单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/stop-loss")
    @ApiOperation(value = "提交止损单", notes = "提交止损单")
    public Result<LeveragedOrder> createStopLossOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long positionId,
            @RequestParam BigDecimal stopPrice) {
        try {
            LeveragedOrder order = leveragedOrderService.createStopLossOrder(userId, positionId, stopPrice);
            return Result.success("止损单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/take-profit")
    @ApiOperation(value = "提交止盈单", notes = "提交止盈单")
    public Result<LeveragedOrder> createTakeProfitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long positionId,
            @RequestParam BigDecimal takeProfitPrice) {
        try {
            LeveragedOrder order = leveragedOrderService.createTakeProfitOrder(userId, positionId, takeProfitPrice);
            return Result.success("止盈单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/stop-limit")
    @ApiOperation(value = "提交止损限价单", notes = "提交止损限价单")
    public Result<LeveragedOrder> createStopLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateLeveragedOrderRequest request) {
        try {
            LeveragedOrder order = leveragedOrderService.createStopLimitOrder(
                    userId, request.getPairName(), request.getSide(), request.getAction(),
                    request.getQuantity(), request.getStopPrice(), request.getPrice(), request.getLeverage());
            return Result.success("止损限价单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/conditional")
    @ApiOperation(value = "提交条件单", notes = "提交条件单")
    public Result<LeveragedOrder> createConditionalOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateLeveragedOrderRequest request) {
        try {
            LeveragedOrder order = leveragedOrderService.createConditionalOrder(
                    userId, request.getPairName(), request.getSide(), request.getAction(),
                    request.getQuantity(), request.getTriggerPrice(), request.getConditionType(),
                    request.getPrice(), request.getLeverage());
            return Result.success("条件单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 杠杆设置接口 ====================

    @PostMapping("/account/leverage")
    @ApiOperation(value = "调整杠杆倍数", notes = "调整用户账户的杠杆倍数")
    public Result<LeveragedAccount> adjustLeverage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AdjustLeverageRequest request) {
        try {
            LeveragedAccount account = leverageService.adjustLeverage(
                    userId, request.getPairName(), request.getLeverage());
            return Result.success("杠杆倍数调整成功", account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/leverage")
    @ApiOperation(value = "查询杠杆倍数", notes = "查询用户当前杠杆倍数")
    public Result<LeveragedAccount> getLeverage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName) {
        try {
            LeveragedAccount account = leverageService.getLeverage(userId, pairName);
            return Result.success(account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 仓位管理接口 ====================

    @GetMapping("/account/positions")
    @ApiOperation(value = "查询仓位信息", notes = "查询用户当前仓位信息")
    public Result<List<LeveragedPositionResponse>> getPositions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<LeveragedPosition> positions = leveragedPositionService.getUserPositions(userId);
            List<LeveragedPositionResponse> responses = positions.stream()
                    .map(this::convertToPositionResponse)
                    .collect(Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/risk")
    @ApiOperation(value = "查询账户风险", notes = "查询账户风险信息（包括保证金、杠杆、可用资金等）")
    public Result<Map<String, Object>> getAccountRisk(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName) {
        try {
            Map<String, Object> risk = leveragedRiskManagementService.getAccountRisk(userId, pairName);
            return Result.success(risk);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/top-up")
    @ApiOperation(value = "补充保证金", notes = "补充保证金操作")
    public Result<LeveragedPositionResponse> topUpMargin(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TopUpMarginRequest request) {
        try {
            LeveragedPosition position = leveragedPositionService.topUpMargin(
                    userId, request.getPositionId(), request.getAmount());
            return Result.success("保证金补充成功", convertToPositionResponse(position));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/liquidate")
    @ApiOperation(value = "强制平仓", notes = "强制平仓操作")
    public Result<Void> liquidate(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long positionId) {
        try {
            boolean success = leveragedPositionService.liquidatePosition(userId, positionId);
            if (success) {
                return Result.success("强平成功", null);
            } else {
                return Result.error("强平失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 交易策略接口 ====================

    @PostMapping("/strategy/configure")
    @ApiOperation(value = "配置交易策略", notes = "配置杠杆交易策略")
    public Result<LeveragedStrategy> configureStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long accountId,
            @RequestParam(required = false) String strategyName,
            @RequestParam String strategyType,
            @RequestParam String pairName,
            @RequestParam(required = false) String strategyParams,
            @RequestParam Integer leverage) {
        try {
            LeveragedStrategy strategy = leveragedStrategyService.configureStrategy(
                    userId, accountId, strategyName, strategyType, pairName, strategyParams, leverage);
            return Result.success("策略配置成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/execute")
    @ApiOperation(value = "执行交易策略", notes = "执行杠杆交易策略")
    public Result<Void> executeStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long strategyId) {
        try {
            leveragedStrategyService.executeStrategy(strategyId);
            return Result.success("策略执行成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/robot/configure")
    @ApiOperation(value = "配置交易机器人", notes = "配置交易机器人")
    public Result<LeveragedStrategy> configureRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long accountId,
            @RequestParam(required = false) String strategyName,
            @RequestParam String strategyType,
            @RequestParam String pairName,
            @RequestParam(required = false) String strategyParams,
            @RequestParam Integer leverage) {
        try {
            LeveragedStrategy strategy = leveragedStrategyService.configureStrategy(
                    userId, accountId, strategyName, strategyType, pairName, strategyParams, leverage);
            return Result.success("交易机器人配置成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/robot/start")
    @ApiOperation(value = "启动交易机器人", notes = "启动交易机器人")
    public Result<Void> startRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long strategyId) {
        try {
            // 启动机器人就是执行策略
            leveragedStrategyService.executeStrategy(strategyId);
            return Result.success("交易机器人启动成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/robot/stop")
    @ApiOperation(value = "停止交易机器人", notes = "停止交易机器人")
    public Result<Void> stopRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long strategyId) {
        try {
            leveragedStrategyService.stopStrategy(userId, strategyId);
            return Result.success("交易机器人停止成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 风险管理接口 ====================

    @PostMapping("/account/risk-alert")
    @ApiOperation(value = "设置风险警报", notes = "设置风险警报")
    public Result<LeveragedRiskAlert> setRiskAlert(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long positionId,
            @RequestParam String alertType,
            @RequestParam(required = false) BigDecimal threshold,
            @RequestParam(required = false) BigDecimal thresholdPercentage,
            @RequestParam(required = false) String notificationMethod) {
        try {
            LeveragedRiskAlert alert = leveragedRiskManagementService.setRiskAlert(
                    userId, accountId, positionId, alertType, threshold, thresholdPercentage, notificationMethod);
            return Result.success("风险警报设置成功", alert);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private LeveragedPositionResponse convertToPositionResponse(LeveragedPosition position) {
        LeveragedPositionResponse response = new LeveragedPositionResponse();
        response.setId(position.getId());
        response.setPairName(position.getPairName());
        response.setSide(position.getSide());
        response.setQuantity(position.getQuantity());
        response.setEntryPrice(position.getEntryPrice());
        response.setCurrentPrice(position.getCurrentPrice());
        response.setLeverage(position.getLeverage());
        response.setMargin(position.getMargin());
        response.setUnrealizedPnl(position.getUnrealizedPnl());
        response.setRealizedPnl(position.getRealizedPnl());
        response.setLiquidationPrice(position.getLiquidationPrice());
        response.setMarginRatio(position.getMarginRatio());
        response.setStatus(position.getStatus());
        response.setCreatedAt(position.getCreatedAt());
        return response;
    }
}















