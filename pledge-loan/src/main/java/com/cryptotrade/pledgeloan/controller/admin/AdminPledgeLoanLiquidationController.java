/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.PledgeLoanLiquidation;
import com.cryptotrade.pledgeloan.service.PledgeLoanLiquidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 质押借币平仓管理Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/liquidation")
@Api(tags = "后台管理-质押借币平仓管理")
public class AdminPledgeLoanLiquidationController {

    @Autowired
    private PledgeLoanLiquidationService liquidationService;

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "获取订单的平仓记录", notes = "获取指定订单的平仓记录")
    public Result<List<PledgeLoanLiquidation>> getOrderLiquidations(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            List<PledgeLoanLiquidation> liquidations = liquidationService.getOrderLiquidations(orderId);
            return Result.success(liquidations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/manual/{orderId}")
    @ApiOperation(value = "手动平仓", notes = "手动触发平仓操作")
    public Result<PledgeLoanLiquidation> manualLiquidation(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId,
            @ApiParam(value = "操作人ID", required = true) @RequestHeader("adminId") Long liquidatorId,
            @ApiParam(value = "平仓原因", required = true) @RequestParam String reason) {
        try {
            PledgeLoanLiquidation liquidation = liquidationService.manualLiquidation(orderId, liquidatorId, reason);
            return Result.success("平仓成功", liquidation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














