/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.dto.request.RepaymentRequest;
import com.cryptotrade.pledgeloan.entity.PledgeLoanRepayment;
import com.cryptotrade.pledgeloan.service.PledgeLoanRepaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币还款Controller
 */
@RestController
@RequestMapping("/api/pledge-loan/repayment")
@Api(tags = "质押借币-还款")
public class PledgeLoanRepaymentController {

    @Autowired
    private PledgeLoanRepaymentService repaymentService;

    @PostMapping("/repay-full")
    @ApiOperation(value = "全额还款", notes = "全额还款（本金+利息）")
    public Result<PledgeLoanRepayment> repayFull(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            PledgeLoanRepayment repayment = repaymentService.repayFull(orderId, userId);
            return Result.success("还款成功", repayment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/repay-partial")
    @ApiOperation(value = "部分还款", notes = "部分还款（本金）")
    public Result<PledgeLoanRepayment> repayPartial(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId,
            @ApiParam(value = "还款本金", required = true) @RequestParam BigDecimal principalAmount) {
        try {
            PledgeLoanRepayment repayment = repaymentService.repayPartial(orderId, userId, principalAmount);
            return Result.success("还款成功", repayment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/repay-interest")
    @ApiOperation(value = "还款利息", notes = "还款利息")
    public Result<PledgeLoanRepayment> repayInterest(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId,
            @ApiParam(value = "还款利息", required = true) @RequestParam BigDecimal interestAmount) {
        try {
            PledgeLoanRepayment repayment = repaymentService.repayInterest(orderId, userId, interestAmount);
            return Result.success("还款成功", repayment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/calculate-interest/{orderId}")
    @ApiOperation(value = "计算应还利息", notes = "计算订单的应还利息")
    public Result<BigDecimal> calculateInterest(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            BigDecimal interest = repaymentService.calculateInterest(orderId);
            return Result.success(interest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-repayments")
    @ApiOperation(value = "获取我的还款记录", notes = "获取当前用户的还款记录")
    public Result<List<PledgeLoanRepayment>> getMyRepayments(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<PledgeLoanRepayment> repayments = repaymentService.getUserRepayments(userId);
            return Result.success(repayments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














