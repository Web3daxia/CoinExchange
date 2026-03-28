/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.experiencefund.entity.ContractExperienceTrade;
import com.cryptotrade.experiencefund.service.ContractExperienceTradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合约体验金交易Controller
 */
@RestController
@RequestMapping("/api/experience-fund/trade")
@Api(tags = "合约体验金交易")
public class ContractExperienceTradeController {

    @Autowired
    private ContractExperienceTradeService tradeService;

    @PostMapping("/open-position")
    @ApiOperation(value = "开仓", notes = "开仓交易")
    public Result<ContractExperienceTrade> openPosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "账户ID", required = true) @RequestParam Long accountId,
            @ApiParam(value = "合约类型", required = true) @RequestParam String contractType,
            @ApiParam(value = "交易对", required = true) @RequestParam String pairName,
            @ApiParam(value = "交易类型", required = true) @RequestParam String tradeType,
            @ApiParam(value = "订单类型", required = true) @RequestParam String orderType,
            @ApiParam(value = "杠杆倍数", required = true) @RequestParam Integer leverage,
            @ApiParam(value = "仓位大小", required = true) @RequestParam BigDecimal positionSize,
            @ApiParam(value = "开仓价格", required = true) @RequestParam BigDecimal entryPrice) {
        try {
            ContractExperienceTrade trade = tradeService.openPosition(userId, accountId, contractType, pairName,
                    tradeType, orderType, leverage, positionSize, entryPrice);
            return Result.success("开仓成功", trade);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/close-position/{tradeId}")
    @ApiOperation(value = "平仓", notes = "平仓交易")
    public Result<ContractExperienceTrade> closePosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "交易ID", required = true) @PathVariable Long tradeId,
            @ApiParam(value = "平仓价格", required = true) @RequestParam BigDecimal exitPrice) {
        try {
            ContractExperienceTrade trade = tradeService.closePosition(userId, tradeId, exitPrice);
            return Result.success("平仓成功", trade);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-trades")
    @ApiOperation(value = "获取我的交易记录", notes = "获取当前用户的所有交易记录")
    public Result<Page<ContractExperienceTrade>> getMyTrades(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<ContractExperienceTrade> trades = tradeService.getUserTrades(userId, page, size);
            return Result.success(trades);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-open-positions/{accountId}")
    @ApiOperation(value = "获取我的持仓", notes = "获取账户的所有持仓")
    public Result<List<ContractExperienceTrade>> getMyOpenPositions(
            @ApiParam(value = "账户ID", required = true) @PathVariable Long accountId) {
        try {
            List<ContractExperienceTrade> positions = tradeService.getAccountOpenPositions(accountId);
            return Result.success(positions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

