/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.wallet.entity.DepositRecord;
import com.cryptotrade.wallet.service.DepositService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deposit")
@Api(tags = "充值模块")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @GetMapping("/addresses")
    @ApiOperation(value = "查询充值地址", notes = "获取所有支持的充值地址和二维码")
    public Result<List<Map<String, Object>>> getDepositAddresses(
            @ApiParam(value = "币种", example = "USDT") @RequestParam(required = false) String currency) {
        try {
            List<Map<String, Object>> addresses = depositService.getDepositAddresses(currency);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/third-party")
    @ApiOperation(value = "第三方充值", notes = "支持第三方充值，获取相关二维码（如优盾钱包）")
    public Result<Map<String, Object>> getThirdPartyDeposit(
            @RequestParam String currency,
            @RequestParam String chain,
            @RequestParam(required = false) BigDecimal amount) {
        try {
            Map<String, Object> result = depositService.getThirdPartyDeposit(currency, chain, amount);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询充值历史", notes = "查询用户的充值记录")
    public Result<List<DepositRecord>> getDepositHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<DepositRecord> records = depositService.getDepositHistory(userId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/record/{depositNo}")
    @ApiOperation(value = "查询充值记录详情", notes = "根据充值单号查询充值记录详情")
    public Result<DepositRecord> getDepositRecord(
            @ApiParam(value = "充值单号", required = true) @PathVariable String depositNo) {
        try {
            DepositRecord record = depositService.getDepositRecord(depositNo);
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

