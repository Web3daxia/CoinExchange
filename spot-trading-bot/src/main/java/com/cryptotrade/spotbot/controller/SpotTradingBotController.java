/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.spotbot.dto.request.BotConfigRequest;
import com.cryptotrade.spotbot.dto.request.MatchOrderRequest;
import com.cryptotrade.spotbot.dto.response.BotSimulationResponse;
import com.cryptotrade.spotbot.dto.response.MatchOrderResponse;
import com.cryptotrade.spotbot.entity.SpotTradingBotConfig;
import com.cryptotrade.spotbot.service.OrderMatchingService;
import com.cryptotrade.spotbot.service.SpotTradingBotConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/spot-bot")
@Api(tags = "现货交易机器人模块")
public class SpotTradingBotController {
    
    @Autowired
    private SpotTradingBotConfigService botConfigService;
    
    @Autowired
    private OrderMatchingService orderMatchingService;
    
    // ==================== 机器人配置管理 ====================
    
    @PostMapping("/config/create")
    @ApiOperation(value = "创建机器人配置", notes = "为指定交易对创建交易机器人配置")
    public Result<SpotTradingBotConfig> createConfig(@Valid @RequestBody BotConfigRequest request) {
        try {
            SpotTradingBotConfig config = botConfigService.createConfig(request);
            return Result.success("机器人配置创建成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/config/update/{configId}")
    @ApiOperation(value = "更新机器人配置", notes = "更新机器人配置参数")
    public Result<SpotTradingBotConfig> updateConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long configId,
            @Valid @RequestBody BotConfigRequest request) {
        try {
            SpotTradingBotConfig config = botConfigService.updateConfig(configId, request);
            return Result.success("机器人配置更新成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/config/{configId}")
    @ApiOperation(value = "删除机器人配置", notes = "删除指定机器人配置")
    public Result<Void> deleteConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long configId) {
        try {
            botConfigService.deleteConfig(configId);
            return Result.success("机器人配置删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/config/list")
    @ApiOperation(value = "查询机器人配置列表", notes = "查询所有机器人配置")
    public Result<List<SpotTradingBotConfig>> getAllConfigs() {
        try {
            List<SpotTradingBotConfig> configs = botConfigService.getAllConfigs();
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/config/{configId}")
    @ApiOperation(value = "查询机器人配置详情", notes = "根据ID查询机器人配置详情")
    public Result<SpotTradingBotConfig> getConfigById(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long configId) {
        try {
            SpotTradingBotConfig config = botConfigService.getConfigById(configId);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/config/pair/{pairName}")
    @ApiOperation(value = "根据交易对查询配置", notes = "根据交易对名称查询机器人配置")
    public Result<SpotTradingBotConfig> getConfigByPairName(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        try {
            SpotTradingBotConfig config = botConfigService.getConfigByPairName(pairName);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ==================== 机器人测算 ====================
    
    @PostMapping("/simulate")
    @ApiOperation(value = "机器人参数测算", notes = "根据输入的机器人参数进行测算，返回预估数据和盘口数据")
    public Result<BotSimulationResponse> simulateConfig(@Valid @RequestBody BotConfigRequest request) {
        try {
            BotSimulationResponse response = botConfigService.simulateConfig(request);
            return Result.success("测算完成", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/simulate/{configId}")
    @ApiOperation(value = "根据配置ID进行测算", notes = "根据已保存的机器人配置进行测算")
    public Result<BotSimulationResponse> simulateConfigById(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long configId) {
        try {
            BotSimulationResponse response = botConfigService.simulateConfigById(configId);
            return Result.success("测算完成", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ==================== 订单撮合 ====================
    
    @PostMapping("/match")
    @ApiOperation(value = "撮合用户订单", notes = "用户下单时立即进行撮合，买入或卖出操作")
    public Result<MatchOrderResponse> matchUserOrder(@Valid @RequestBody MatchOrderRequest request) {
        try {
            MatchOrderResponse response = orderMatchingService.matchUserOrder(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/generate/{pairName}")
    @ApiOperation(value = "生成机器人订单", notes = "无用户订单时生成机器人订单")
    public Result<Boolean> generateBotOrder(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        try {
            boolean success = orderMatchingService.generateBotOrder(pairName);
            return Result.success(success ? "订单生成成功" : "订单生成失败", success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/start/{pairName}")
    @ApiOperation(value = "启动机器人", notes = "启动指定交易对的交易机器人")
    public Result<Void> startBot(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        try {
            orderMatchingService.startBot(pairName);
            return Result.success("机器人启动成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/stop/{pairName}")
    @ApiOperation(value = "停止机器人", notes = "停止指定交易对的交易机器人")
    public Result<Void> stopBot(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        try {
            orderMatchingService.stopBot(pairName);
            return Result.success("机器人停止成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














