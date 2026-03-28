/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.coin.dto.request.CreateCoinFuturesOrderRequest;
import com.cryptotrade.futures.coin.dto.request.CoinFuturesStrategyRequest;
import com.cryptotrade.futures.coin.dto.request.CoinFuturesTradingRobotRequest;
import com.cryptotrade.futures.usdt.dto.request.CreateSegmentRequest;
import com.cryptotrade.futures.usdt.dto.request.SetMarginModeRequest;
import com.cryptotrade.futures.coin.dto.request.CoinFuturesStrategyRequest;
import com.cryptotrade.futures.coin.dto.request.CoinFuturesTradingRobotRequest;
import com.cryptotrade.common.dto.MarketDepthRequest;
import com.cryptotrade.common.dto.MarketDepthResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesMarketDataResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesOrderResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesPositionResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesAccountRiskResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesStrategyResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesTradingRobotResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesAdvancedOrder;
import com.cryptotrade.futures.coin.entity.CoinFuturesOrder;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.entity.CoinFuturesStrategy;
import com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot;
import com.cryptotrade.futures.coin.service.*;
import com.cryptotrade.futures.coin.service.CoinFuturesContractCalculatorService;
import com.cryptotrade.futures.coin.dto.request.CoinFuturesCalculatorRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesCalculatorResponse;
import com.cryptotrade.futures.usdt.entity.GradientRule;
import com.cryptotrade.futures.usdt.entity.MarginMode;
import com.cryptotrade.futures.usdt.entity.MarginSegment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/coin-futures")
@Api(tags = "币本位永续合约模块")
public class CoinFuturesController {

    @Autowired
    private CoinFuturesMarketDataService coinFuturesMarketDataService;

    @Autowired
    private CoinFuturesOrderService coinFuturesOrderService;

    @Autowired
    private CoinFuturesAdvancedOrderService coinFuturesAdvancedOrderService;

    @Autowired
    private CoinFuturesPositionService coinFuturesPositionService;

    @Autowired
    private CoinFuturesMarginModeService coinFuturesMarginModeService;

    @Autowired
    private CoinFuturesGradientService coinFuturesGradientService;

    @Autowired
    private CoinFuturesTradingRobotService coinFuturesTradingRobotService;

    @Autowired
    private CoinFuturesStrategyService coinFuturesStrategyService;

    @Autowired
    private CoinFuturesRiskManagementService coinFuturesRiskManagementService;
    
    @Autowired
    private CoinFuturesContractCalculatorService coinFuturesContractCalculatorService;

    // ==================== 市场数据接口 ====================

    @GetMapping("/market/{pair}")
    @ApiOperation(value = "获取市场数据", notes = "获取指定币本位合约的实时价格、标记价格、指数价格、资金费率等")
    public Result<CoinFuturesMarketDataResponse> getMarketData(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/BTC") @PathVariable String pair) {
        try {
            CoinFuturesMarketDataResponse marketData = coinFuturesMarketDataService.getMarketData(pair);
            return Result.success(marketData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/{pair}/chart")
    @ApiOperation(value = "获取K线数据", notes = "获取指定币本位合约的K线数据")
    public Result<List<Map<String, Object>>> getKlineData(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/BTC") @PathVariable String pair,
            @ApiParam(value = "时间粒度", example = "1h") @RequestParam(defaultValue = "1h") String interval,
            @ApiParam(value = "数据条数", example = "100") @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<Map<String, Object>> klineData = coinFuturesMarketDataService.getKlineData(pair, interval, limit);
            return Result.success(klineData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/{pair}/depth")
    @ApiOperation(value = "获取市场深度", notes = "获取指定币本位合约的买卖盘深度（支持买卖盘切换和价格精度）")
    public Result<MarketDepthResponse> getMarketDepth(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/BTC") @PathVariable String pair,
            @ApiParam(value = "深度条数", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "盘口类型：BUY（买盘）, SELL（卖盘）, BOTH（买卖盘，默认）", example = "BOTH") @RequestParam(defaultValue = "BOTH") String depthType,
            @ApiParam(value = "价格精度（可选，如0.01表示保留两位小数）", example = "0.01") @RequestParam(required = false) String pricePrecision) {
        try {
            MarketDepthRequest request = new MarketDepthRequest();
            request.setPair(pair);
            request.setLimit(limit);
            request.setDepthType(depthType);
            request.setPricePrecision(pricePrecision);
            MarketDepthResponse depth = coinFuturesMarketDataService.getMarketDepthEnhanced(request);
            return Result.success(depth);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 订单管理接口 ====================

    @PostMapping("/order")
    @ApiOperation(value = "提交订单", notes = "提交币本位永续合约订单（通用接口）")
    public Result<CoinFuturesOrderResponse> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateCoinFuturesOrderRequest request) {
        try {
            CoinFuturesOrder order = coinFuturesOrderService.createOrder(userId, request);
            CoinFuturesOrderResponse response = coinFuturesOrderService.getOrderStatus(userId, order.getId());
            return Result.success("订单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/cancel")
    @ApiOperation(value = "取消订单", notes = "取消待成交状态的订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            coinFuturesOrderService.cancelOrder(userId, orderId);
            return Result.success("订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/status")
    @ApiOperation(value = "查询订单状态", notes = "根据订单ID查询订单详情")
    public Result<CoinFuturesOrderResponse> getOrderStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            CoinFuturesOrderResponse order = coinFuturesOrderService.getOrderStatus(userId, orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/history")
    @ApiOperation(value = "查询订单历史", notes = "查询用户的订单历史记录")
    public Result<Page<CoinFuturesOrderResponse>> getOrderHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<CoinFuturesOrderResponse> orders = coinFuturesOrderService.getOrderHistory(userId, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 仓位管理接口 ====================

    @GetMapping("/account/positions")
    @ApiOperation(value = "查询仓位列表", notes = "查询用户的所有持仓仓位")
    public Result<List<CoinFuturesPositionResponse>> getPositions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<CoinFuturesPositionResponse> positions = coinFuturesPositionService.getPositions(userId);
            return Result.success(positions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/position/{positionId}")
    @ApiOperation(value = "查询单个仓位", notes = "根据仓位ID查询仓位详情")
    public Result<CoinFuturesPositionResponse> getPosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "仓位ID", required = true) @PathVariable Long positionId) {
        try {
            CoinFuturesPositionResponse position = coinFuturesPositionService.getPosition(userId, positionId);
            return Result.success(position);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 保证金模式接口 ====================

    @PostMapping("/account/margin")
    @ApiOperation(value = "设置保证金模式", notes = "设置用户的保证金模式（全仓/逐仓/分仓/组合）")
    public Result<MarginMode> setMarginMode(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SetMarginModeRequest request) {
        try {
            MarginMode marginMode = coinFuturesMarginModeService.setMarginMode(userId, request);
            return Result.success("保证金模式设置成功", marginMode);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/margin")
    @ApiOperation(value = "查询保证金模式", notes = "查询用户的保证金模式配置")
    public Result<MarginMode> getMarginMode(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            MarginMode marginMode = coinFuturesMarginModeService.getMarginMode(userId);
            return Result.success(marginMode);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/segment")
    @ApiOperation(value = "创建分仓", notes = "创建分仓（分仓模式使用）")
    public Result<MarginSegment> createSegment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateSegmentRequest request) {
        try {
            MarginSegment segment = coinFuturesMarginModeService.createSegment(userId, request);
            return Result.success("分仓创建成功", segment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/segments")
    @ApiOperation(value = "查询分仓列表", notes = "查询用户的所有分仓")
    public Result<List<MarginSegment>> getSegments(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<MarginSegment> segments = coinFuturesMarginModeService.getSegments(userId);
            return Result.success(segments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 梯度杠杆接口 ====================

    @GetMapping("/account/gradient")
    @ApiOperation(value = "查询梯度杠杆规则", notes = "查询指定交易对和仓位大小的梯度杠杆规则")
    public Result<GradientRule> getGradientRule(
            @ApiParam(value = "交易对名称", required = true) @RequestParam String pairName,
            @ApiParam(value = "仓位大小", required = true) @RequestParam java.math.BigDecimal positionQuantity) {
        try {
            Optional<GradientRule> ruleOpt = coinFuturesGradientService.getGradientRule(pairName, positionQuantity);
            if (ruleOpt.isPresent()) {
                return Result.success(ruleOpt.get());
            } else {
                return Result.error("未找到适用的梯度规则");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/gradient-adjust")
    @ApiOperation(value = "调整杠杆", notes = "调整仓位的杠杆倍数")
    public Result<CoinFuturesPositionResponse> adjustLeverage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "仓位ID", required = true) @RequestParam Long positionId,
            @ApiParam(value = "新杠杆倍数", required = true) @RequestParam Integer newLeverage) {
        try {
            CoinFuturesPosition position = coinFuturesGradientService.adjustLeverage(userId, positionId, newLeverage);
            CoinFuturesPositionResponse response = coinFuturesPositionService.getPosition(userId, positionId);
            return Result.success("杠杆调整成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 高级订单接口 ====================

    @PostMapping("/order/advanced")
    @ApiOperation(value = "创建高级订单", notes = "创建币本位永续合约高级订单")
    public Result<CoinFuturesAdvancedOrder> createAdvancedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateCoinFuturesAdvancedOrderRequest request) {
        try {
            CoinFuturesAdvancedOrder order = coinFuturesAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("高级订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/advanced/cancel")
    @ApiOperation(value = "取消高级订单", notes = "取消高级订单")
    public Result<Void> cancelAdvancedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            coinFuturesAdvancedOrderService.cancelAdvancedOrder(userId, orderId);
            return Result.success("高级订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/advanced/list")
    @ApiOperation(value = "查询高级订单列表", notes = "查询用户的高级订单列表")
    public Result<List<CoinFuturesAdvancedOrder>> getUserAdvancedOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<CoinFuturesAdvancedOrder> orders = coinFuturesAdvancedOrderService.getUserAdvancedOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 交易机器人接口 ====================

    @PostMapping("/robot/configure")
    @ApiOperation(value = "配置交易机器人策略", notes = "配置币本位永续合约交易机器人（网格、趋势、反转策略）")
    public Result<CoinFuturesTradingRobotResponse> configureRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CoinFuturesTradingRobotRequest request) {
        try {
            CoinFuturesTradingRobot robot = coinFuturesTradingRobotService.configureRobot(userId, request);
            CoinFuturesTradingRobotResponse response = convertToRobotResponse(robot);
            return Result.success("机器人配置成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/robot/start")
    @ApiOperation(value = "启动交易机器人", notes = "启动指定的币本位永续合约交易机器人")
    public Result<Void> startRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @RequestParam Long robotId) {
        try {
            coinFuturesTradingRobotService.startRobot(userId, robotId);
            return Result.success("机器人启动成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/robot/stop")
    @ApiOperation(value = "停止交易机器人", notes = "停止指定的币本位永续合约交易机器人")
    public Result<Void> stopRobot(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @RequestParam Long robotId) {
        try {
            coinFuturesTradingRobotService.stopRobot(userId, robotId);
            return Result.success("机器人停止成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/robot/status")
    @ApiOperation(value = "查询交易机器人状态", notes = "根据ID查询币本位永续合约交易机器人状态")
    public Result<CoinFuturesTradingRobotResponse> getRobotStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "机器人ID", required = true) @RequestParam Long robotId) {
        try {
            CoinFuturesTradingRobot robot = coinFuturesTradingRobotService.getRobotStatus(userId, robotId);
            CoinFuturesTradingRobotResponse response = convertToRobotResponse(robot);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/robot/list")
    @ApiOperation(value = "查询机器人列表", notes = "查询用户的所有币本位永续合约交易机器人")
    public Result<List<CoinFuturesTradingRobotResponse>> getRobotList(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<CoinFuturesTradingRobot> robots = coinFuturesTradingRobotService.getUserRobots(userId);
            List<CoinFuturesTradingRobotResponse> responses = robots.stream()
                    .map(this::convertToRobotResponse)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 合约策略接口 ====================

    @PostMapping("/strategy/configure")
    @ApiOperation(value = "配置合约策略", notes = "配置币本位永续合约策略（套利、对冲、跨期套利）")
    public Result<CoinFuturesStrategyResponse> configureStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CoinFuturesStrategyRequest request) {
        try {
            CoinFuturesStrategy strategy = coinFuturesStrategyService.configureStrategy(userId, request);
            CoinFuturesStrategyResponse response = convertToStrategyResponse(strategy);
            return Result.success("策略配置成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/execute")
    @ApiOperation(value = "执行合约策略", notes = "执行指定的币本位永续合约策略")
    public Result<Void> executeStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @RequestParam Long strategyId) {
        try {
            coinFuturesStrategyService.executeStrategy(userId, strategyId);
            return Result.success("策略执行成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/stop")
    @ApiOperation(value = "停止合约策略", notes = "停止指定的币本位永续合约策略")
    public Result<Void> stopStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @RequestParam Long strategyId) {
        try {
            coinFuturesStrategyService.stopStrategy(userId, strategyId);
            return Result.success("策略停止成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/strategy/list")
    @ApiOperation(value = "查询策略列表", notes = "查询用户的所有币本位永续合约策略")
    public Result<List<CoinFuturesStrategyResponse>> getStrategyList(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<CoinFuturesStrategy> strategies = coinFuturesStrategyService.getUserStrategies(userId);
            List<CoinFuturesStrategyResponse> responses = strategies.stream()
                    .map(this::convertToStrategyResponse)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/strategy/status")
    @ApiOperation(value = "查询策略状态", notes = "根据ID查询币本位永续合约策略状态")
    public Result<CoinFuturesStrategyResponse> getStrategyStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @RequestParam Long strategyId) {
        try {
            CoinFuturesStrategy strategy = coinFuturesStrategyService.getStrategyStatus(userId, strategyId);
            CoinFuturesStrategyResponse response = convertToStrategyResponse(strategy);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 风险管理接口 ====================

    @GetMapping("/account/risk")
    @ApiOperation(value = "查询账户风险", notes = "查询币本位永续合约账户的风险状况，包括保证金率、风险等级等")
    public Result<CoinFuturesAccountRiskResponse> getAccountRisk(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            CoinFuturesAccountRiskResponse response = coinFuturesRiskManagementService.getAccountRisk(userId);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 合约计算器接口 ====================

    @PostMapping("/calculator")
    @ApiOperation(value = "币本位合约计算器", notes = "计算币本位合约的盈亏、平仓价格、强平价格等")
    public Result<CoinFuturesCalculatorResponse> calculate(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CoinFuturesCalculatorRequest request) {
        try {
            CoinFuturesCalculatorResponse response = coinFuturesContractCalculatorService.calculate(request);
            return Result.success("计算成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private CoinFuturesTradingRobotResponse convertToRobotResponse(com.cryptotrade.futures.coin.entity.CoinFuturesTradingRobot robot) {
        CoinFuturesTradingRobotResponse response = new CoinFuturesTradingRobotResponse();
        response.setId(robot.getId());
        response.setUserId(robot.getUserId());
        response.setRobotName(robot.getRobotName());
        response.setPairName(robot.getPairName());
        response.setStrategyType(robot.getStrategyType());
        response.setStatus(robot.getStatus());
        response.setLeverage(robot.getLeverage());
        response.setMarginMode(robot.getMarginMode());
        response.setPositionSide(robot.getPositionSide());
        response.setTotalProfit(robot.getTotalProfit());
        response.setTotalLoss(robot.getTotalLoss());
        response.setLastExecutionTime(robot.getLastExecutionTime());
        response.setCreatedAt(robot.getCreatedAt());
        return response;
    }

    private CoinFuturesStrategyResponse convertToStrategyResponse(com.cryptotrade.futures.coin.entity.CoinFuturesStrategy strategy) {
        CoinFuturesStrategyResponse response = new CoinFuturesStrategyResponse();
        response.setId(strategy.getId());
        response.setUserId(strategy.getUserId());
        response.setStrategyName(strategy.getStrategyName());
        response.setStrategyType(strategy.getStrategyType());
        response.setStatus(strategy.getStatus());
        response.setTotalProfit(strategy.getTotalProfit());
        response.setTotalLoss(strategy.getTotalLoss());
        response.setLastExecutionTime(strategy.getLastExecutionTime());
        response.setCreatedAt(strategy.getCreatedAt());
        return response;
    }
}

