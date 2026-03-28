/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.simulatedtrading.dto.request.ResetAccountRequest;
import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccount;
import com.cryptotrade.simulatedtrading.service.SimulatedTradingAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 模拟交易账户Controller
 */
@RestController
@RequestMapping("/api/simulated-trading/account")
@Api(tags = "模拟交易账户")
public class SimulatedTradingAccountController {

    @Autowired
    private SimulatedTradingAccountService accountService;

    @PostMapping("/create")
    @ApiOperation(value = "创建或获取模拟账户", notes = "如果账户不存在则创建，否则返回现有账户")
    public Result<SimulatedTradingAccount> createOrGetAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            SimulatedTradingAccount account = accountService.createOrGetAccount(userId);
            return Result.success("账户获取成功", account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-account")
    @ApiOperation(value = "获取我的模拟账户", notes = "获取当前用户的模拟账户")
    public Result<SimulatedTradingAccount> getMyAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            SimulatedTradingAccount account = accountService.getAccountByUserId(userId);
            return Result.success(account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset")
    @ApiOperation(value = "重置账户余额", notes = "重置模拟账户余额为初始余额或指定金额")
    public Result<SimulatedTradingAccount> resetAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "重置信息") @RequestBody(required = false) ResetAccountRequest request) {
        try {
            SimulatedTradingAccount account = accountService.resetAccount(userId,
                    request != null ? request.getNewBalance() : null);
            return Result.success("账户重置成功", account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














