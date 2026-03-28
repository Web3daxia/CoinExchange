/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.controller;

import com.cryptotrade.spotbot.dto.request.ControlPanelBotConfigRequest;
import com.cryptotrade.spotbot.dto.request.KlineDataRequest;
import com.cryptotrade.spotbot.dto.response.KlineDataResponse;
import com.cryptotrade.spotbot.entity.ControlPanelBotConfig;
import com.cryptotrade.spotbot.service.ControlPanelBotConfigService;
import com.cryptotrade.spotbot.service.ControlPanelKlineDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 控盘机器人管理Controller
 */
@RestController
@RequestMapping("/api/control-panel-bot")
@Api(tags = "控盘机器人管理")
public class ControlPanelBotController {

    @Autowired
    private ControlPanelBotConfigService configService;

    @Autowired
    private ControlPanelKlineDataService klineDataService;

    // ========== 配置管理 ==========

    @PostMapping("/configs")
    @ApiOperation(value = "创建控盘机器人配置", notes = "创建新的控盘机器人配置")
    public ResponseEntity<ControlPanelBotConfig> createConfig(
            @ApiParam(value = "配置信息", required = true) @RequestBody ControlPanelBotConfigRequest request) {
        ControlPanelBotConfig config = configService.createConfig(request);
        return ResponseEntity.ok(config);
    }

    @PutMapping("/configs/{id}")
    @ApiOperation(value = "更新控盘机器人配置", notes = "更新控盘机器人配置信息")
    public ResponseEntity<ControlPanelBotConfig> updateConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id,
            @ApiParam(value = "配置信息", required = true) @RequestBody ControlPanelBotConfigRequest request) {
        ControlPanelBotConfig config = configService.updateConfig(id, request);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/configs/{id}")
    @ApiOperation(value = "获取控盘机器人配置", notes = "根据ID获取控盘机器人配置")
    public ResponseEntity<ControlPanelBotConfig> getConfigById(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        return configService.getConfigById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/configs/pair/{pairName}")
    @ApiOperation(value = "根据交易对获取配置", notes = "根据交易对名称获取控盘机器人配置")
    public ResponseEntity<ControlPanelBotConfig> getConfigByPairName(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        return configService.getConfigByPairName(pairName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/configs")
    @ApiOperation(value = "获取所有控盘机器人配置", notes = "获取所有控盘机器人配置列表")
    public ResponseEntity<List<ControlPanelBotConfig>> getAllConfigs() {
        List<ControlPanelBotConfig> configs = configService.getAllConfigs();
        return ResponseEntity.ok(configs);
    }

    @DeleteMapping("/configs/{id}")
    @ApiOperation(value = "删除控盘机器人配置", notes = "根据ID删除控盘机器人配置")
    public ResponseEntity<Void> deleteConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        configService.deleteConfig(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/configs/{id}/status")
    @ApiOperation(value = "更新配置状态", notes = "启用或禁用控盘机器人配置")
    public ResponseEntity<ControlPanelBotConfig> updateStatus(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        ControlPanelBotConfig config = configService.updateStatus(id, status);
        return ResponseEntity.ok(config);
    }

    // ========== K线数据管理 ==========

    @PostMapping("/kline")
    @ApiOperation(value = "创建K线数据", notes = "创建单条K线数据。K线周期最短为5分钟（5m），最长为1周（1w）。K线的时段与行情时段相同，即开盘时间和收盘时间对应实际的行情时间段")
    public ResponseEntity<com.cryptotrade.spotbot.document.ControlPanelKlineData> createKlineData(
            @ApiParam(value = "K线数据（周期：5m, 15m, 30m, 1h, 4h, 1d, 1w）", required = true) @RequestBody KlineDataRequest request) {
        com.cryptotrade.spotbot.document.ControlPanelKlineData klineData = klineDataService.createKlineData(request);
        return ResponseEntity.ok(klineData);
    }

    @PostMapping("/kline/batch")
    @ApiOperation(value = "批量创建K线数据", notes = "批量创建K线数据。K线周期最短为5分钟（5m），最长为1周（1w）。K线的时段与行情时段相同")
    public ResponseEntity<List<com.cryptotrade.spotbot.document.ControlPanelKlineData>> batchCreateKlineData(
            @ApiParam(value = "K线数据列表（周期：5m, 15m, 30m, 1h, 4h, 1d, 1w）", required = true) @RequestBody List<KlineDataRequest> requests) {
        List<com.cryptotrade.spotbot.document.ControlPanelKlineData> klineDataList = klineDataService.batchCreateKlineData(requests);
        return ResponseEntity.ok(klineDataList);
    }

    @PutMapping("/kline/{id}")
    @ApiOperation(value = "更新K线数据", notes = "更新K线数据。K线周期最短为5分钟（5m），最长为1周（1w）")
    public ResponseEntity<com.cryptotrade.spotbot.document.ControlPanelKlineData> updateKlineData(
            @ApiParam(value = "K线数据ID", required = true) @PathVariable String id,
            @ApiParam(value = "K线数据（周期：5m, 15m, 30m, 1h, 4h, 1d, 1w）", required = true) @RequestBody KlineDataRequest request) {
        com.cryptotrade.spotbot.document.ControlPanelKlineData klineData = klineDataService.updateKlineData(id, request);
        return ResponseEntity.ok(klineData);
    }

    @GetMapping("/kline/{id}")
    @ApiOperation(value = "获取K线数据", notes = "根据ID获取K线数据")
    public ResponseEntity<com.cryptotrade.spotbot.document.ControlPanelKlineData> getKlineDataById(
            @ApiParam(value = "K线数据ID", required = true) @PathVariable String id) {
        com.cryptotrade.spotbot.document.ControlPanelKlineData klineData = klineDataService.getKlineDataById(id);
        return ResponseEntity.ok(klineData);
    }

    @GetMapping("/kline/pair/{pairName}/interval/{interval}")
    @ApiOperation(value = "获取K线数据列表", notes = "根据交易对名称和K线周期获取K线数据列表。K线周期最短为5分钟（5m），最长为1周（1w）")
    public ResponseEntity<List<KlineDataResponse>> getKlineDataByPairAndInterval(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName,
            @ApiParam(value = "K线周期: 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）", required = true) @PathVariable String interval) {
        List<KlineDataResponse> klineDataList = klineDataService.getKlineDataByPairAndInterval(pairName, interval);
        return ResponseEntity.ok(klineDataList);
    }

    @GetMapping("/kline/pair/{pairName}/interval/{interval}/range")
    @ApiOperation(value = "获取时间范围内的K线数据", notes = "根据交易对名称、K线周期和时间范围获取K线数据。K线周期最短为5分钟（5m），最长为1周（1w）。K线的时段与行情时段相同")
    public ResponseEntity<List<KlineDataResponse>> getKlineDataByTimeRange(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName,
            @ApiParam(value = "K线周期: 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）", required = true) @PathVariable String interval,
            @ApiParam(value = "开始时间（对应行情时段的开始时间）", required = true) @RequestParam LocalDateTime startTime,
            @ApiParam(value = "结束时间（对应行情时段的结束时间）", required = true) @RequestParam LocalDateTime endTime) {
        List<KlineDataResponse> klineDataList = klineDataService
                .getKlineDataByPairAndIntervalAndTimeRange(pairName, interval, startTime, endTime);
        return ResponseEntity.ok(klineDataList);
    }

    @DeleteMapping("/kline/{id}")
    @ApiOperation(value = "删除K线数据", notes = "根据ID删除K线数据")
    public ResponseEntity<Void> deleteKlineData(
            @ApiParam(value = "K线数据ID", required = true) @PathVariable String id) {
        klineDataService.deleteKlineData(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/kline/pair/{pairName}")
    @ApiOperation(value = "删除交易对的所有K线数据", notes = "删除指定交易对的所有K线数据")
    public ResponseEntity<Void> deleteKlineDataByPairName(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        klineDataService.deleteKlineDataByPairName(pairName);
        return ResponseEntity.ok().build();
    }
}

