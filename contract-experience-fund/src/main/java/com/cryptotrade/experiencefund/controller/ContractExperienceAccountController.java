/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.experiencefund.entity.ContractExperienceAccount;
import com.cryptotrade.experiencefund.service.ContractExperienceAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 合约体验金账户Controller
 */
@RestController
@RequestMapping("/api/experience-fund/account")
@Api(tags = "合约体验金账户")
public class ContractExperienceAccountController {

    @Autowired
    private ContractExperienceAccountService accountService;

    @PostMapping("/create/{activityId}")
    @ApiOperation(value = "创建体验金账户", notes = "用户创建体验金账户")
    public Result<ContractExperienceAccount> createAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "活动ID", required = true) @PathVariable Long activityId) {
        try {
            ContractExperienceAccount account = accountService.createAccount(userId, activityId);
            return Result.success("体验金账户创建成功", account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-account")
    @ApiOperation(value = "获取我的体验金账户", notes = "获取当前用户的体验金账户")
    public Result<ContractExperienceAccount> getMyAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            ContractExperienceAccount account = accountService.getAccountByUserId(userId);
            return Result.success(account);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/can-trade/{accountId}")
    @ApiOperation(value = "检查是否可以交易", notes = "检查账户是否可以交易")
    public Result<Boolean> canTrade(
            @ApiParam(value = "账户ID", required = true) @PathVariable Long accountId) {
        try {
            boolean canTrade = accountService.canTrade(accountId);
            return Result.success(canTrade);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














