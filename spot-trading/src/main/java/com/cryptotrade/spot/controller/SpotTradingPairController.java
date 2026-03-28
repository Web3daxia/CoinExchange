/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.spot.entity.SpotTradingPair;
import com.cryptotrade.spot.service.SpotTradingPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/spot/trading-pair")
@Api(tags = "现货交易对管理")
public class SpotTradingPairController {
    
    @Autowired
    private SpotTradingPairService spotTradingPairService;
    
    @PostMapping("/create")
    @ApiOperation(value = "添加现货交易对", notes = "添加新的现货交易对配置")
    public Result<SpotTradingPair> createTradingPair(@Valid @RequestBody SpotTradingPair tradingPair) {
        try {
            SpotTradingPair result = spotTradingPairService.createTradingPair(tradingPair);
            return Result.success("交易对创建成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新现货交易对", notes = "更新现货交易对配置")
    public Result<SpotTradingPair> updateTradingPair(
            @ApiParam(value = "交易对ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SpotTradingPair tradingPair) {
        try {
            SpotTradingPair result = spotTradingPairService.updateTradingPair(id, tradingPair);
            return Result.success("交易对更新成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除现货交易对", notes = "删除现货交易对")
    public Result<Void> deleteTradingPair(
            @ApiParam(value = "交易对ID", required = true) @PathVariable Long id) {
        try {
            spotTradingPairService.deleteTradingPair(id);
            return Result.success("交易对删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询现货交易对列表", notes = "查询所有现货交易对")
    public Result<List<SpotTradingPair>> getAllTradingPairs() {
        try {
            List<SpotTradingPair> pairs = spotTradingPairService.getAllTradingPairs();
            return Result.success(pairs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/active")
    @ApiOperation(value = "查询可用交易对", notes = "查询启用且可交易的交易对")
    public Result<List<SpotTradingPair>> getActiveTradingPairs() {
        try {
            List<SpotTradingPair> pairs = spotTradingPairService.getActiveTradingPairs();
            return Result.success(pairs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "查询交易对详情", notes = "根据ID查询交易对详情")
    public Result<SpotTradingPair> getTradingPairById(
            @ApiParam(value = "交易对ID", required = true) @PathVariable Long id) {
        try {
            SpotTradingPair pair = spotTradingPairService.getTradingPairById(id);
            return Result.success(pair);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/pair/{pairName}")
    @ApiOperation(value = "根据交易对名称查询", notes = "根据交易对名称查询详情")
    public Result<SpotTradingPair> getTradingPairByPairName(
            @ApiParam(value = "交易对名称", required = true) @PathVariable String pairName) {
        try {
            SpotTradingPair pair = spotTradingPairService.getTradingPairByPairName(pairName);
            return Result.success(pair);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














