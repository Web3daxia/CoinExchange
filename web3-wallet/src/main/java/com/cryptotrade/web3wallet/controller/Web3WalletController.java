/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.web3wallet.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.web3wallet.entity.Web3TokenBalance;
import com.cryptotrade.web3wallet.entity.Web3Transaction;
import com.cryptotrade.web3wallet.entity.Web3Wallet;
import com.cryptotrade.web3wallet.service.Web3WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Web3钱包控制器
 */
@RestController
@RequestMapping("/web3-wallet")
@Api(tags = "Web3钱包模块")
public class Web3WalletController {
    @Autowired
    private Web3WalletService web3WalletService;

    @PostMapping("/create")
    @ApiOperation(value = "创建钱包", notes = "创建新的Web3钱包")
    public Result<Web3Wallet> createWallet(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "链类型", required = true, example = "ETHEREUM") @RequestParam String chainType,
            @ApiParam(value = "钱包名称") @RequestParam(required = false) String walletName) {
        try {
            Web3Wallet wallet = web3WalletService.createWallet(userId, chainType, walletName);
            return Result.success("钱包创建成功", wallet);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入钱包", notes = "通过私钥或助记词导入钱包")
    public Result<Web3Wallet> importWallet(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "链类型", required = true) @RequestParam String chainType,
            @ApiParam(value = "私钥或助记词", required = true) @RequestParam String privateKeyOrMnemonic,
            @ApiParam(value = "钱包名称") @RequestParam(required = false) String walletName) {
        try {
            Web3Wallet wallet = web3WalletService.importWallet(userId, chainType, privateKeyOrMnemonic, walletName);
            return Result.success("钱包导入成功", wallet);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/wallets")
    @ApiOperation(value = "获取钱包列表", notes = "获取用户的所有Web3钱包")
    public Result<List<Web3Wallet>> getUserWallets(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<Web3Wallet> wallets = web3WalletService.getUserWallets(userId);
            return Result.success(wallets);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/balances/{walletId}")
    @ApiOperation(value = "获取钱包余额", notes = "获取钱包的代币余额")
    public Result<List<Web3TokenBalance>> getWalletBalances(
            @ApiParam(value = "钱包ID", required = true) @PathVariable Long walletId) {
        try {
            List<Web3TokenBalance> balances = web3WalletService.getWalletBalances(walletId);
            return Result.success(balances);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/sync/{walletId}")
    @ApiOperation(value = "同步余额", notes = "同步钱包余额")
    public Result<Void> syncWalletBalance(
            @ApiParam(value = "钱包ID", required = true) @PathVariable Long walletId) {
        try {
            web3WalletService.syncWalletBalance(walletId);
            return Result.success("余额同步成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/send")
    @ApiOperation(value = "发送交易", notes = "发送代币交易")
    public Result<Web3Transaction> sendTransaction(
            @ApiParam(value = "钱包ID", required = true) @RequestParam Long walletId,
            @ApiParam(value = "接收地址", required = true) @RequestParam String toAddress,
            @ApiParam(value = "代币合约地址") @RequestParam(required = false) String tokenContractAddress,
            @ApiParam(value = "金额", required = true) @RequestParam BigDecimal amount) {
        try {
            Web3Transaction transaction = web3WalletService.sendTransaction(walletId, toAddress, tokenContractAddress, amount);
            return Result.success("交易发送成功", transaction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/transactions/{walletId}")
    @ApiOperation(value = "获取交易记录", notes = "获取钱包的交易记录")
    public Result<List<Web3Transaction>> getTransactions(
            @ApiParam(value = "钱包ID", required = true) @PathVariable Long walletId,
            @ApiParam(value = "交易状态") @RequestParam(required = false) String txStatus) {
        try {
            List<Web3Transaction> transactions = web3WalletService.getTransactions(walletId, txStatus);
            return Result.success(transactions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















