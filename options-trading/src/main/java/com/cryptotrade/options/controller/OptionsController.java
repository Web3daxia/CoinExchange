/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.options.dto.request.CreateOptionOrderRequest;
import com.cryptotrade.options.dto.request.ExerciseOptionRequest;
import com.cryptotrade.options.dto.response.OptionMarketDataResponse;
import com.cryptotrade.options.dto.response.OptionPricingResponse;
import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.entity.OptionExercise;
import com.cryptotrade.options.entity.OptionOrder;
import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.entity.OptionStrategy;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 期权交易控制器
 */
@RestController
@RequestMapping("/options")
@Api(tags = "期权合约交易")
public class OptionsController {

    @Autowired
    private OptionContractRepository optionContractRepository;

    @Autowired
    private OptionOrderService optionOrderService;

    @Autowired
    private OptionExerciseService optionExerciseService;

    @Autowired
    private OptionPricingService optionPricingService;

    @Autowired
    private OptionStrategyService optionStrategyService;

    @Autowired
    private OptionRiskManagementService optionRiskManagementService;

    // ==================== 市场数据接口 ====================

    @GetMapping("/market/{pair}")
    @ApiOperation(value = "获取期权市场数据", notes = "获取指定交易对的期权合约市场数据")
    public Result<OptionMarketDataResponse> getMarketData(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pair) {
        try {
            // TODO: 实现获取市场数据逻辑
            OptionMarketDataResponse response = new OptionMarketDataResponse();
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 订单接口 ====================

    @PostMapping("/order")
    @ApiOperation(value = "创建期权订单", notes = "提交开仓或平仓订单")
    public Result<OptionOrder> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOptionOrderRequest request) {
        try {
            OptionOrder order = optionOrderService.createOrder(
                    userId, request.getContractId(), request.getOrderType(),
                    request.getSide(), request.getQuantity(), request.getPrice());
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/history")
    @ApiOperation(value = "查询订单历史", notes = "查询用户的期权订单历史")
    public Result<List<OptionOrder>> getOrderHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<OptionOrder> orders = optionOrderService.getUserOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 期权类型接口 ====================

    @GetMapping("/type")
    @ApiOperation(value = "查询期权类型", notes = "查询期权类型（美式、欧式）")
    public Result<Map<String, Object>> getOptionTypes() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("exerciseTypes", Arrays.asList("AMERICAN", "EUROPEAN"));
            result.put("optionTypes", Arrays.asList("CALL", "PUT"));
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/call")
    @ApiOperation(value = "提交看涨期权订单", notes = "提交看涨期权开仓订单")
    public Result<OptionOrder> createCallOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOptionOrderRequest request) {
        try {
            // 验证期权类型为CALL
            OptionContract contract = optionContractRepository.findById(request.getContractId())
                    .orElseThrow(() -> new RuntimeException("期权合约不存在"));
            if (!"CALL".equals(contract.getOptionType())) {
                return Result.error("该合约不是看涨期权");
            }

            OptionOrder order = optionOrderService.createOrder(
                    userId, request.getContractId(), request.getOrderType(),
                    request.getSide(), request.getQuantity(), request.getPrice());
            return Result.success("看涨期权订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/put")
    @ApiOperation(value = "提交看跌期权订单", notes = "提交看跌期权开仓订单")
    public Result<OptionOrder> createPutOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOptionOrderRequest request) {
        try {
            // 验证期权类型为PUT
            OptionContract contract = optionContractRepository.findById(request.getContractId())
                    .orElseThrow(() -> new RuntimeException("期权合约不存在"));
            if (!"PUT".equals(contract.getOptionType())) {
                return Result.error("该合约不是看跌期权");
            }

            OptionOrder order = optionOrderService.createOrder(
                    userId, request.getContractId(), request.getOrderType(),
                    request.getSide(), request.getQuantity(), request.getPrice());
            return Result.success("看跌期权订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 期权定价接口 ====================

    @GetMapping("/pricing")
    @ApiOperation(value = "获取期权定价信息", notes = "获取期权的定价信息（包括市场价格、理论价格、隐含波动率等）")
    public Result<OptionPricingResponse> getPricing(
            @ApiParam(value = "期权合约ID", required = true) @RequestParam Long contractId) {
        try {
            Map<String, Object> pricingInfo = optionPricingService.getPricingInfo(contractId);
            OptionPricingResponse response = new OptionPricingResponse();
            response.setContractId((Long) pricingInfo.get("contractId"));
            response.setMarketPrice((java.math.BigDecimal) pricingInfo.get("marketPrice"));
            response.setTheoreticalPrice((java.math.BigDecimal) pricingInfo.get("theoreticalPrice"));
            response.setUnderlyingPrice((java.math.BigDecimal) pricingInfo.get("underlyingPrice"));
            response.setStrikePrice((java.math.BigDecimal) pricingInfo.get("strikePrice"));
            response.setTimeToExpiry((Double) pricingInfo.get("timeToExpiry"));
            response.setImpliedVolatility((java.math.BigDecimal) pricingInfo.get("impliedVolatility"));
            response.setIntrinsicValue((java.math.BigDecimal) pricingInfo.get("intrinsicValue"));
            response.setTimeValue((java.math.BigDecimal) pricingInfo.get("timeValue"));
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 期权行使接口 ====================

    @PostMapping("/exercise")
    @ApiOperation(value = "行使期权", notes = "提交期权行使请求")
    public Result<OptionExercise> exerciseOption(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ExerciseOptionRequest request) {
        try {
            OptionExercise exercise = optionExerciseService.exerciseOption(
                    userId, request.getPositionId(), request.getQuantity());
            return Result.success("期权行使成功", exercise);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/exercise/status")
    @ApiOperation(value = "查询期权行使状态", notes = "查询期权行使的状态（是否已行使）")
    public Result<List<OptionExercise>> getExerciseStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "持仓ID", required = true) @RequestParam Long positionId) {
        try {
            List<OptionExercise> exercises = optionExerciseService.getExerciseStatus(userId, positionId);
            return Result.success(exercises);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 期权平仓接口 ====================

    @PostMapping("/close")
    @ApiOperation(value = "平仓期权", notes = "提交期权平仓订单")
    public Result<Void> closePosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOptionOrderRequest request) {
        try {
            if (!"CLOSE".equals(request.getOrderType())) {
                return Result.error("订单类型必须是CLOSE");
            }
            optionOrderService.createOrder(
                    userId, request.getContractId(), request.getOrderType(),
                    request.getSide(), request.getQuantity(), request.getPrice());
            return Result.success("平仓订单提交成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/close/history")
    @ApiOperation(value = "查询平仓历史", notes = "查询期权平仓历史")
    public Result<List<OptionOrder>> getCloseHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<OptionOrder> orders = optionOrderService.getUserOrders(userId);
            List<OptionOrder> closeOrders = orders.stream()
                    .filter(order -> "CLOSE".equals(order.getOrderType()))
                    .collect(Collectors.toList());
            return Result.success(closeOrders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 到期日期接口 ====================

    @GetMapping("/expiry")
    @ApiOperation(value = "获取期权到期日期", notes = "获取期权合约的到期日期和有效期")
    public Result<Map<String, Object>> getExpiry(
            @ApiParam(value = "期权合约ID", required = true) @RequestParam Long contractId) {
        try {
            OptionContract contract = optionContractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("期权合约不存在"));

            Map<String, Object> result = new HashMap<>();
            result.put("contractId", contractId);
            result.put("expiryDate", contract.getExpiryDate());
            result.put("pairName", contract.getPairName());
            result.put("optionType", contract.getOptionType());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 高级策略接口 ====================

    @PostMapping("/strategy/straddle")
    @ApiOperation(value = "创建跨式期权策略", notes = "提交跨式期权策略")
    public Result<OptionStrategy> createStraddleStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName,
            @RequestParam java.math.BigDecimal strikePrice,
            @RequestParam java.time.LocalDateTime expiryDate,
            @RequestParam java.math.BigDecimal quantity) {
        try {
            OptionStrategy strategy = optionStrategyService.createStraddleStrategy(
                    userId, pairName, strikePrice, expiryDate, quantity);
            return Result.success("跨式期权策略创建成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/butterfly")
    @ApiOperation(value = "创建蝶式期权策略", notes = "提交蝶式期权策略")
    public Result<OptionStrategy> createButterflyStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName,
            @RequestParam java.math.BigDecimal lowerStrike,
            @RequestParam java.math.BigDecimal middleStrike,
            @RequestParam java.math.BigDecimal upperStrike,
            @RequestParam java.time.LocalDateTime expiryDate,
            @RequestParam java.math.BigDecimal quantity,
            @RequestParam String optionType) {
        try {
            OptionStrategy strategy = optionStrategyService.createButterflyStrategy(
                    userId, pairName, lowerStrike, middleStrike, upperStrike,
                    expiryDate, quantity, optionType);
            return Result.success("蝶式期权策略创建成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/vertical")
    @ApiOperation(value = "创建价差策略", notes = "提交牛市/熊市价差策略")
    public Result<OptionStrategy> createVerticalSpreadStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName,
            @RequestParam java.math.BigDecimal buyStrike,
            @RequestParam java.math.BigDecimal sellStrike,
            @RequestParam java.time.LocalDateTime expiryDate,
            @RequestParam java.math.BigDecimal quantity,
            @RequestParam String optionType,
            @RequestParam String direction) {
        try {
            OptionStrategy strategy = optionStrategyService.createVerticalSpreadStrategy(
                    userId, pairName, buyStrike, sellStrike, expiryDate,
                    quantity, optionType, direction);
            return Result.success("价差策略创建成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/strategy/calendar")
    @ApiOperation(value = "创建日历价差策略", notes = "提交日历价差策略")
    public Result<OptionStrategy> createCalendarSpreadStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String pairName,
            @RequestParam java.math.BigDecimal strikePrice,
            @RequestParam java.time.LocalDateTime nearExpiryDate,
            @RequestParam java.time.LocalDateTime farExpiryDate,
            @RequestParam java.math.BigDecimal quantity,
            @RequestParam String optionType) {
        try {
            OptionStrategy strategy = optionStrategyService.createCalendarSpreadStrategy(
                    userId, pairName, strikePrice, nearExpiryDate, farExpiryDate,
                    quantity, optionType);
            return Result.success("日历价差策略创建成功", strategy);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 风险管理接口 ====================

    @GetMapping("/account/risk")
    @ApiOperation(value = "查询账户风险", notes = "查询期权账户风险情况")
    public Result<Map<String, Object>> getAccountRisk(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, Object> risk = optionRiskManagementService.getAccountRisk(userId);
            return Result.success(risk);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/stop-loss")
    @ApiOperation(value = "设置止损策略", notes = "设置期权强平止损策略")
    public Result<com.cryptotrade.options.entity.OptionRiskAlert> setStopLoss(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Long positionId,
            @RequestParam String alertType,
            @RequestParam(required = false) java.math.BigDecimal threshold,
            @RequestParam(required = false) java.math.BigDecimal thresholdPercentage) {
        try {
            com.cryptotrade.options.entity.OptionRiskAlert alert = optionRiskManagementService.setStopLoss(
                    userId, positionId, alertType, threshold, thresholdPercentage);
            return Result.success("止损策略设置成功", alert);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}




