/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.futures.coin.entity.CoinFuturesTradingPair;
import com.cryptotrade.futures.coin.service.CoinFuturesTradingPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/futures-coin/trading-pair")
@Api(tags = "币本位永续合约交易对管理")
public class CoinFuturesTradingPairController {
    
    @Autowired
    private CoinFuturesTradingPairService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加币本位合约交易对")
    public Result<CoinFuturesTradingPair> create(@Valid @RequestBody CoinFuturesTradingPair pair) {
        try {
            return Result.success("创建成功", service.createTradingPair(pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新币本位合约交易对")
    public Result<CoinFuturesTradingPair> update(@PathVariable Long id, @Valid @RequestBody CoinFuturesTradingPair pair) {
        try {
            return Result.success("更新成功", service.updateTradingPair(id, pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除币本位合约交易对")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteTradingPair(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询币本位合约交易对列表")
    public Result<List<CoinFuturesTradingPair>> list() {
        try {
            return Result.success(service.getAllTradingPairs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询币本位合约交易对详情")
    public Result<CoinFuturesTradingPair> getById(@PathVariable Long id) {
        try {
            return Result.success(service.getTradingPairById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














