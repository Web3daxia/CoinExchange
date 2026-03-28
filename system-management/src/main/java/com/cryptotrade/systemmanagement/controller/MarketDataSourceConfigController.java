/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.MarketDataSourceConfig;
import com.cryptotrade.systemmanagement.service.MarketDataSourceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/system/market-data-config")
@Api(tags = "行情源配置管理")
public class MarketDataSourceConfigController {
    
    @Autowired
    private MarketDataSourceConfigService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建行情源配置")
    public Result<MarketDataSourceConfig> create(@Valid @RequestBody MarketDataSourceConfig config) {
        try {
            return Result.success("创建成功", service.createConfig(config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新行情源配置")
    public Result<MarketDataSourceConfig> update(@PathVariable Long id, @Valid @RequestBody MarketDataSourceConfig config) {
        try {
            return Result.success("更新成功", service.updateConfig(id, config));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除行情源配置")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询所有行情源配置")
    public Result<List<MarketDataSourceConfig>> list() {
        try {
            return Result.success(service.getAllConfigs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/trading-area/{tradingArea}")
    @ApiOperation(value = "根据交易区域查询行情源配置")
    public Result<List<MarketDataSourceConfig>> getByTradingArea(@PathVariable String tradingArea) {
        try {
            return Result.success(service.getConfigsByTradingArea(tradingArea));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/default/{tradingArea}")
    @ApiOperation(value = "获取指定交易区域的默认行情源配置")
    public Result<MarketDataSourceConfig> getDefault(@PathVariable String tradingArea) {
        try {
            return Result.success(service.getDefaultConfigByTradingArea(tradingArea));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/set-default/{id}")
    @ApiOperation(value = "设置默认行情源配置")
    public Result<Void> setDefault(@PathVariable Long id) {
        try {
            service.setDefault(id);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














