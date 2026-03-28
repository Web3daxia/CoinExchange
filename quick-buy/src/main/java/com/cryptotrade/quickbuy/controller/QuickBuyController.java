/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.quickbuy.dto.request.ExecuteQuickBuyRequest;
import com.cryptotrade.quickbuy.entity.QuickBuyOrder;
import com.cryptotrade.quickbuy.service.QuickBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 快捷买币控制器
 */
@RestController
@RequestMapping("/quick-buy")
@Api(tags = "快捷买币模块")
public class QuickBuyController {

    @Autowired
    private QuickBuyService quickBuyService;

    @GetMapping
    @ApiOperation(value = "查询可用选项", notes = "查询可用于快捷购买的加密货币及支付方式")
    public Result<Map<String, Object>> getAvailableOptions() {
        try {
            Map<String, Object> options = quickBuyService.getAvailableOptions();
            return Result.success(options);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/execute")
    @ApiOperation(value = "执行快捷买币", notes = "执行快捷买币交易")
    public Result<QuickBuyOrder> executeQuickBuy(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ExecuteQuickBuyRequest request) {
        try {
            QuickBuyOrder order = quickBuyService.executeQuickBuy(
                    userId, request.getCryptoCurrency(), request.getFiatCurrency(),
                    request.getPaymentMethod(), request.getCryptoAmount());
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/orders")
    @ApiOperation(value = "查询订单历史", notes = "获取用户的交易历史记录")
    public Result<List<QuickBuyOrder>> getUserOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<QuickBuyOrder> orders = quickBuyService.getUserOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/confirm-payment")
    @ApiOperation(value = "确认支付", notes = "确认支付并上传支付凭证")
    public Result<Void> confirmPayment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long orderId,
            @RequestParam String paymentProof) {
        try {
            quickBuyService.confirmPayment(userId, orderId, paymentProof);
            return Result.success("支付确认成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















