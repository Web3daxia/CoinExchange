/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.service.PledgeLoanStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 质押借币统计Controller（用户端）
 */
@RestController
@RequestMapping("/api/pledge-loan/statistics")
@Api(tags = "质押借币-统计")
public class PledgeLoanStatisticsController {

    @Autowired
    private PledgeLoanStatisticsService statisticsService;

    @GetMapping("/my-statistics")
    @ApiOperation(value = "获取我的质押统计", notes = "获取当前用户的质押借币统计信息")
    public Result<Map<String, Object>> getMyStatistics(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            Map<String, Object> statistics = statisticsService.getUserPledgeStatistics(userId);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














