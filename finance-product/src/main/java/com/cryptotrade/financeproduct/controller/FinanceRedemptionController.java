/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.financeproduct.dto.request.FinanceRedemptionRequest;
import com.cryptotrade.financeproduct.entity.FinanceRedemption;
import com.cryptotrade.financeproduct.service.FinanceRedemptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理财产品赎回管理Controller
 */
@RestController
@RequestMapping("/api/finance/redemption")
@Api(tags = "理财产品赎回")
public class FinanceRedemptionController {

    @Autowired
    private FinanceRedemptionService redemptionService;

    @PostMapping("/redeem")
    @ApiOperation(value = "赎回投资", notes = "用户赎回理财产品投资")
    public Result<FinanceRedemption> redeem(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "赎回信息", required = true) @RequestBody FinanceRedemptionRequest request) {
        try {
            FinanceRedemption redemption = redemptionService.redeem(userId, request);
            return Result.success("赎回申请成功", redemption);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-redemptions")
    @ApiOperation(value = "获取我的赎回记录", notes = "获取当前用户的所有赎回记录")
    public Result<List<FinanceRedemption>> getMyRedemptions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<FinanceRedemption> redemptions = redemptionService.getUserRedemptions(userId);
            return Result.success(redemptions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-redemptions/page")
    @ApiOperation(value = "分页获取我的赎回记录", notes = "分页获取当前用户的赎回记录")
    public Result<Page<FinanceRedemption>> getMyRedemptionsPage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<FinanceRedemption> redemptions = redemptionService.getUserRedemptions(userId, page, size);
            return Result.success(redemptions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/fee/{investmentId}")
    @ApiOperation(value = "计算赎回手续费", notes = "计算赎回所需的手续费")
    public Result<BigDecimal> calculateRedemptionFee(
            @ApiParam(value = "投资ID", required = true) @PathVariable Long investmentId,
            @ApiParam(value = "赎回金额") @RequestParam(required = false) BigDecimal redemptionAmount) {
        try {
            BigDecimal fee = redemptionService.calculateRedemptionFee(investmentId, redemptionAmount);
            return Result.success(fee);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














