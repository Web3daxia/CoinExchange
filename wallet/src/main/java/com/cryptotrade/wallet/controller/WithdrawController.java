/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.wallet.dto.request.SubmitWithdrawRequest;
import com.cryptotrade.wallet.dto.request.VerifyWithdrawRequest;
import com.cryptotrade.wallet.entity.WithdrawAddress;
import com.cryptotrade.wallet.entity.WithdrawRecord;
import com.cryptotrade.wallet.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/withdraw")
@Api(tags = "提现模块")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交提现请求", notes = "提交加密货币提现申请")
    public Result<WithdrawRecord> submitWithdraw(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SubmitWithdrawRequest request) {
        try {
            WithdrawRecord record = withdrawService.submitWithdraw(
                    userId, request.getCurrency(), request.getChain(),
                    request.getAddress(), request.getAmount(), request.getAddressId());
            return Result.success("提现申请已提交，请完成验证", record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verify")
    @ApiOperation(value = "提现验证", notes = "提交邮箱/手机验证码、谷歌验证码等进行提现验证")
    public Result<Void> verifyWithdraw(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody VerifyWithdrawRequest request) {
        try {
            withdrawService.verifyWithdraw(userId, request.getWithdrawId(),
                    request.getEmailCode(), request.getPhoneCode(), request.getGoogleCode());
            return Result.success("验证成功，提现已提交审核", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/status")
    @ApiOperation(value = "查询提现状态", notes = "根据提现单号查询提现状态")
    public Result<WithdrawRecord> getWithdrawStatus(
            @ApiParam(value = "提现单号", required = true) @RequestParam String withdrawNo) {
        try {
            WithdrawRecord record = withdrawService.getWithdrawStatus(withdrawNo);
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询提现历史", notes = "查询用户的提现记录")
    public Result<List<WithdrawRecord>> getWithdrawHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<WithdrawRecord> records = withdrawService.getWithdrawHistory(userId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/limit")
    @ApiOperation(value = "查询提现限制", notes = "查询当前账户的提现限制和手续费")
    public Result<Map<String, Object>> getWithdrawLimit(
            @RequestParam String currency,
            @RequestParam String chain,
            @RequestParam BigDecimal amount) {
        try {
            Map<String, Object> limit = withdrawService.getWithdrawLimit(currency, chain, amount);
            return Result.success(limit);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/addresses")
    @ApiOperation(value = "查询提现地址簿", notes = "查询用户的提现地址簿")
    public Result<List<WithdrawAddress>> getWithdrawAddresses(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String chain) {
        try {
            List<WithdrawAddress> addresses = withdrawService.getWithdrawAddresses(userId, currency, chain);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/address")
    @ApiOperation(value = "添加提现地址", notes = "添加提现地址到地址簿")
    public Result<WithdrawAddress> addWithdrawAddress(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam String currency,
            @RequestParam String chain,
            @RequestParam String address,
            @RequestParam(required = false) String addressLabel) {
        try {
            WithdrawAddress withdrawAddress = withdrawService.addWithdrawAddress(
                    userId, currency, chain, address, addressLabel);
            return Result.success("地址添加成功", withdrawAddress);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

