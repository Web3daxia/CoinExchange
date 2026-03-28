/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.PledgeLoanRateHistory;
import com.cryptotrade.pledgeloan.service.PledgeLoanRateHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 质押借币利率调整历史Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/rate-history")
@Api(tags = "后台管理-质押借币利率调整历史")
public class AdminPledgeLoanRateHistoryController {

    @Autowired
    private PledgeLoanRateHistoryService rateHistoryService;

    @GetMapping("/currency/{currencyCode}")
    @ApiOperation(value = "获取币种的利率调整历史", notes = "根据币种代码获取利率调整历史")
    public Result<List<PledgeLoanRateHistory>> getRateHistoryByCurrency(
            @ApiParam(value = "币种代码", required = true) @PathVariable String currencyCode) {
        try {
            List<PledgeLoanRateHistory> history = rateHistoryService.getRateHistory(currencyCode);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/config-type/{configType}")
    @ApiOperation(value = "获取配置类型的利率调整历史", notes = "根据配置类型获取利率调整历史")
    public Result<List<PledgeLoanRateHistory>> getRateHistoryByConfigType(
            @ApiParam(value = "配置类型: PLEDGE, LOAN", required = true) @PathVariable String configType) {
        try {
            List<PledgeLoanRateHistory> history = rateHistoryService.getRateHistoryByConfigType(configType);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














