/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.options.entity.OptionsTradingPair;
import com.cryptotrade.options.service.OptionsTradingPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/options/trading-pair")
@Api(tags = "期权合约交易对管理")
public class OptionsTradingPairController {
    
    @Autowired
    private OptionsTradingPairService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加期权合约交易对")
    public Result<OptionsTradingPair> create(@Valid @RequestBody OptionsTradingPair pair) {
        try {
            return Result.success("创建成功", service.createTradingPair(pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新期权合约交易对")
    public Result<OptionsTradingPair> update(@PathVariable Long id, @Valid @RequestBody OptionsTradingPair pair) {
        try {
            return Result.success("更新成功", service.updateTradingPair(id, pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除期权合约交易对")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteTradingPair(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询期权合约交易对列表")
    public Result<List<OptionsTradingPair>> list() {
        try {
            return Result.success(service.getAllTradingPairs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询期权合约交易对详情")
    public Result<OptionsTradingPair> getById(@PathVariable Long id) {
        try {
            return Result.success(service.getTradingPairById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














