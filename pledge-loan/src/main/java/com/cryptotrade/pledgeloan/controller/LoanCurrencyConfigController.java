/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;
import com.cryptotrade.pledgeloan.service.LoanCurrencyConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借款币种配置Controller（用户端）
 */
@RestController
@RequestMapping("/api/pledge-loan/loan-currency")
@Api(tags = "质押借币-借款币种配置")
public class LoanCurrencyConfigController {

    @Autowired
    private LoanCurrencyConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "获取借款币种配置列表", notes = "获取所有启用的借款币种配置")
    public Result<List<LoanCurrencyConfig>> getConfigList() {
        try {
            List<LoanCurrencyConfig> configs = configService.getAllConfigs().stream()
                    .filter(c -> "ACTIVE".equals(c.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{currencyCode}")
    @ApiOperation(value = "获取借款币种配置详情", notes = "根据币种代码获取配置详情")
    public Result<LoanCurrencyConfig> getConfigByCurrencyCode(
            @ApiParam(value = "币种代码", required = true) @PathVariable String currencyCode) {
        try {
            LoanCurrencyConfig config = configService.getConfigByCurrencyCode(currencyCode);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














