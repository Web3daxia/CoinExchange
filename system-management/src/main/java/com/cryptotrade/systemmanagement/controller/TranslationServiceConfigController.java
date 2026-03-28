/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.TranslationServiceConfig;
import com.cryptotrade.systemmanagement.service.TranslationServiceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 翻译服务配置管理Controller
 */
@RestController
@RequestMapping("/api/admin/translation-service")
@Api(tags = "后台管理-翻译服务配置")
public class TranslationServiceConfigController {

    @Autowired
    private TranslationServiceConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "获取所有翻译服务配置", notes = "获取所有翻译服务配置列表")
    public Result<List<TranslationServiceConfig>> getAllConfigs() {
        try {
            List<TranslationServiceConfig> configs = configService.getAllConfigs();
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取翻译服务配置详情", notes = "根据ID获取翻译服务配置详情")
    public Result<TranslationServiceConfig> getConfigById(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            TranslationServiceConfig config = configService.getConfigById(id);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation(value = "获取当前启用的翻译服务配置", notes = "获取当前启用的翻译服务配置")
    public Result<TranslationServiceConfig> getActiveConfig() {
        try {
            TranslationServiceConfig config = configService.getActiveConfig();
            if (config == null) {
                return Result.error("没有启用的翻译服务配置");
            }
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建翻译服务配置", notes = "创建新的翻译服务配置")
    public Result<TranslationServiceConfig> createConfig(
            @ApiParam(value = "配置信息", required = true) @RequestBody TranslationServiceConfig config) {
        try {
            TranslationServiceConfig saved = configService.createConfig(config);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新翻译服务配置", notes = "更新翻译服务配置信息")
    public Result<TranslationServiceConfig> updateConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id,
            @ApiParam(value = "配置信息", required = true) @RequestBody TranslationServiceConfig config) {
        try {
            TranslationServiceConfig saved = configService.updateConfig(id, config);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/enable/{id}")
    @ApiOperation(value = "启用翻译服务", notes = "启用指定的翻译服务（会同时禁用其他服务）")
    public Result<TranslationServiceConfig> enableConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            TranslationServiceConfig config = configService.enableConfig(id);
            return Result.success("启用成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/disable/{id}")
    @ApiOperation(value = "禁用翻译服务", notes = "禁用指定的翻译服务")
    public Result<TranslationServiceConfig> disableConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            TranslationServiceConfig config = configService.disableConfig(id);
            return Result.success("禁用成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/test/{id}")
    @ApiOperation(value = "测试翻译服务配置", notes = "测试翻译服务配置是否可用")
    public Result<Boolean> testConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            boolean success = configService.testConfig(id);
            return Result.success(success ? "测试成功" : "测试失败", success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset-usage/{id}")
    @ApiOperation(value = "重置每日使用量", notes = "重置翻译服务的每日使用量统计")
    public Result<Void> resetDailyUsage(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            configService.resetDailyUsage(id);
            return Result.success("重置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除翻译服务配置", notes = "删除翻译服务配置")
    public Result<Void> deleteConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            configService.deleteConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














