/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.miningpool.entity.MiningSettlement;
import com.cryptotrade.miningpool.service.MiningSettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挖矿收益结算Controller（后台管理）
 */
@RestController
@RequestMapping("/admin/mining/settlement")
@Api(tags = "挖矿收益结算")
public class MiningSettlementController {

    @Autowired
    private MiningSettlementService settlementService;

    @PostMapping("/settle/{poolId}")
    @ApiOperation(value = "结算矿池收益", notes = "手动结算矿池收益并分配给矿工")
    public Result<List<MiningSettlement>> settlePoolRevenue(
            @ApiParam(value = "矿池ID", required = true) @PathVariable Long poolId,
            @ApiParam(value = "矿池收益", required = true) @RequestParam BigDecimal poolRevenue,
            @ApiParam(value = "币种", required = true) @RequestParam String currency) {
        try {
            List<MiningSettlement> settlements = settlementService.settlePoolRevenue(poolId, poolRevenue, currency);
            return Result.success("收益结算成功，共结算" + settlements.size() + "条记录", settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch-settle")
    @ApiOperation(value = "批量结算收益", notes = "按结算周期批量结算矿池收益")
    public Result<List<MiningSettlement>> batchSettle(
            @ApiParam(value = "结算周期", required = true) @RequestParam String settlementCycle) {
        try {
            List<MiningSettlement> settlements = settlementService.batchSettlePoolRevenue(settlementCycle);
            return Result.success("批量结算完成，共结算" + settlements.size() + "条记录", settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pool/{poolId}")
    @ApiOperation(value = "获取矿池的结算记录", notes = "获取指定矿池的所有结算记录")
    public Result<List<MiningSettlement>> getPoolSettlements(
            @ApiParam(value = "矿池ID", required = true) @PathVariable Long poolId) {
        try {
            List<MiningSettlement> settlements = settlementService.getPoolSettlements(poolId);
            return Result.success(settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














