/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.strategy.dto.request.ExecuteStrategyRequest;
import com.cryptotrade.strategy.dto.response.StrategyInfoResponse;
import com.cryptotrade.strategy.entity.StrategyTemplate;
import com.cryptotrade.strategy.entity.StrategyBacktest;
import com.cryptotrade.strategy.entity.TradingStrategy;
import com.cryptotrade.strategy.service.StrategyManagementService;
import com.cryptotrade.strategy.service.StrategyTemplateService;
import com.cryptotrade.strategy.service.TradingStrategyService;
import com.cryptotrade.strategy.service.StrategyBacktestService;
import com.cryptotrade.strategy.service.StrategyPerformanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 策略交易控制器
 */
@RestController
@RequestMapping("/strategy")
@Api(tags = "策略交易模块")
public class StrategyController {

    @Autowired
    private StrategyManagementService strategyManagementService;

    @Autowired
    private StrategyTemplateService strategyTemplateService;

    @Autowired
    private TradingStrategyService tradingStrategyService;

    @Autowired
    private StrategyBacktestService strategyBacktestService;

    @Autowired
    private StrategyPerformanceService strategyPerformanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/execute")
    @ApiOperation(value = "执行策略", notes = "执行指定的交易策略")
    public Result<Void> executeStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ExecuteStrategyRequest request) {
        try {
            // 将策略参数Map转换为JSON字符串
            String strategyParamsJson = objectMapper.writeValueAsString(
                    request.getStrategyParams() != null ? request.getStrategyParams() : new HashMap<>());

            // 执行策略
            strategyManagementService.executeStrategy(
                    userId,
                    request.getPairName(),
                    request.getMarketType(),
                    request.getStrategyType(),
                    strategyParamsJson);

            return Result.success("策略执行成功", null);
        } catch (Exception e) {
            return Result.error("策略执行失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取支持的策略列表", notes = "获取所有支持的策略类型")
    public Result<StrategyInfoResponse> getSupportedStrategies() {
        try {
            Set<String> strategyTypes = strategyManagementService.getSupportedStrategyTypes();
            StrategyInfoResponse response = new StrategyInfoResponse();
            response.setSupportedStrategyTypes(strategyTypes);
            response.setStrategyCount(strategyTypes.size());
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/templates")
    @ApiOperation(value = "获取策略模板列表", notes = "获取平台提供的策略模板列表")
    public Result<List<StrategyTemplate>> getTemplates(
            @ApiParam(value = "市场类型筛选") @RequestParam(required = false) String marketType,
            @ApiParam(value = "策略分类筛选") @RequestParam(required = false) String strategyCategory) {
        try {
            List<StrategyTemplate> templates = strategyTemplateService.getTemplates(marketType, strategyCategory);
            return Result.success(templates);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/templates/{templateId}")
    @ApiOperation(value = "获取策略模板详情", notes = "获取指定策略模板的详细信息")
    public Result<StrategyTemplate> getTemplate(
            @ApiParam(value = "模板ID", required = true) @PathVariable Long templateId) {
        try {
            StrategyTemplate template = strategyTemplateService.getTemplate(templateId);
            return Result.success(template);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/spot/ma-cross/create")
    @ApiOperation(value = "创建均线交叉策略（现货）", notes = "创建现货市场的均线交叉策略")
    public Result<com.cryptotrade.strategy.dto.response.StrategyResponse> createMaCrossStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody com.cryptotrade.strategy.dto.request.CreateMaCrossStrategyRequest request) {
        try {
            TradingStrategy strategy = new TradingStrategy();
            strategy.setUserId(userId);
            strategy.setStrategyName(request.getStrategyName() != null ? request.getStrategyName() : 
                    "均线交叉策略_" + request.getPairName());
            strategy.setPairName(request.getPairName());
            strategy.setMarketType("SPOT");
            strategy.setStrategyType("MA_CROSS");
            strategy.setInitialCapital(request.getInitialCapital());
            
            // 设置策略参数（JSON格式）
            Map<String, Object> params = new HashMap<>();
            params.put("shortPeriod", request.getShortPeriod());
            params.put("longPeriod", request.getLongPeriod());
            params.put("maType", request.getMaType());
            params.put("investmentRatio", request.getInvestmentRatio());
            params.put("stopLossPercentage", request.getStopLossPercentage());
            params.put("takeProfitPercentage", request.getTakeProfitPercentage());
            strategy.setStrategyParams(objectMapper.writeValueAsString(params));
            
            strategy = tradingStrategyService.createStrategy(userId, strategy);
            
            com.cryptotrade.strategy.dto.response.StrategyResponse response = new com.cryptotrade.strategy.dto.response.StrategyResponse();
            BeanUtils.copyProperties(strategy, response);
            return Result.success("策略创建成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list/user")
    @ApiOperation(value = "查询策略列表", notes = "查询用户的所有策略列表")
    public Result<List<com.cryptotrade.strategy.dto.response.StrategyResponse>> getStrategies(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "市场类型筛选") @RequestParam(required = false) String marketType,
            @ApiParam(value = "策略类型筛选") @RequestParam(required = false) String strategyType,
            @ApiParam(value = "交易对筛选") @RequestParam(required = false) String pairName,
            @ApiParam(value = "状态筛选") @RequestParam(required = false) String status) {
        try {
            List<TradingStrategy> strategies = tradingStrategyService.getStrategies(userId, marketType, strategyType, pairName, status);
            List<com.cryptotrade.strategy.dto.response.StrategyResponse> responses = strategies.stream().map(s -> {
                com.cryptotrade.strategy.dto.response.StrategyResponse response = new com.cryptotrade.strategy.dto.response.StrategyResponse();
                BeanUtils.copyProperties(s, response);
                response.setNetProfit(s.getTotalProfit().subtract(s.getTotalLoss()));
                return response;
            }).collect(Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{strategyId}")
    @ApiOperation(value = "查询策略详情", notes = "查询指定策略的详细信息")
    public Result<com.cryptotrade.strategy.dto.response.StrategyResponse> getStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @PathVariable Long strategyId) {
        try {
            TradingStrategy strategy = tradingStrategyService.getStrategy(userId, strategyId);
            com.cryptotrade.strategy.dto.response.StrategyResponse response = new com.cryptotrade.strategy.dto.response.StrategyResponse();
            BeanUtils.copyProperties(strategy, response);
            response.setNetProfit(strategy.getTotalProfit().subtract(strategy.getTotalLoss()));
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{strategyId}/start")
    @ApiOperation(value = "启动策略", notes = "启动指定的策略")
    public Result<com.cryptotrade.strategy.dto.response.StrategyResponse> startStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @PathVariable Long strategyId) {
        try {
            TradingStrategy strategy = tradingStrategyService.startStrategy(userId, strategyId);
            com.cryptotrade.strategy.dto.response.StrategyResponse response = new com.cryptotrade.strategy.dto.response.StrategyResponse();
            BeanUtils.copyProperties(strategy, response);
            return Result.success("策略启动成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{strategyId}/stop")
    @ApiOperation(value = "停止策略", notes = "停止指定的策略")
    public Result<com.cryptotrade.strategy.dto.response.StrategyResponse> stopStrategy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @PathVariable Long strategyId,
            @ApiParam(value = "是否平掉所有持仓") @RequestParam(required = false, defaultValue = "true") Boolean closePositions,
            @ApiParam(value = "是否取消所有未成交订单") @RequestParam(required = false, defaultValue = "true") Boolean cancelOrders) {
        try {
            TradingStrategy strategy = tradingStrategyService.stopStrategy(userId, strategyId, closePositions, cancelOrders);
            com.cryptotrade.strategy.dto.response.StrategyResponse response = new com.cryptotrade.strategy.dto.response.StrategyResponse();
            BeanUtils.copyProperties(strategy, response);
            return Result.success("策略停止成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/backtest/create")
    @ApiOperation(value = "创建策略回测任务", notes = "创建策略回测任务，使用历史数据验证策略有效性")
    public Result<StrategyBacktest> createBacktest(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "策略ID", required = true) @RequestParam Long strategyId,
            @ApiParam(value = "开始时间", required = true) @RequestParam String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam String endTime,
            @ApiParam(value = "初始资金") @RequestParam(required = false) java.math.BigDecimal initialCapital) {
        try {
            StrategyBacktest backtest = strategyBacktestService.createBacktest(userId, strategyId, startTime, endTime, initialCapital);
            return Result.success("回测任务创建成功", backtest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/backtest/{backtestId}/result")
    @ApiOperation(value = "查询回测结果", notes = "查询回测任务的详细结果")
    public Result<StrategyBacktest> getBacktestResult(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "回测任务ID", required = true) @PathVariable String backtestId) {
        try {
            StrategyBacktest backtest = strategyBacktestService.getBacktestResult(backtestId);
            return Result.success(backtest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}



