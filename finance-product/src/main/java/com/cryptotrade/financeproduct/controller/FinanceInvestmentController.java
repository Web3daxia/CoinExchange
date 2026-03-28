/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.financeproduct.dto.request.FinanceInvestmentRequest;
import com.cryptotrade.financeproduct.entity.FinanceInvestment;
import com.cryptotrade.financeproduct.service.FinanceInvestmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户投资理财产品Controller
 */
@RestController
@RequestMapping("/api/finance/investment")
@Api(tags = "用户投资管理")
public class FinanceInvestmentController {

    @Autowired
    private FinanceInvestmentService investmentService;

    @PostMapping("/invest")
    @ApiOperation(value = "投资理财产品", notes = "用户投资理财产品")
    public Result<FinanceInvestment> invest(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "投资信息", required = true) @RequestBody FinanceInvestmentRequest request) {
        try {
            FinanceInvestment investment = investmentService.invest(userId, request);
            return Result.success("投资成功", investment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-investments")
    @ApiOperation(value = "获取我的投资记录", notes = "获取当前用户的所有投资记录")
    public Result<List<FinanceInvestment>> getMyInvestments(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<FinanceInvestment> investments = investmentService.getUserInvestments(userId);
            return Result.success(investments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-investments/page")
    @ApiOperation(value = "分页获取我的投资记录", notes = "分页获取当前用户的投资记录")
    public Result<Page<FinanceInvestment>> getMyInvestmentsPage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<FinanceInvestment> investments = investmentService.getUserInvestments(userId, page, size);
            return Result.success(investments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{investmentId}")
    @ApiOperation(value = "获取投资详情", notes = "根据ID获取投资详情")
    public Result<FinanceInvestment> getInvestmentById(
            @ApiParam(value = "投资ID", required = true) @PathVariable Long investmentId) {
        try {
            FinanceInvestment investment = investmentService.getInvestmentById(investmentId);
            return Result.success(investment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/expected-profit/{investmentId}")
    @ApiOperation(value = "计算预期收益", notes = "计算投资的预期收益")
    public Result<BigDecimal> calculateExpectedProfit(
            @ApiParam(value = "投资ID", required = true) @PathVariable Long investmentId) {
        try {
            BigDecimal profit = investmentService.calculateExpectedProfit(investmentId);
            return Result.success(profit);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














