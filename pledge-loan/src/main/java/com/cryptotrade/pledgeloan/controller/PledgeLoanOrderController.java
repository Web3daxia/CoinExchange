/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.dto.request.PledgeLoanOrderRequest;
import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币订单Controller
 */
@RestController
@RequestMapping("/api/pledge-loan/order")
@Api(tags = "质押借币")
public class PledgeLoanOrderController {

    @Autowired
    private PledgeLoanOrderService orderService;

    @PostMapping("/create")
    @ApiOperation(value = "创建质押借币订单", notes = "申请质押借币")
    public Result<PledgeLoanOrder> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "订单信息", required = true) @RequestBody PledgeLoanOrderRequest request) {
        try {
            PledgeLoanOrder order = orderService.createOrder(userId, request.getPledgeCurrency(),
                    request.getPledgeAmount(), request.getLoanCurrency(), request.getLoanAmount(),
                    request.getLoanTermDays());
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/calculate-loan-amount")
    @ApiOperation(value = "计算可借额度", notes = "根据质押币种和数量计算可借额度")
    public Result<BigDecimal> calculateLoanAmount(
            @ApiParam(value = "质押币种", required = true) @RequestParam String pledgeCurrency,
            @ApiParam(value = "质押数量", required = true) @RequestParam BigDecimal pledgeAmount,
            @ApiParam(value = "借款币种", required = true) @RequestParam String loanCurrency) {
        try {
            BigDecimal maxLoanAmount = orderService.calculateLoanAmount(pledgeCurrency, pledgeAmount, loanCurrency);
            return Result.success(maxLoanAmount);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-orders")
    @ApiOperation(value = "获取我的订单列表", notes = "获取当前用户的质押借币订单列表")
    public Result<Page<PledgeLoanOrder>> getMyOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<PledgeLoanOrder> orders = orderService.getUserOrders(userId, page, size);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "获取订单详情", notes = "根据ID获取订单详情")
    public Result<PledgeLoanOrder> getOrderById(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            PledgeLoanOrder order = orderService.getOrderById(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/health-rate/{orderId}")
    @ApiOperation(value = "计算订单健康度", notes = "计算并返回订单的健康度")
    public Result<BigDecimal> calculateHealthRate(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            BigDecimal healthRate = orderService.calculateHealthRate(orderId);
            return Result.success(healthRate);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














