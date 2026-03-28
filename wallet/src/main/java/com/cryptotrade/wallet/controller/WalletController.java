/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.wallet.dto.request.AssetTransferRequest;
import com.cryptotrade.wallet.dto.response.AssetTransferResponse;
import com.cryptotrade.wallet.entity.AssetTransferRecord;
import com.cryptotrade.wallet.entity.Wallet;
import com.cryptotrade.wallet.repository.WalletRepository;
import com.cryptotrade.wallet.service.AssetTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@Api(tags = "钱包模块")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private AssetTransferService assetTransferService;

    @GetMapping("/balance")
    @ApiOperation(value = "查询钱包余额", notes = "查询用户各账户的资产余额")
    public Result<Map<String, Object>> getBalance(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        
        Map<String, Object> result = new HashMap<>();
        Map<String, BigDecimal> spotBalances = new HashMap<>();
        Map<String, BigDecimal> futuresBalances = new HashMap<>();
        
        BigDecimal totalValue = BigDecimal.ZERO;
        
        for (Wallet wallet : wallets) {
            if ("SPOT".equals(wallet.getAccountType())) {
                spotBalances.put(wallet.getCurrency(), wallet.getBalance());
            } else if (wallet.getAccountType().startsWith("FUTURES")) {
                futuresBalances.put(wallet.getCurrency(), wallet.getBalance());
            }
            totalValue = totalValue.add(wallet.getBalance());
        }
        
        result.put("spot", spotBalances);
        result.put("futures", futuresBalances);
        result.put("totalValue", totalValue);
        
        return Result.success(result);
    }

    @PostMapping("/transfer")
    @ApiOperation(value = "资产划转", notes = "在不同账户之间划转资产（支持SPOT、FUTURES_USDT、FUTURES_COIN、DELIVERY、FINANCE等账户类型）")
    public Result<AssetTransferResponse> transfer(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AssetTransferRequest request) {
        AssetTransferResponse response = assetTransferService.transfer(userId, request);
        return Result.success("划转成功", response);
    }

    @GetMapping("/asset-transfer/history")
    @ApiOperation(value = "查询资产划转历史", notes = "查询用户的资产划转记录")
    public Result<Page<AssetTransferRecord>> getAssetTransferHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "页码（从0开始）", example = "0") @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "每页大小", example = "20") @RequestParam(defaultValue = "20") int size) {
        Page<AssetTransferRecord> history = assetTransferService.getTransferHistory(userId, page, size);
        return Result.success("查询成功", history);
    }
    
    @GetMapping("/asset-transfer/{transferId}")
    @ApiOperation(value = "查询划转详情", notes = "根据划转记录ID查询详情")
    public Result<AssetTransferRecord> getTransferDetail(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "划转记录ID", required = true) @PathVariable Long transferId) {
        AssetTransferRecord record = assetTransferService.getTransferDetail(transferId);
        // 验证是否为当前用户的记录
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问该划转记录");
        }
        return Result.success("查询成功", record);
    }
}


