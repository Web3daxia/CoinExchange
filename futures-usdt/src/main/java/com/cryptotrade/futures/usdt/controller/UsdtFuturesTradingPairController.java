/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.futures.usdt.entity.UsdtFuturesTradingPair;
import com.cryptotrade.futures.usdt.service.UsdtFuturesTradingPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/futures-usdt/trading-pair")
@Api(tags = "USDT本位合约交易对管理")
public class UsdtFuturesTradingPairController {
    
    @Autowired
    private UsdtFuturesTradingPairService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加USDT本位合约交易对")
    public Result<UsdtFuturesTradingPair> create(@Valid @RequestBody UsdtFuturesTradingPair pair) {
        try {
            return Result.success("创建成功", service.createTradingPair(pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新USDT本位合约交易对")
    public Result<UsdtFuturesTradingPair> update(@PathVariable Long id, @Valid @RequestBody UsdtFuturesTradingPair pair) {
        try {
            return Result.success("更新成功", service.updateTradingPair(id, pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除USDT本位合约交易对")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteTradingPair(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询USDT本位合约交易对列表")
    public Result<List<UsdtFuturesTradingPair>> list() {
        try {
            return Result.success(service.getAllTradingPairs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询USDT本位合约交易对详情")
    public Result<UsdtFuturesTradingPair> getById(@PathVariable Long id) {
        try {
            return Result.success(service.getTradingPairById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














