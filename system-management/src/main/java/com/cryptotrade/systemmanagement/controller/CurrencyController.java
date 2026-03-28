/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.CurrencyCreateRequest;
import com.cryptotrade.systemmanagement.dto.request.CurrencyUpdateRequest;
import com.cryptotrade.systemmanagement.entity.Currency;
import com.cryptotrade.systemmanagement.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/system/currency")
@Api(tags = "币种管理")
public class CurrencyController {
    
    @Autowired
    private CurrencyService currencyService;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加币种", notes = "添加新的币种，支持多语言名称、分类、汇率等")
    public Result<Currency> createCurrency(
            @ApiParam(value = "币种创建请求", required = true) @RequestBody CurrencyCreateRequest request) {
        try {
            Currency currency = currencyService.createCurrency(request);
            return Result.success("币种添加成功", currency);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{currencyId}")
    @ApiOperation(value = "编辑币种", notes = "编辑币种的详细信息")
    public Result<Currency> updateCurrency(
            @ApiParam(value = "币种ID", required = true) @PathVariable Long currencyId,
            @ApiParam(value = "币种更新请求", required = true) @RequestBody CurrencyUpdateRequest request) {
        try {
            Currency currency = currencyService.updateCurrency(currencyId, request);
            return Result.success("币种更新成功", currency);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{currencyId}")
    @ApiOperation(value = "删除币种", notes = "删除币种")
    public Result<Void> deleteCurrency(
            @ApiParam(value = "币种ID", required = true) @PathVariable Long currencyId) {
        try {
            currencyService.deleteCurrency(currencyId);
            return Result.success("币种删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查看币种列表", notes = "查看所有币种")
    public Result<List<Currency>> getAllCurrencies() {
        try {
            List<Currency> currencies = currencyService.getAllCurrencies();
            return Result.success(currencies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{currencyId}")
    @ApiOperation(value = "查看币种详情", notes = "根据ID查看币种详情")
    public Result<Currency> getCurrencyById(
            @ApiParam(value = "币种ID", required = true) @PathVariable Long currencyId) {
        try {
            Currency currency = currencyService.getCurrencyById(currencyId);
            return Result.success(currency);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/code/{currencyCode}")
    @ApiOperation(value = "根据代码查询币种", notes = "根据币种代码查询币种")
    public Result<Currency> getCurrencyByCode(
            @ApiParam(value = "币种代码", required = true) @PathVariable String currencyCode) {
        try {
            Currency currency = currencyService.getCurrencyByCode(currencyCode);
            return Result.success(currency);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/spot-enabled")
    @ApiOperation(value = "查询现货启用币种", notes = "查询现货交易区启用的币种")
    public Result<List<Currency>> getSpotEnabledCurrencies() {
        try {
            List<Currency> currencies = currencyService.getCurrenciesBySpotEnabled();
            return Result.success(currencies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/futures-usdt-enabled")
    @ApiOperation(value = "查询USDT合约启用币种", notes = "查询USDT本位合约启用的币种")
    public Result<List<Currency>> getFuturesUsdtEnabledCurrencies() {
        try {
            List<Currency> currencies = currencyService.getCurrenciesByFuturesUsdtEnabled();
            return Result.success(currencies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/futures-coin-enabled")
    @ApiOperation(value = "查询币本位合约启用币种", notes = "查询币本位合约启用的币种")
    public Result<List<Currency>> getFuturesCoinEnabledCurrencies() {
        try {
            List<Currency> currencies = currencyService.getCurrenciesByFuturesCoinEnabled();
            return Result.success(currencies);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
