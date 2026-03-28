/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.service.PledgeLoanStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 后台管理 - 质押借币统计Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/statistics")
@Api(tags = "后台管理-质押借币统计")
public class AdminPledgeLoanStatisticsController {

    @Autowired
    private PledgeLoanStatisticsService statisticsService;

    @GetMapping("/platform")
    @ApiOperation(value = "获取平台质押统计", notes = "获取平台的质押借币统计信息")
    public Result<Map<String, Object>> getPlatformStatistics() {
        try {
            Map<String, Object> statistics = statisticsService.getPlatformPledgeStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/risk")
    @ApiOperation(value = "获取风险统计", notes = "获取风险监控统计信息")
    public Result<Map<String, Object>> getRiskStatistics() {
        try {
            Map<String, Object> statistics = statisticsService.getRiskStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/interest-income")
    @ApiOperation(value = "获取利息收入统计", notes = "获取指定时间范围内的利息收入统计")
    public Result<Map<String, Object>> getInterestIncomeStatistics(
            @ApiParam(value = "开始时间", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam(value = "结束时间", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            Map<String, Object> statistics = statisticsService.getInterestIncomeStatistics(startTime, endTime);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/liquidation")
    @ApiOperation(value = "获取平仓统计", notes = "获取指定时间范围内的平仓统计")
    public Result<Map<String, Object>> getLiquidationStatistics(
            @ApiParam(value = "开始时间", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam(value = "结束时间", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            Map<String, Object> statistics = statisticsService.getLiquidationStatistics(startTime, endTime);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














