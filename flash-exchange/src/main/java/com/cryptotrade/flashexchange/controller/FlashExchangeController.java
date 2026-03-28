/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.flashexchange.dto.request.ExecuteExchangeRequest;
import com.cryptotrade.flashexchange.entity.FlashExchangeOrder;
import com.cryptotrade.flashexchange.service.FlashExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 闪兑交易控制器
 */
@RestController
@RequestMapping("/flash-exchange")
@Api(tags = "闪兑交易模块")
public class FlashExchangeController {

    @Autowired
    private FlashExchangeService flashExchangeService;

    @GetMapping
    @ApiOperation(value = "查询支持的闪兑币种及实时汇率", notes = "查询支持的币种和实时汇率")
    public Result<Map<String, Object>> getSupportedCurrencies(
            @ApiParam(value = "源币种") @RequestParam(required = false) String fromCurrency,
            @ApiParam(value = "目标币种") @RequestParam(required = false) String toCurrency) {
        try {
            Map<String, Object> result = flashExchangeService.getSupportedCurrencies(fromCurrency, toCurrency);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/calculate")
    @ApiOperation(value = "计算兑换数量", notes = "根据输入的金额计算兑换后的数量")
    public Result<Map<String, Object>> calculateExchange(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam java.math.BigDecimal fromAmount) {
        try {
            Map<String, Object> result = flashExchangeService.calculateExchange(fromCurrency, toCurrency, fromAmount);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/execute")
    @ApiOperation(value = "执行闪兑交易", notes = "执行闪兑交易，用户输入兑换金额，系统自动计算兑换数量")
    public Result<FlashExchangeOrder> executeExchange(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ExecuteExchangeRequest request) {
        try {
            FlashExchangeOrder order = flashExchangeService.executeExchange(
                    userId, request.getFromCurrency(), request.getToCurrency(),
                    request.getFromAmount(), request.getMaxSlippage());
            return Result.success("兑换成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询兑换历史", notes = "查询用户的兑换历史记录")
    public Result<List<FlashExchangeOrder>> getExchangeHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<FlashExchangeOrder> orders = flashExchangeService.getUserExchangeHistory(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "查询订单详情", notes = "查询闪兑订单详情")
    public Result<FlashExchangeOrder> getOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            FlashExchangeOrder order = flashExchangeService.getOrder(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















