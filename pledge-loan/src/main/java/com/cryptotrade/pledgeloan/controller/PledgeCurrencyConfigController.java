/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.PledgeCurrencyConfig;
import com.cryptotrade.pledgeloan.service.PledgeCurrencyConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质押币种配置Controller（用户端）
 */
@RestController
@RequestMapping("/api/pledge-loan/pledge-currency")
@Api(tags = "质押借币-质押币种配置")
public class PledgeCurrencyConfigController {

    @Autowired
    private PledgeCurrencyConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "获取质押币种配置列表", notes = "获取所有启用的质押币种配置")
    public Result<List<PledgeCurrencyConfig>> getConfigList() {
        try {
            List<PledgeCurrencyConfig> configs = configService.getAllConfigs().stream()
                    .filter(c -> "ACTIVE".equals(c.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{currencyCode}")
    @ApiOperation(value = "获取质押币种配置详情", notes = "根据币种代码获取配置详情")
    public Result<PledgeCurrencyConfig> getConfigByCurrencyCode(
            @ApiParam(value = "币种代码", required = true) @PathVariable String currencyCode) {
        try {
            PledgeCurrencyConfig config = configService.getConfigByCurrencyCode(currencyCode);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














