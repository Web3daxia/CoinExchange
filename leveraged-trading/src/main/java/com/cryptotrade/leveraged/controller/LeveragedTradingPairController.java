/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.leveraged.entity.LeveragedTradingPair;
import com.cryptotrade.leveraged.service.LeveragedTradingPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/leveraged/trading-pair")
@Api(tags = "杠杆合约交易对管理")
public class LeveragedTradingPairController {
    
    @Autowired
    private LeveragedTradingPairService service;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加杠杆合约交易对")
    public Result<LeveragedTradingPair> create(@Valid @RequestBody LeveragedTradingPair pair) {
        try {
            return Result.success("创建成功", service.createTradingPair(pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新杠杆合约交易对")
    public Result<LeveragedTradingPair> update(@PathVariable Long id, @Valid @RequestBody LeveragedTradingPair pair) {
        try {
            return Result.success("更新成功", service.updateTradingPair(id, pair));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除杠杆合约交易对")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            service.deleteTradingPair(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询杠杆合约交易对列表")
    public Result<List<LeveragedTradingPair>> list() {
        try {
            return Result.success(service.getAllTradingPairs());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询杠杆合约交易对详情")
    public Result<LeveragedTradingPair> getById(@PathVariable Long id) {
        try {
            return Result.success(service.getTradingPairById(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














