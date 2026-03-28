/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.wallet.dto.request.TransferRequest;
import com.cryptotrade.wallet.entity.WalletTransfer;
import com.cryptotrade.wallet.service.WalletAssetSummaryService;
import com.cryptotrade.wallet.service.WalletTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 钱包资产划转控制器
 */
@RestController
@RequestMapping("/wallet/transfer")
@Api(tags = "钱包模块 - 资产划转")
public class WalletTransferController {

    @Autowired
    private WalletTransferService walletTransferService;

    @Autowired
    private WalletAssetSummaryService walletAssetSummaryService;

    @GetMapping("/account-balances")
    @Deprecated
    @ApiOperation(value = "查询用户钱包中各账户的资产余额", notes = "查询所有账户类型的余额")
    public Result<Map<String, Object>> getAllAccountBalances(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, Object> balances = walletTransferService.getAllAccountBalances(userId);
            return Result.success(balances);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/execute")
    @ApiOperation(value = "跨账户划转资产", notes = "在不同账户之间划转资产")
    public Result<WalletTransfer> transferBetweenAccounts(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TransferRequest request) {
        try {
            WalletTransfer transfer = walletTransferService.transferBetweenAccounts(
                    userId, request.getFromAccountType(), request.getToAccountType(),
                    request.getCurrency(), request.getAmount(), request.getRemark());
            return Result.success("划转成功", transfer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询钱包资产划转历史记录", notes = "查询用户的钱包资产划转历史")
    public Result<List<WalletTransfer>> getTransferHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<WalletTransfer> transfers = walletTransferService.getTransferHistory(userId);
            return Result.success(transfers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/confirm")
    @ApiOperation(value = "确认资金划转操作", notes = "确认资金划转（需要安全验证）")
    public Result<Void> confirmTransfer(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long transferId,
            @RequestParam String verificationCode) {
        try {
            walletTransferService.confirmTransfer(userId, transferId, verificationCode);
            return Result.success("确认成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/asset-summary")
    @ApiOperation(value = "获取资产汇总数据", notes = "获取资产汇总数据（用于图表展示）")
    public Result<Map<String, Object>> getAssetSummary(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "REAL_TIME") String period,
            @RequestParam(required = false, defaultValue = "CRYPTO") String currencyType) {
        try {
            Map<String, Object> summary = walletAssetSummaryService.getAssetSummary(userId, period, currencyType);
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}









