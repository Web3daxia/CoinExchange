/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.inviterebate.entity.RebateRecord;
import com.cryptotrade.inviterebate.entity.RebateSettlement;
import com.cryptotrade.inviterebate.entity.SystemRebateConfig;
import com.cryptotrade.inviterebate.service.AdminRebateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 后台返佣管理控制器
 */
@RestController
@RequestMapping("/admin/invite")
@Api(tags = "后台返佣管理")
public class AdminRebateController {
    @Autowired
    private AdminRebateService adminRebateService;

    @PostMapping("/system-config")
    @ApiOperation(value = "设置系统返佣配置", notes = "后台管理员设置系统返佣比例和奖励规则")
    public Result<SystemRebateConfig> setSystemRebateConfig(
            @RequestBody SystemRebateConfig config) {
        try {
            SystemRebateConfig result = adminRebateService.setSystemRebateConfig(config);
            return Result.success("系统返佣配置设置成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/system-configs")
    @ApiOperation(value = "获取系统返佣配置列表", notes = "获取所有系统返佣配置")
    public Result<List<SystemRebateConfig>> getSystemRebateConfigs() {
        try {
            List<SystemRebateConfig> configs = adminRebateService.getSystemRebateConfigs();
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/settlement/{settlementId}/audit")
    @ApiOperation(value = "审核返佣结算", notes = "审核返佣结算记录")
    public Result<RebateSettlement> auditSettlement(
            @ApiParam(value = "结算ID", required = true) @PathVariable Long settlementId,
            @ApiParam(value = "审核人ID", required = true) @RequestHeader("X-Admin-Id") Long auditUserId,
            @ApiParam(value = "审核状态", required = true, allowableValues = "APPROVED,REJECTED") @RequestParam String status,
            @ApiParam(value = "审核备注") @RequestParam(required = false) String remark) {
        try {
            RebateSettlement settlement = adminRebateService.auditSettlement(settlementId, auditUserId, status, remark);
            return Result.success("审核成功", settlement);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "获取返佣统计报表", notes = "获取平台整体的邀请活动效果数据")
    public Result<Map<String, Object>> getRebateStatistics(
            @ApiParam(value = "开始日期", required = true, example = "2024-01-01") @RequestParam String startDate,
            @ApiParam(value = "结束日期", required = true, example = "2024-12-31") @RequestParam String endDate) {
        try {
            Map<String, Object> statistics = adminRebateService.getRebateStatistics(startDate, endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/large-rebates")
    @ApiOperation(value = "查询大额返佣记录", notes = "查询需要审核的大额返佣记录")
    public Result<List<RebateRecord>> getLargeRebateRecords(
            @ApiParam(value = "最小金额", required = true, example = "1000.00") @RequestParam BigDecimal minAmount) {
        try {
            List<RebateRecord> records = adminRebateService.getLargeRebateRecords(minAmount);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















