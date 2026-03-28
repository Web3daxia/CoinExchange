/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.financeproduct.entity.FinanceProfitSettlement;
import com.cryptotrade.financeproduct.service.FinanceSettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 理财产品收益结算Controller（后台管理）
 */
@RestController
@RequestMapping("/admin/finance/settlement")
@Api(tags = "理财产品收益结算")
public class FinanceSettlementController {

    @Autowired
    private FinanceSettlementService settlementService;

    @PostMapping("/settle/{investmentId}")
    @ApiOperation(value = "结算单个投资收益", notes = "手动结算单个投资的收益")
    public Result<FinanceProfitSettlement> settleInvestment(
            @ApiParam(value = "投资ID", required = true) @PathVariable Long investmentId) {
        try {
            FinanceProfitSettlement settlement = settlementService.settleInvestmentProfit(investmentId);
            return Result.success("收益结算成功", settlement);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch-settle")
    @ApiOperation(value = "批量结算收益", notes = "按结算周期批量结算收益")
    public Result<List<FinanceProfitSettlement>> batchSettle(
            @ApiParam(value = "结算周期", required = true) @RequestParam String settlementCycle) {
        try {
            List<FinanceProfitSettlement> settlements = settlementService.batchSettleProfits(settlementCycle);
            return Result.success("批量结算完成，共结算" + settlements.size() + "条记录", settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/investment/{investmentId}")
    @ApiOperation(value = "获取投资的结算记录", notes = "获取指定投资的所有结算记录")
    public Result<List<FinanceProfitSettlement>> getSettlementsByInvestment(
            @ApiParam(value = "投资ID", required = true) @PathVariable Long investmentId) {
        try {
            List<FinanceProfitSettlement> settlements = settlementService.getSettlementsByInvestmentId(investmentId);
            return Result.success(settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pending")
    @ApiOperation(value = "获取待结算记录", notes = "获取所有待结算的收益记录")
    public Result<List<FinanceProfitSettlement>> getPendingSettlements() {
        try {
            List<FinanceProfitSettlement> settlements = settlementService.getPendingSettlements();
            return Result.success(settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














