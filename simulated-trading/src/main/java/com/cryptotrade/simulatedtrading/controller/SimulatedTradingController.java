/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.simulatedtrading.dto.request.PlaceOrderRequest;
import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRecord;
import com.cryptotrade.simulatedtrading.service.SimulatedTradingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 模拟交易Controller
 */
@RestController
@RequestMapping("/api/simulated-trading/trade")
@Api(tags = "模拟交易")
public class SimulatedTradingController {

    @Autowired
    private SimulatedTradingService tradingService;

    @PostMapping("/place-order")
    @ApiOperation(value = "下单", notes = "进行模拟交易下单（支持所有交易类型）")
    public Result<SimulatedTradingRecord> placeOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "下单信息", required = true) @RequestBody PlaceOrderRequest request) {
        try {
            SimulatedTradingRecord trade = tradingService.placeOrder(userId, request.getAccountId(),
                    request.getTradeType(), request.getContractType(), request.getPairName(),
                    request.getOrderType(), request.getSide(), request.getLeverage(),
                    request.getQuantity(), request.getPrice(), request.getStopLossPrice(),
                    request.getTakeProfitPrice());
            return Result.success("下单成功", trade);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/close-position/{tradeId}")
    @ApiOperation(value = "平仓", notes = "平仓合约交易")
    public Result<SimulatedTradingRecord> closePosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "交易ID", required = true) @PathVariable Long tradeId,
            @ApiParam(value = "平仓价格", required = true) @RequestParam BigDecimal exitPrice) {
        try {
            SimulatedTradingRecord trade = tradingService.closePosition(userId, tradeId, exitPrice);
            return Result.success("平仓成功", trade);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/cancel-order/{tradeId}")
    @ApiOperation(value = "取消订单", notes = "取消未成交的订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "交易ID", required = true) @PathVariable Long tradeId) {
        try {
            tradingService.cancelOrder(userId, tradeId);
            return Result.success("订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-trades")
    @ApiOperation(value = "获取我的交易记录", notes = "获取当前用户的所有交易记录")
    public Result<Page<SimulatedTradingRecord>> getMyTrades(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<SimulatedTradingRecord> trades = tradingService.getUserTrades(userId, page, size);
            return Result.success(trades);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-open-positions/{accountId}")
    @ApiOperation(value = "获取我的持仓", notes = "获取账户的所有持仓")
    public Result<List<SimulatedTradingRecord>> getMyOpenPositions(
            @ApiParam(value = "账户ID", required = true) @PathVariable Long accountId) {
        try {
            List<SimulatedTradingRecord> positions = tradingService.getAccountOpenPositions(accountId);
            return Result.success(positions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














