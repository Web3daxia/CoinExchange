/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.spot.dto.request.MarketAlertRequest;
import com.cryptotrade.spot.entity.MarketAlert;
import com.cryptotrade.spot.service.MarketAlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/market/alerts")
@Api(tags = "市场异动提醒模块")
public class MarketAlertController {

    @Autowired
    private MarketAlertService marketAlertService;

    @PostMapping("/set")
    @ApiOperation(value = "设置市场异动提醒", notes = "设置价格或成交量异动提醒")
    public Result<MarketAlert> setAlert(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody MarketAlertRequest request) {
        try {
            MarketAlert alert = marketAlertService.setAlert(userId, request);
            return Result.success("提醒设置成功", alert);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询提醒列表", notes = "查询用户的所有提醒")
    public Result<List<MarketAlert>> getUserAlerts(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<MarketAlert> alerts = marketAlertService.getUserAlerts(userId);
            return Result.success(alerts);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{alertId}")
    @ApiOperation(value = "查询提醒状态", notes = "根据ID查询提醒详情")
    public Result<MarketAlert> getAlertStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "提醒ID", required = true) @PathVariable Long alertId) {
        try {
            MarketAlert alert = marketAlertService.getAlertStatus(userId, alertId);
            return Result.success(alert);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{alertId}")
    @ApiOperation(value = "删除提醒", notes = "删除指定的提醒")
    public Result<Void> removeAlert(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "提醒ID", required = true) @PathVariable Long alertId) {
        try {
            marketAlertService.removeAlert(userId, alertId);
            return Result.success("提醒删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{alertId}/toggle")
    @ApiOperation(value = "启用/禁用提醒", notes = "启用或禁用指定的提醒")
    public Result<Void> toggleAlert(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "提醒ID", required = true) @PathVariable Long alertId,
            @ApiParam(value = "是否启用", required = true) @RequestParam Boolean enabled) {
        try {
            marketAlertService.toggleAlert(userId, alertId, enabled);
            return Result.success(enabled ? "提醒已启用" : "提醒已禁用", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


