/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;
import com.cryptotrade.pledgeloan.repository.LoanCurrencyConfigRepository;
import com.cryptotrade.pledgeloan.service.LoanCurrencyConfigService;
import com.cryptotrade.pledgeloan.service.PledgeLoanRateHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 借款币种配置Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/loan-currency")
@Api(tags = "后台管理-借款币种配置")
public class AdminLoanCurrencyConfigController {

    @Autowired
    private LoanCurrencyConfigService configService;

    @Autowired(required = false)
    private PledgeLoanRateHistoryService rateHistoryService;

    @GetMapping("/list")
    @ApiOperation(value = "获取借款币种配置列表", notes = "获取所有借款币种配置")
    public Result<List<LoanCurrencyConfig>> getConfigList(
            @ApiParam(value = "状态") @RequestParam(required = false) String status) {
        try {
            List<LoanCurrencyConfig> configs = configService.getAllConfigs();
            if (status != null && !status.isEmpty()) {
                configs = configs.stream()
                        .filter(c -> status.equals(c.getStatus()))
                        .collect(java.util.stream.Collectors.toList());
            }
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取配置详情", notes = "根据ID获取配置详情")
    public Result<LoanCurrencyConfig> getConfigById(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            LoanCurrencyConfig config = configService.getConfigById(id);
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建配置", notes = "创建新的借款币种配置")
    public Result<LoanCurrencyConfig> createConfig(
            @ApiParam(value = "配置信息", required = true) @RequestBody LoanCurrencyConfig config) {
        try {
            LoanCurrencyConfig saved = configService.createConfig(config);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新配置", notes = "更新借款币种配置")
    public Result<LoanCurrencyConfig> updateConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id,
            @ApiParam(value = "配置信息", required = true) @RequestBody LoanCurrencyConfig config) {
        try {
            LoanCurrencyConfig existing = configService.getConfigById(id);
            
            // 记录利率调整历史
            if (rateHistoryService != null && config.getInterestRate() != null && 
                    !config.getInterestRate().equals(existing.getInterestRate())) {
                rateHistoryService.recordRateChange("LOAN", existing.getCurrencyCode(), "INTEREST_RATE",
                        existing.getInterestRate(), config.getInterestRate(), 0L, "系统", "更新利率");
            }

            LoanCurrencyConfig saved = configService.updateConfig(id, config);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @ApiOperation(value = "更新配置状态", notes = "启用或停用配置")
    public Result<LoanCurrencyConfig> updateStatus(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态: ACTIVE, INACTIVE", required = true) @RequestParam String status) {
        try {
            LoanCurrencyConfig config = configService.updateStatus(id, status);
            return Result.success("状态更新成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除配置", notes = "删除借款币种配置")
    public Result<Void> deleteConfig(
            @ApiParam(value = "配置ID", required = true) @PathVariable Long id) {
        try {
            configService.deleteConfig(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

